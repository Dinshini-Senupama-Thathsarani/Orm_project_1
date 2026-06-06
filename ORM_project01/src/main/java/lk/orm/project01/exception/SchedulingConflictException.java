package lk.orm.project01.exception;


public class SchedulingConflictException extends Exception {

    public SchedulingConflictException(String message) {
        super(message);
    }

    public SchedulingConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
