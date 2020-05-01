package dal;

import java.util.HashMap;

public class UserDTO {
    private String username;
    private String email;
    private String password;
    private HashMap<String, String> apiKeys;

    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserDTO(String username, HashMap<String, String> apiKeys) {
        this.username = username;
        this.apiKeys = apiKeys;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<String, String> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(HashMap<String, String> apiKeys) {
        this.apiKeys = apiKeys;
    }
}
