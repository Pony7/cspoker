package org.cspoker.client.request;

import java.net.MalformedURLException;

import org.cspoker.client.request.abstracts.NoOutputRequest;

public class LeaveTableRequest extends NoOutputRequest{

    public LeaveTableRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getRequestMethod() {
        return "POST";
    }

    @Override
    protected String getPath() {
	return "/table/leave/";
    }

    @Override
    protected String getResult() {
	return "Left table."+n;
    }

}
