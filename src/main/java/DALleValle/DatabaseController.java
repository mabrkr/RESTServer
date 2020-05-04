package DALleValle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void eksempelMetode() {
        try (Connection connection = databaseConnection.getConnection();
             /*
             * TODO: Denne tilgang lukker db-forbindelse efter metode-kaldet, er det smart?
             */
             PreparedStatement ps = connection.prepareStatement("")){

            ResultSet rs = ps.executeQuery();
            // Do something

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    // TODO: inputvalidering, findes bruger allerede etc.
    public boolean createNewUser(UserDTO user) throws DatabaseException {
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

    public UserDTO getUser(String username) throws DatabaseException {
        UserDTO user;

        String statement = "SELECT username, email, password, service_company, service_apikey " +
                "FROM users INNER JOIN api_keys ON users.ID = api_keys.userid " +
                "WHERE apikey_status ='live' AND username = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(statement)) {

            connection.setAutoCommit(false);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            rs.next();
            String email = rs.getString("email");
            String password = rs.getString("password");
            user = new UserDTO(username, email, password);
            user.addApikey(rs.getString("service_company"), rs.getString("service_apikey"));

            // Add the rest of the API keys
            while (rs.next()) {
                user.addApikey(rs.getString("service_company"), rs.getString("service_apikey"));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return user;
    }
}
