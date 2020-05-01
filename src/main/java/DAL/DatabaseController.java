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
            * Måske ikke hvis databasen skal bruges hele tiden...
            */

            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("");
            ResultSet rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
