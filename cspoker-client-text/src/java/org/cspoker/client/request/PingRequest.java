package org.cspoker.client.request;

import java.net.MalformedURLException;

public class PingRequest extends HttpGetRequest{

    public PingRequest(String url) throws MalformedURLException {
	super(url);
    }

    @Override
    protected String getPath() {
	return "/ping/";
    }


}
