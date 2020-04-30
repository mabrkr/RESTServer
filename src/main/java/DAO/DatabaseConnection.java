package DAO;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {

    private static Connection conn;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                String hostname = "127.0.0.1:3306/?serverTimezone=UTC#";
                String db = "zadmin_dist";
                String user = "root";
                String password = "mysql";
                String dataBase = "jdbc:mysql://" + hostname + "/" + db;
                conn = DriverManager.getConnection(dataBase, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

