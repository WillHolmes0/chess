package server.exception;

public class UnauthorizedException extends RuntimeException {
    private int code = 401;

    public UnauthorizedException(String message) {
        super(message);
    }

    public int getCode() {return code;}

    public MessageWrapper messageWrapper() {
        return new MessageWrapper(getMessage());
    }
}
