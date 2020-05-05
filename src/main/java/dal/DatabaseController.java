package dal;

import java.sql.*;

public final class DatabaseController {

    private static volatile DatabaseController instance = null;

    private DatabaseConnection databaseConnection;

    private DatabaseController() {
        databaseConnection = new DatabaseConnection();
    }

    // https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
    public static DatabaseController getInstance() {
        if (instance == null) {
            synchronized (DatabaseController.class) {
                if (instance == null) {
                    instance = new DatabaseController();
                }
            }
        }

        return instance;
    }

    public boolean authenticateUser(String username, String password) throws DatabaseException {
        boolean doesUsernamePasswordComboExist = false;

        String statement = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            // isBeforeFirst is true if the result set is non-empty, i.e. the user-password-combo exists
            if (rs.isBeforeFirst()) {
                doesUsernamePasswordComboExist = true;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return doesUsernamePasswordComboExist;
    }

    /**
     * Checks if a user exists in the database (by username).
     * @return Returns -1 if the user does not exist. Otherwise returns the user's ID.
     */
    private int checkIfUserExists(String username) throws DatabaseException {
        int userId = -1;

        String statement = "SELECT ID FROM users WHERE username=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)){

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            // isBeforeFirst is false if the result set is empty, i.e. the username does not exist
            if (!rs.isBeforeFirst()) {
                return userId;
            }

            rs.next();
            userId = rs.getInt("ID");

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return userId;
    }

    public void createNewUser(UserDTO user) throws DatabaseException, UsernameTakenException {
        if (checkIfUserExists(user.getUsername()) != -1) {
            throw new UsernameTakenException("Username is taken.");
        }

        String statement = "INSERT INTO users (username, email, password) VALUES (?,?,?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }
    }

    /**
     * Updates a user in the database (by username, i.e. email and password can be changed).
     * It is already implicitly tested whether the user exists or not as long as the endpoint that uses this method
     * calls the authenticateUser method first (which it probably always should).
     */
    public void updateUser(UserDTO user) throws DatabaseException {
        String statement = "UPDATE users SET email=?, password=? WHERE username=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUsername());

            ps.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }
    }

    /**
     * Get a user in the database (by username).
     * It is already implicitly tested whether the user exists or not as long as the endpoint that uses this method
     * calls the authenticateUser method first (which it probably always should).
     */
    public UserDTO getUser(String username) throws DatabaseException {
        UserDTO user;

        String statementUser = "SELECT username, email, password FROM users WHERE username = ?";
        String statementKeys = "SELECT service_apikey, service_username, service_company, service_email " +
                "FROM users INNER JOIN api_keys ON users.ID = api_keys.userid " +
                "WHERE apikey_status ='live' AND username = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement psUser = connection.prepareStatement(statementUser);
             PreparedStatement psKeys = connection.prepareStatement(statementKeys)) {

            connection.setAutoCommit(false);

            psUser.setString(1, username);

            ResultSet rs = psUser.executeQuery();

            rs.next();
            String email = rs.getString("email");
            String password = rs.getString("password");
            user = new UserDTO(username, email, password);

            // Starting to add the API keys
            psKeys.setString(1, username);

            rs = psKeys.executeQuery();

            // isBeforeFirst is false if the result set is empty, i.e. the user has no active keys
            if (!rs.isBeforeFirst()) {
                return user;
            }

            while (rs.next()) {
                String service_apikey = rs.getString("service_apikey");
                String service_username = rs.getString("service_username");
                String service_company = rs.getString("service_company");
                String service_email = rs.getString("service_email");
                ApiKeyDTO key = new ApiKeyDTO(service_apikey, service_username, service_company, service_email);
                user.addApikey(key);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return user;
    }

    /**
     * Checks whether the given user already has a key with the given service provider/company and updates
     * or creates the key accordingly. An update sets the old key's status to 'invalid' and creates a new key.
     * The service provider name is not validated here and therefore probably should be at application level.
     */
    public void createOrUpdateApiKey(String username, ApiKeyDTO key) throws DatabaseException {
        int userId = checkIfUserExists(username);
        UserDTO user = getUser(username);

        boolean doesActiveKeyAlreadyExist = false;

        for (ApiKeyDTO existingKey : user.getApikeys()) {
            if (existingKey.getService_company().equalsIgnoreCase(key.getService_company())) {
                doesActiveKeyAlreadyExist = true;
            }
        }

        if (doesActiveKeyAlreadyExist == true) {
            updateApiKey(userId, key);
        } else {
            createApiKey(userId, key);
        }
    }

    private void updateApiKey(int userId, ApiKeyDTO key) throws DatabaseException {
        String statement = "UPDATE api_keys SET apikey_status='invalid' WHERE userid=? AND service_company=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setInt(1, userId);
            ps.setString(2, key.getService_company());

            ps.executeUpdate();

            connection.commit();

            /* Breaking this up into two commits technically violates ACID, but that shouldn't become
            relevant, unless the same user has two open sessions and is updating API keys on both at the same time. */
            createApiKey(userId, key);

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }
    }

    private void createApiKey(int userId, ApiKeyDTO key) throws DatabaseException {
        String statement = "INSERT INTO api_keys (userid, service_apikey, service_username, service_company, " +
                "date_created, service_email, apikey_status) VALUES (?,?,?,?,?,?,'live')";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setInt(1, userId);
            ps.setString(2, key.getService_apikey());
            ps.setString(3, key.getService_username());
            ps.setString(4, key.getService_company());
            ps.setDate(5, new Date(System.currentTimeMillis())); // Does this cause problems once deployed?
            ps.setString(6, key.getService_email());

            ps.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }
    }
}
