package server;

public class NotAuthenticatedException extends Exception {
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
