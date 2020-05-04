package DALleValle;

import java.util.HashMap;

public class UserDTO {

    private String username;
    private String email;
    private String password;
    private HashMap<String, String> apikeys;

    public UserDTO(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;

        apikeys = new HashMap<>();
    }

    public void addApikey(String service, String key) {
        apikeys.put(service, key);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public HashMap<String, String> getApikeys() {
        return apikeys;
    }
}
