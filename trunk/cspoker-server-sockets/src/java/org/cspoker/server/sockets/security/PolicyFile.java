package org.cspoker.server.sockets.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class PolicyFile {

    private static Logger logger = Logger.getLogger(PolicyFile.class);
    public static final String POLICY;
    public final static String request ="<policy-file-request";
    
    static {
	StringBuilder sb = new StringBuilder();;
	try {
	    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("org/cspoker/server/sockets/security/crossdomain.xml");
	    BufferedReader br = new BufferedReader(new InputStreamReader(is));

	    String line = null;

	    while ((line = br.readLine()) != null) {
	        sb.append(line + "\n");
	    }

	    br.close();
	    logger.info("Loaded policy file.");
	} catch (IOException e) {
	    logger.error("Can't load policy file: "+e.getMessage(),e);
	    e.printStackTrace();
	    sb.setLength(0);
	    sb.append("<cross-domain-policy/>");
	}
	POLICY = sb.toString();
    }
    
}
