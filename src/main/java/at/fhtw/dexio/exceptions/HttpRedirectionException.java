package at.fhtw.dexio.exceptions;

public class HttpRedirectionException extends HttpHandlerException {
    public HttpRedirectionException() {
        super();
    }

    public HttpRedirectionException(String message) {
        super(message);
    }
}
