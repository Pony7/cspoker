package org.cspoker.client.request;

import java.net.MalformedURLException;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.HttpPostRequest;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class JoinTableRequest extends HttpPostRequest{

    public JoinTableRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	if(args.length<1)
	    throw new IllegalArgumentException("Not enough arguments.");
	
	this.id=args[0];
	request.startElement("", "id", "id", new AttributesImpl());
	String s=args[0];
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "id", "id");
    }

    @Override
    protected String getPath() {
	return "/table/join/";
    }

    private String id="";
    
    @Override
    protected String getResult() {
	return "Joined table "+id+"."+n;
    }

}
