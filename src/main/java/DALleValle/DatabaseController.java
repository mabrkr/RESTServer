package DALleValle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
     * Checks if a user exists in the database.
     * @param username is the relevant parameter.
     * @return Returns -1 if the user does not exist. Otherwise returns the user's ID.
     */
    public int checkIfUserExists(String username) throws DatabaseException {
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

    public boolean createNewUser(UserDTO user) throws DatabaseException, UsernameTakenException {
        if (checkIfUserExists(user.getUsername()) != -1) {
            throw new UsernameTakenException("Username is taken.");
        }

        boolean isUserCreated = false;

        String statement = "INSERT INTO users (username, email, password) VALUES (?,?,?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int updatedRows = ps.executeUpdate();

            if (updatedRows == 0) {
                isUserCreated = false;
            } else {
                connection.commit();
                isUserCreated = true;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return isUserCreated;
    }

    /* It is already implicitly tested whether the user exists or not as long as the endpoint that uses this method
    calls the authenticateUser method first (which it probably should) */
    public boolean updateUser(UserDTO user) throws DatabaseException {
        boolean isUserUpdated = false;

        String statement = "UPDATE users SET email=?, password=? WHERE username=?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUsername());

            int updatedRows = ps.executeUpdate();

            if (updatedRows == 0) {
                isUserUpdated = false;
            } else {
                connection.commit();
                isUserUpdated = true;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return isUserUpdated;
    }

    /* It is already implicitly tested whether the user exists or not as long as the endpoint that uses this method
    calls the authenticateUser method first (which it probably should) */
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

    // TODO: Set old key to inactive (if it exists) and new to live
    // SET USER ID FOR KEYS MANUALLY
    // SET DATE CREATED MANUALLY this.date_created = new Date(System.currentTimeMillis()); // Does this cause problems once deployed?
    public boolean updateApiKeys(String username, HashMap<String, String> keys) throws DatabaseException {
        boolean areApiKeysUpdated = false;
        int userID = checkIfUserExists(username);
        int numberOfUpdatedRows = 0;

        String statement = "INSERT INTO api_keys (userid, service_company, service_apikey, apikey_status) VALUES (?,?,?)";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            // No validation of the service provider name here means that it probably should happen at application level
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                ps.setInt(1, userID);
                ps.setString(2, entry.getKey());
                ps.setString(3, entry.getValue());
                ps.executeUpdate();
                numberOfUpdatedRows ++;
            }

            if (numberOfUpdatedRows != 0) {
                areApiKeysUpdated = true;
            }

            connection.commit();

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return areApiKeysUpdated;
    }
}
