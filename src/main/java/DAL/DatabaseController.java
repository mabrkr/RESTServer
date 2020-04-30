package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseController {

    public void eksempelMetode() {
        try (Connection connection = DatabaseConnection.getConnection()){
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
