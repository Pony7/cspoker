package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class AllInRequest extends NoOutputRequest{

    public AllInRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/game/allin/";
    }

    @Override
    protected String getResult() {
	return "Went All In."+n;
    }

}
