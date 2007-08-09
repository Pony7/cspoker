package api.httphandler;

import java.io.IOException;

import javax.security.sasl.SaslException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class GetHandler extends HttpHandlerImpl {

    public GetHandler() {
	super();
    }

    public void handle(HttpExchange http) throws IOException {
        
        TransformerHandler response=null;
        StreamResult requestResult;
        try {
            requestResult = new StreamResult(http.getResponseBody());
            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
            .newInstance();
            
            response = tf.newTransformerHandler();
            response.setResult(requestResult);
        } catch (Exception e) {
            throwException(http, e);
            return;
        }
        http.sendResponseHeaders(200, 0);
        try{
            response.startDocument();
            respond(response);
            response.endDocument();
        }catch (SAXException e) {
	    try{response.endDocument();}catch(Throwable t){}
            e.printStackTrace();
	    throw new IOException(e.getMessage());
	}
        http.getResponseBody().close();
        http.close();
                		
    }
    
    protected abstract void respond(TransformerHandler response)
	    throws SAXException;

}