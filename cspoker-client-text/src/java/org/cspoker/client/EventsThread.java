package org.cspoker.client;

import java.net.MalformedURLException;
import java.util.List;

import org.cspoker.client.request.GameEventsAckRequest;
import org.cspoker.client.request.contenthandler.EventsContentHandler;
import org.cspoker.client.savedstate.Cards;
import org.cspoker.client.savedstate.Pot;

public class EventsThread implements Runnable {

    private final EventsContentHandler handler;
    private final GameEventsAckRequest request;

    private boolean running;
    private volatile int waitSlots;
    private final int waitUnit;

    public EventsThread(String address, Cards cards, Pot pot) throws MalformedURLException {
	handler = new EventsContentHandler(cards, pot);
	request = new GameEventsAckRequest(address, handler);
	running = true;
	waitSlots=1;
	waitUnit = 200;
    }

    public void run() {
	while (running) {
	    try {
		request.execute("");
		List<String> events = handler.getEvents();
		if(events.size()>0){
		    System.out.println("");
		    for(String line:events){
			System.out.println(line);
		    }
		    System.out.print(">");
		    halfWait();
		}else{
		    doubleWait();
		}
	    } catch (Exception e) {
		if(!e.getMessage().equals("Requesting the latest game events is not a valid action. You have not yet started the game."))
		    System.out.println("Error: "+e.getMessage());
		doubleWait();
	    }

	    int i=0;
	    while(i<waitSlots){
		try {
		    Thread.sleep(waitUnit);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		i++;
	    }
	}

    }

    public boolean isRunning() {
	return running;
    }

    public void setRunning(boolean running) {
	this.running = running;
    }

    public synchronized void doubleWait () {
	if(waitSlots<20)
	    waitSlots*=2;
    }

    public synchronized void halfWait () {
	if(waitSlots>=4)
	    waitSlots/=4;
    }

    public synchronized void resetWait(){
	waitSlots = 1;
    }

}
