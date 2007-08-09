package api.httphandler;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class PingHandler extends GetHandler {
    
    @Override
    protected void respond(TransformerHandler response)
	    throws SAXException {
	response.startElement("", "pong", "pong", new AttributesImpl());
	response.endElement("", "pong", "pong");
	System.out.println("pong ok");
    }


}
