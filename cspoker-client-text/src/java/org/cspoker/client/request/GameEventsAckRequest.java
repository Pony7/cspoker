package org.cspoker.client.request;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.OutputRequest;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class GameEventsAckRequest extends OutputRequest{

    private List<String> events;
    private String lastID;
    
    private StringBuilder sb=new StringBuilder();
    
    public GameEventsAckRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	if(args.length<1)
	    throw new IllegalArgumentException("Not enough arguments.");
	
	request.startElement("", "ack", "ack", new AttributesImpl());
	String s=args[0];
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "ack", "ack");
    }

    @Override
    protected String getPath() {
	return "/game/events/ack/";
    }
    
    @Override
    protected String getResult() {
	if(events.size()==0)
	    return "No events found."+n;
	String r="";
	for(String event:events){
	    r+=event+n;
	}
	return r;
    }
    
    @Override
    public void startDocument() throws SAXException {
        events=new ArrayList<String>();
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch, start, length);
    }
    
    @Override
    public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
        sb.setLength(0);
        lastID=attributes.getValue("id");
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if(name.equalsIgnoreCase("event")){
            events.add(lastID+": "+sb.toString());
            sb.setLength(0);
        }
    }

}
