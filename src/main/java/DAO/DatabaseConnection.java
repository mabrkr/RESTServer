package DAO;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {

    public static void main(String[] args) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        }
//        catch(ClassNotFoundException ex) {
//            System.out.println("Error: unable to load driver class!");
//            System.exit(1);
//        }
        Connection conn = getConnection();
        dbcall(conn);

    }


    public static Connection getConnection() {
        String hostname = "127.0.0.1:3306/?serverTimezone=UTC#";
        String db = "zadmin_dist";
        String username = "root";
        String passwordx = "testtest";
        Connection conn = null;


        try {
            if (conn == null || conn.isClosed()) {
                String dataBase = "jdbc:mysql://" + hostname + "/" + db;
                String user = username;
                String password = passwordx;
                System.out.println(dataBase);
                conn = DriverManager.getConnection(dataBase, user, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return conn;
    }

    public static void dbcall(Connection conn){
        try{
            conn.setAutoCommit(false);
            PreparedStatement statement = conn
                    .prepareStatement("select * from zadmin_dist.users");
            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }




}

