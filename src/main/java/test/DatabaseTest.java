package test;

import DALleValle.DatabaseController;

public class DatabaseTest {

    public static void main(String[] args) throws Exception {
        boolean result1 = DatabaseController.getInstance().authenticateUser("lol","lol");
        System.out.println(result1);
        boolean result2 = DatabaseController.getInstance().authenticateUser("SouthStruds", "MalteMalte");
        System.out.println(result2);
    }
}
