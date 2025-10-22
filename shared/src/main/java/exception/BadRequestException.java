package exception;

public class BadRequestException extends RuntimeException {
    private int code = 403;
    private String message;

    public BadRequestException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public MessageWrapper messageWrapper() {
        return new MessageWrapper(message);
    }
}
