package org.cspoker.client.request;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.cspoker.client.request.abstracts.HttpGetRequest;
import org.xml.sax.SAXException;

public class ListTablesRequest extends HttpGetRequest {

    private List<String> tables;
    private StringBuilder sb=new StringBuilder();
    
    public ListTablesRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getResult() {
	if(tables.size()==0)
	    return "No tables found."+n;
	String r="Table ID's:"+n;
	for(String s:tables){
	    r+=s+n;
	}
	return r;
    }

    @Override
    protected String getPath() {
	return "/table/list/";
    }
    
    @Override
    public void startDocument() throws SAXException {
        tables=new ArrayList<String>();
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if(name.equalsIgnoreCase("table")){
            tables.add(sb.toString());
            sb.setLength(0);
        }
    }

}
