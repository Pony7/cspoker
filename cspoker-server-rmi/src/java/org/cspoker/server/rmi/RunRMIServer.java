package org.cspoker.server.rmi;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.config.Log4JPropertiesLoader;

public class RunRMIServer {

    static{
	Log4JPropertiesLoader.load("org/cspoker/server/rmi/logging/log4j.properties");
    }
    
    private final static Logger logger = Logger.getLogger(RunRMIServer.class);
        
    public static void main(String[] args) throws NumberFormatException, IOException {
	if (args.length < 1) {
	    usage();
	}

	int port=0;
	try {
	    port=Integer.parseInt(args[0]);
	} catch (NumberFormatException e) {
	    usage();
	}

	RMIServer server = new RMIServer(new XmlFileAuthenticator(), port);
	server.start();
    }

    private static void usage() {
	logger.fatal("usage: java -jar cspoker-server-rmi.jar [portnumber]");
	System.exit(0);
    }
    
}
