package org.cspoker.client;

import java.net.MalformedURLException;

import org.cspoker.client.request.GameEventsAckRequest;
import org.cspoker.client.request.contenthandler.EventsContentHandler;
import org.cspoker.client.savedstate.Cards;
import org.cspoker.client.savedstate.Pot;

public class EventsThread implements Runnable {

    private final EventsContentHandler handler;
    private final GameEventsAckRequest request;
    private final Console console;

    private boolean running;
    private volatile int waitSlots;
    private final int waitUnit;

    public EventsThread(String address, Cards cards, Pot pot, Console console) throws MalformedURLException {
	handler = new EventsContentHandler(cards, pot);
	request = new GameEventsAckRequest(address, handler);
	this.console = console;
	running = true;
	waitSlots=1;
	waitUnit = 150;
    }

    public void run() {
	while (running) {
	    try {
		String response = request.execute("");
		if(response != null){
		    String[] responses = response.split(request.n);
		    System.out.println("");
		    for(String line:responses){
			System.out.println(line);
		    }
		    System.out.print(">");
		    halfWait();
		}else{
		    doubleWait();
		}
	    } catch (Exception e1) {
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
	if(waitSlots<30)
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
