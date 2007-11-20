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
	logger.info("Starting RMI server");
	RMIServer server = new RMIServer(new XmlFileAuthenticator());
	server.start();
    }

    private static void usage() {
	logger.fatal("usage: java -jar cspoker-server-rmi.jar");
	System.exit(0);
    }
    
}
