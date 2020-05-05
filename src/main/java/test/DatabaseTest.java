package test;

import dal.ApiKeyDTO;
import dal.DatabaseController;
import dal.UserDTO;

public class DatabaseTest {

    public static void main(String[] args) throws Exception {
        UserDTO user;

        DatabaseController.getInstance().createNewUser(new UserDTO("haha", "hehe", "hoho"));

        user = DatabaseController.getInstance().getUser("haha");
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getApikeys());
        System.out.println();

        DatabaseController.getInstance().updateUser(new UserDTO("haha", "hehe", "hihi"));

        user = DatabaseController.getInstance().getUser("haha");
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getApikeys());
        System.out.println();

        DatabaseController.getInstance().createOrUpdateApiKey("haha", new ApiKeyDTO("key", "username", "company", "email"));

        user = DatabaseController.getInstance().getUser("haha");
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getApikeys());
        System.out.println();

        DatabaseController.getInstance().createOrUpdateApiKey("haha", new ApiKeyDTO("key", "username", "company", "email"));

        user = DatabaseController.getInstance().getUser("haha");
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        System.out.println(user.getApikeys());
        System.out.println();
    }
}
