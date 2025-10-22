package exception;

public class AlreadyTakenException extends RuntimeException {
    private int code = 403;

    public AlreadyTakenException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public MessageWrapper messageWrapper() {
        return new MessageWrapper(getMessage());
    }

}
