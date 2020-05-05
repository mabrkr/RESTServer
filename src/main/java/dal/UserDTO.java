package dal;

import java.util.ArrayList;

public class UserDTO {

    private String username;
    private String email;
    private String password;
    private ArrayList<ApiKeyDTO> apikeys;

    public UserDTO(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;

        apikeys = new ArrayList<>();
    }

    public void addApikey(ApiKeyDTO key) {
        apikeys.add(key);
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

    public ArrayList<ApiKeyDTO> getApikeys() {
        return apikeys;
    }
}
