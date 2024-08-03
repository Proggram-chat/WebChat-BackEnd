package sanity.nil.webchat.application.exceptions;

public class CentrifugoException extends RuntimeException {

    private int statusCode;

    public CentrifugoException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
