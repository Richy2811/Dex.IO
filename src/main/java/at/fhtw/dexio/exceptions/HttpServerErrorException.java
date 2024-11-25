package at.fhtw.dexio.exceptions;

public class HttpServerErrorException extends HttpHandlerException {
    public HttpServerErrorException(){
        super();
    }

    public HttpServerErrorException(String message) {
        super(message);
    }
}
