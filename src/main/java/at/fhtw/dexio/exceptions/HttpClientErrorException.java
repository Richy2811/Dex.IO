package at.fhtw.dexio.exceptions;

public class HttpClientErrorException extends HttpHandlerException {
    public HttpClientErrorException(){
        super();
    }

    public HttpClientErrorException(String message) {
        super(message);
    }
}
