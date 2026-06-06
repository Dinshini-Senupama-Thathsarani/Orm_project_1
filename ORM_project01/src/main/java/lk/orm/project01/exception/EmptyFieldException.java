package lk.orm.project01.exception;


public class EmptyFieldException extends Exception {

    public EmptyFieldException(String message) {
        super(message);
    }

    public EmptyFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
