package org.cspoker.client.request.contenthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EventsContentHandler extends DefaultHandler {

    private List<String> events;
    
    private String lastID;
    
    private StringBuilder sb=new StringBuilder();
    
    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public String getLastID() {
        return lastID;
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
        if(name.equalsIgnoreCase("events")){
            lastID=attributes.getValue("lastEventNumber");
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if(name.equalsIgnoreCase("msg")){
            events.add(sb.toString());
        }
        sb.setLength(0);
    }
    
}
