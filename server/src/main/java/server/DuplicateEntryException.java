package server;

public class DuplicateEntryException extends Exception{
    public DuplicateEntryException(String message) {
        super(message);
    }

}
