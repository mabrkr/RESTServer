package test;

import DALleValle.DatabaseController;
import DALleValle.UserDTO;

public class DatabaseTest {

    public static void main(String[] args) throws Exception {
        boolean result1 = DatabaseController.getInstance().authenticateUser("lol","lol");
        System.out.println(result1);
        boolean result2 = DatabaseController.getInstance().authenticateUser("SouthStruds", "MalteMalte");
        System.out.println(result2);
        UserDTO result3 = DatabaseController.getInstance().getUser("SouthStruds");
        System.out.println(result3.getUsername());
        System.out.println(result3.getEmail());
        System.out.println(result3.getPassword());
        System.out.println(result3.getApikeys());
    }
}
