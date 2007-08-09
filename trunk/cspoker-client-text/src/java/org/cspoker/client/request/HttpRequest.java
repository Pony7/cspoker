package org.cspoker.client.request;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.client.exceptions.FailedAuthenticationException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public abstract class HttpRequest extends DefaultHandler {

    private URL url;

    public HttpRequest(String address) throws MalformedURLException {
	this.url=new URL(address+getPath());
    }
    
    protected URL getURL(){
	return url;
    }

    protected abstract String getPath();
    
    protected abstract boolean isDoOutput();
    
    protected abstract void doOutput(TransformerHandler request);
    
    public void send() throws Exception{
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setInstanceFollowRedirects(false);
	connection.setDoOutput(isDoOutput());
	
	if (isDoOutput()) {
	    StreamResult requestResult = new StreamResult(connection
		    .getOutputStream());
	    SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
		    .newInstance();
	    TransformerHandler request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    doOutput(request);
	}
	
	XMLReader xr = XMLReaderFactory.createXMLReader();
	xr.setContentHandler(this);
	xr.setErrorHandler(this);
	try {
	    xr.parse(new InputSource(connection.getInputStream()));
	} catch (ProtocolException e) {
	    //TODO fix problem with 20 redirects when login fails.
	    throw new FailedAuthenticationException("Authentication failed.");
	}
    }
    
}
