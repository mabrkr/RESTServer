package dal;

public class DatabaseException extends Exception {

    public DatabaseException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

    public DatabaseException(String errorMessage) {
        super(errorMessage);
    }
}
