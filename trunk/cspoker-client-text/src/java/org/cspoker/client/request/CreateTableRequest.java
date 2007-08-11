package org.cspoker.client.request;

import java.net.MalformedURLException;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.OutputRequest;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class CreateTableRequest extends OutputRequest{

    public CreateTableRequest(String address) throws MalformedURLException {
	super(address);
    }
    
    @Override
    protected String getRequestMethod() {
        return "PUT";
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	request.startElement("", "name", "name", new AttributesImpl());
	String s="default";
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "name", "name");
    }

    @Override
    protected String getPath() {
	return "/table/create/";
    }

    @Override
    protected String getResult() {
	return "Table created with ID "+ id + "."+n;
    }
    
    private String id="unknown";
    private StringBuilder sb=new StringBuilder();
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch, start, length);
    }
    
    @Override
    public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
        sb.setLength(0);
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if(name.equalsIgnoreCase("id"))
            id=sb.toString();
	sb.setLength(0);
    }

}
