package at.fhtw.dexio.exceptions;

public class HttpHandlerException extends RuntimeException{
    public HttpHandlerException(){
        super();
    }

    public HttpHandlerException(String message) {
        super(message);
    }
}
