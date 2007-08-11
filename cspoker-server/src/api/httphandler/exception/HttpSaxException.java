package api.httphandler.exception;

import org.xml.sax.SAXException;

public class HttpSaxException extends SAXException implements HttpException{

    private static final long serialVersionUID = 1886168467073180945L;
    
    private final int status;
    
    public HttpSaxException(Exception e, int status) {
	super(e);
	this.status=status;
    }
    
    public int getStatus(){
	return status;
    }
    
}
