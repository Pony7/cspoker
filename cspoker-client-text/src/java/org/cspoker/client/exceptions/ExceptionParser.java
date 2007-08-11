package org.cspoker.client.exceptions;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExceptionParser extends DefaultHandler {

    private HttpException exception;

    private StringBuilder sb=new StringBuilder();
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch,start,length);
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
	if(name.equalsIgnoreCase("msg")){
	    exception=new HttpException(sb.toString());
	}if(name.equalsIgnoreCase("stacktrace")){
	    exception.setStackTraceString(sb.toString());
	}
	sb.setLength(0);
    }
    
    public Exception getException(){
	return exception;
    }
    
}
