package org.cspoker.client.request;

import java.net.MalformedURLException;

import javax.xml.transform.sax.TransformerHandler;

public abstract class HttpGetRequest extends HttpRequest {

    public HttpGetRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected void doOutput(TransformerHandler request) {
	// no op
    }

    @Override
    protected boolean isDoOutput() {
	return false;
    }

}
