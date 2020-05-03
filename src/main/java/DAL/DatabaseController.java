package DAL;

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
        try (Connection connection = databaseConnection.getConnection()){
            /*
            * TODO: Denne tilgang lukker db-forbindelse efter metode-kaldet, er det smart?
            */

            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("");
            ResultSet rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String username, String password) throws DatabaseException {
        boolean doesUserExist = false;

        try (Connection connection = databaseConnection.getConnection()){
            connection.setAutoCommit(false);
            String statement = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            // isBeforeFirst is false if the result set is empty, i.e. the user-password-combo does not exist
            if (rs.isBeforeFirst()) {
                doesUserExist = true;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return doesUserExist;
    }

    // TODO: inputvalidering, findes bruger allerede etc.
    public boolean createNewUser(String username, String email, String password) throws DatabaseException {
        boolean isUserCreated = false;

        try (Connection connection = databaseConnection.getConnection()){
            connection.setAutoCommit(false);
            String statement = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(statement);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);

            int updatedRows = ps.executeUpdate();

            if (updatedRows == 0) {
                isUserCreated = false;
            } else {
                isUserCreated = true;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database error", e);
        }

        return isUserCreated;
    }
}
