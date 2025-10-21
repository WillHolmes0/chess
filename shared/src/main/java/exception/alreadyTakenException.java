package exception;

public class alreadyTakenException extends RuntimeException {
    private int code = 403;

    public alreadyTakenException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}
