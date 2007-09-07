package org.cspoker.server.sockets.runnables;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cspoker.server.sockets.ClientContext;
import org.cspoker.server.sockets.security.PolicyFile;
import org.cspoker.server.sockets.security.SocketsAuthenticator;
import org.cspoker.server.sockets.threading.Prioritizable;

public class ProcessXML implements Runnable, Prioritizable{

    private static Logger logger = Logger.getLogger(ProcessXML.class);
    public final static char DELIM = (char)0x00;

    private final String xml;
    private final ClientContext context;

    private final SocketsAuthenticator auth;

    public ProcessXML(String xml, ClientContext context, SocketsAuthenticator auth) {
	this.xml=xml;
	this.context = context;
	this.auth = auth;
    }

    public void run() {
	
	logger.trace("recieved xml:\n"+xml);
	
	//Flash client request for authorization to connect from a different host
	if(xml.startsWith(PolicyFile.request)){
	    logger.info("handling flash security manager request.");
	    context.send(PolicyFile.POLICY+DELIM);
	}
	
	//Check the credentials
	else if(!context.isAuthenticated()){
	    if(!auth.authenticate(context, xml)){
		try {
		    context.closeConnection();
		} catch (IOException e) {
		    logger.error("can't close socket: "+e.getMessage());
		}
	    }else{
		context.send(auth.POSITIVE_RESPONSE);
	    }
	}
	
	//Perform the other requests
	else{
	    context.send("recieved: "+xml);
	}
    }

    public int getPriority() {
	return 1;
    }

}
