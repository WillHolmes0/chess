package exception;

public class BadRequestException extends RuntimeException {
    private int code = 400;

    public BadRequestException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public MessageWrapper messageWrapper() {
        return new MessageWrapper(getMessage());
    }
}
