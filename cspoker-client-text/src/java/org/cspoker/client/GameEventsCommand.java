package org.cspoker.client;

import java.net.MalformedURLException;

import org.cspoker.client.request.GameEventsAckRequest;
import org.cspoker.client.request.GameEventsRequest;

public class GameEventsCommand implements CommandExecutor {

    private GameEventsRequest noack;
    private GameEventsAckRequest ack;

    public GameEventsCommand(String address) throws MalformedURLException{
	this.noack = new GameEventsRequest(address);
	this.ack = new GameEventsAckRequest(address);
    }
    
    @Override
    public String execute(String... args) throws Exception {
	if(args.length==0){
	    return noack.execute(args);
	}else{
	    return ack.execute(args);
	}
    }

}
