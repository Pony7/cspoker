package org.cspoker.server.sockets;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Authenticator {

    private static Logger logger = Logger.getLogger(Authenticator.class);
    
    private XmlFileAuthenticator auth;

    public Authenticator(XmlFileAuthenticator auth) {
	this.auth = auth;
    }

    public boolean authenticate(ClientContext context, String xml){
	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    LoginHandler handler = new LoginHandler();
	    xr.setContentHandler(handler);
	    xr.parse(new InputSource(new StringReader(xml)));
	    if(!auth.hasPassword(handler.getUsername(), handler.getPassword())){
		logger.info("login failed for "+handler.getUsername());
		return false;
	    }
	    context.setPassword(handler.getPassword());
	    context.setUsername(handler.getUsername());
	    context.setUseragent(handler.getUseragent());
	    context.setAuthenticated();
	    return true;
	} catch (SAXException e) {
	    logger.error("error parsing login: "+e.getMessage());
	    return false;
	} catch (IOException e) {
	    logger.error("error parsing login: "+e.getMessage());
	    return false;
	}
    }

    private class LoginHandler extends DefaultHandler{

	private String useragent="unknown";
	private String username="John Doe";
	private String password="";

	public String getUseragent() {
	    return useragent;
	}

	public String getUsername() {
	    return username;
	}

	public String getPassword() {
	    return password;
	}

	@Override
	public void startElement(String uri, String localName, String name,
		Attributes attributes) throws SAXException {
	    if (name.equals("login")) {
		if(attributes.getValue("useragent")!=null)
		    useragent = attributes.getValue("useragent");
		if(attributes.getValue("username")!=null)
		    username = attributes.getValue("username");
		if(attributes.getValue("password")!=null)
		password = attributes.getValue("password");
	    }
	}

    }

}
