package org.cspoker.client.request;

import java.net.MalformedURLException;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.OutputRequest;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class BetRequest extends OutputRequest{

    public BetRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	if(args.length<1)
	    throw new IllegalArgumentException("Not enough arguments.");
	
	this.amount=args[0];
	request.startElement("", "amount", "amount", new AttributesImpl());
	String s=args[0];
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "amount", "amount");
    }

    @Override
    protected String getPath() {
	return "/game/bet/";
    }

    private String amount="";
    
    @Override
    protected String getResult() {
	return "Betted "+amount+"."+n;
    }

}
