package org.cspoker.client.request;

import java.net.MalformedURLException;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.HttpPutRequest;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class CreateTableRequest extends HttpPutRequest{

    public CreateTableRequest(String address) throws MalformedURLException {
	super(address);
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
	return "Table created."+n;
    }

}
