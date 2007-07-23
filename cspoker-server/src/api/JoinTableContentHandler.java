package api;

import java.io.OutputStream;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class JoinTableContentHandler extends DefaultHandler {

    private OutputStream out;
    private JoinTableHandler handler;

    private StringBuilder chars = new StringBuilder();
    private long id;
    private String tablename;
    
    private boolean idset=false;
    private boolean tableset=false;
    
    public JoinTableContentHandler(JoinTableHandler handler, OutputStream out) {
	this.handler = handler;
	this.out = out;
    }

    
    @Override
    public void endDocument() throws SAXException {
	respond();
    }

    private void respond() {
	StreamResult requestResult = new StreamResult(out);
	SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
		.newInstance();
	TransformerHandler request;

	List<String> players;
	
	try {
	    if(!idset || !tableset)
		throw new SAXException("Illegal syntax.");
	    handler.joinTable(id, tablename);
	    players = handler.getPlayersExceptFor(id, tablename);
	} catch (Exception e) {
	    try {
		request = tf.newTransformerHandler();
		request.setResult(requestResult);
		request.startDocument();
		String comment = "CSPoker jointable response (exception)";
		request.comment(comment.toCharArray(), 0, comment.length());
		Attributes noattrs = new AttributesImpl();
		request.startElement("", "jointable", "jointable", noattrs);
		request.startElement("", "exception", "exception", noattrs);
		request.characters(e.getMessage().toCharArray(), 0, e
			.getMessage().length());
		request.endElement("", "exception", "exception");
		request.endElement("", "jointable", "jointable");
		request.endDocument();
	    } catch (SAXException f) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (TransformerConfigurationException f) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    return;
	}

	try {
	    request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    request.startDocument();
	    String comment = "CSPoker jointable response";
	    request.comment(comment.toCharArray(), 0, comment.length());
	    Attributes noattrs = new AttributesImpl();
	    request.startElement("", "jointable", "jointable", noattrs);
	    request.startElement("", "status", "status", noattrs);
	    String status = "ok";
	    request.characters(status.toCharArray(), 0, status.length());
	    request.endElement("", "status", "status");
	    if(players.size()>0){
		request.startElement("", "players", "players", noattrs);
		for(String name:players){
		    request.startElement("", "player", "player", noattrs);
		    request.characters(name.toCharArray(), 0, name.length());
		    request.endElement("", "player", "player");  
		}
		request.endElement("", "players", "players");
	    }

	    
	    request.endElement("", "jointable", "jointable");
	    request.endDocument();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if ("jointable".equals(qName)||"id".equals(qName)||"table".equals(qName)) {
	    // no op
	} else {
	    throw new SAXException("Illegal syntax:" + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if ("jointable".equals(qName)) {
	    // no op
	} else if ("id".equals(qName)) {
	    id = Long.parseLong(chars.toString());
	    idset=true;
	} else if ("table".equals(qName)) {
	    tablename = chars.toString();
	    tableset=true;
	} else {
	    throw new SAXException("Illegal syntax:" + qName);
	}
	chars.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	chars.append(ch, start, length);

    }
    
}
