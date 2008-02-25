package org.cspoker.client.xml.http;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.cspoker.common.exceptions.IllegalActionException;

public class NoOpSubmitter implements Runnable {

	private final static Logger logger = Logger.getLogger(XmlHttpChannel.class);
	
	private final XmlHttpChannel c;

	public NoOpSubmitter(XmlHttpChannel c) {
		this.c = c;
	}
	
	@Override
	public void run() {
		try {
			c.send(XmlHttpChannel.noOpXml);
		} catch (RemoteException e) {
			logger.error(e);
		} 
	}

}
