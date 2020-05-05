package DALleValle;

public class UsernameTakenException extends Exception {

    public UsernameTakenException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public UsernameTakenException(String errorMessage) {
        super(errorMessage);
    }
}
