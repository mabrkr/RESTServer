package test;

import DALleValle.DatabaseController;
import DALleValle.UserDTO;

import java.util.HashMap;

public class DatabaseTest {

    public static void main(String[] args) throws Exception {
//        boolean result1 = DatabaseController.getInstance().authenticateUser("lol","lol");
//        System.out.println(result1);
//
//        boolean result2 = DatabaseController.getInstance().authenticateUser("SouthStruds", "MalteMalte");
//        System.out.println(result2);

//        HashMap<String, String> keys = new HashMap<>();
//        keys.put("asdf", "1234");
//        keys.put("qwerty", "4321");
//        boolean result6 = DatabaseController.getInstance().updateApiKeys("haha", keys);
//        System.out.println(result6);

        UserDTO result3 = DatabaseController.getInstance().getUser("SouthStruds");
        System.out.println(result3.getUsername());
        System.out.println(result3.getEmail());
        System.out.println(result3.getPassword());
        System.out.println(result3.getApikeys());

//        UserDTO user1 = new UserDTO("haha", "hehe", "hoho");
//        user1.addApikey("lol", "rofl");
//        boolean result4 = DatabaseController.getInstance().createNewUser(user1);
//        System.out.println(result4);
//        UserDTO result5 = DatabaseController.getInstance().getUser("SouthStruds");
//        System.out.println(result5.getUsername());
//        System.out.println(result5.getEmail());
//        System.out.println(result5.getPassword());
//        System.out.println(result5.getApikeys());


    }
}
