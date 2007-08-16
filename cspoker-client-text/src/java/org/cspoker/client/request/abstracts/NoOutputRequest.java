package java.org.cspoker.client.request.abstracts;


import java.net.MalformedURLException;

import javax.xml.transform.sax.TransformerHandler;

public abstract class NoOutputRequest extends HttpRequest {

    public NoOutputRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) {
	// no op
    }

    @Override
    protected boolean isDoOutput() {
	return false;
    }

    protected String getRequestMethod() {
	return "GET";
    }
}
