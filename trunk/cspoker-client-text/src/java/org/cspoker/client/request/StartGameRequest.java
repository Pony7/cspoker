package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class StartGameRequest extends NoOutputRequest{

    public StartGameRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/game/start/";
    }

    @Override
    protected String getResult() {
	return "Started Game."+n;
    }

}
