package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class FoldRequest extends NoOutputRequest{

    public FoldRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/game/fold/";
    }

    @Override
    protected String getResult() {
	return "Folded."+n;
    }

}
