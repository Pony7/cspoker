package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class DealRequest extends NoOutputRequest{

    public DealRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/game/deal/";
    }

    @Override
    protected String getResult() {
	return "Dealt."+n;
    }

}
