package dal;

public class ApiKeyDTO {

    private String service_apikey;
    private String service_username;
    private String service_company;
    private String service_email;

    public ApiKeyDTO(String service_apikey, String service_username, String service_company, String service_email) {
        this.service_apikey = service_apikey;
        this.service_username = service_username;
        this.service_company = service_company;
        this.service_email = service_email;
    }

    public String getService_apikey() {
        return service_apikey;
    }

    public String getService_username() {
        return service_username;
    }

    public String getService_company() {
        return service_company;
    }

    public String getService_email() {
        return service_email;
    }
}
