package java.org.cspoker.client.request.abstracts;


import java.net.MalformedURLException;

public abstract class OutputRequest extends HttpRequest {

    public OutputRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected boolean isDoOutput() {
	return true;
    }

    protected String getRequestMethod() {
	return "POST";
    }
}
