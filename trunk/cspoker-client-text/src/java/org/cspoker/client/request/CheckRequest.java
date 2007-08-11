package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class CheckRequest extends NoOutputRequest{

    public CheckRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/game/check/";
    }

    @Override
    protected String getResult() {
	return "Checked."+n;
    }

}
