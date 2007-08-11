package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class CallRequest extends NoOutputRequest{

    public CallRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/game/call/";
    }

    @Override
    protected String getResult() {
	return "Called."+n;
    }

}
