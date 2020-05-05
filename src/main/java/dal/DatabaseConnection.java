package dal;

import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;


class DatabaseConnection {

    private Connection conn;

    private final String db;
    private final String user;
    private final String password;

    DatabaseConnection() {
        String tempDb = "";
        String tempUser = "";
        String tempPassword = "";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("..\\Auth\\database.txt")))){
            tempDb = bufferedReader.readLine();
            tempUser = bufferedReader.readLine();
            tempPassword = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db = tempDb;
        user = tempUser;
        password = tempPassword;
    }

    Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                String hostname = "127.0.0.1:3306";
                String timezone = "?serverTimezone=UTC";
                String dataBase = "jdbc:mysql://" + hostname + "/" + db + timezone;
                conn = DriverManager.getConnection(dataBase, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

