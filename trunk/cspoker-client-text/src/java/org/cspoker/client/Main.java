package org.cspoker.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.cspoker.client.exceptions.FailedAuthenticationException;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	if (args.length != 2) {
	    System.out.println("usage: java -jar cspoker-client-text.jar [server] [portnumber]");
	    System.exit(0);
	}
	Client client;
	
	do {
	    BufferedReader in = new BufferedReader(new InputStreamReader(
		    System.in));
	    System.out.println("Enter username:");
	    System.out.print(">");
	    String username = in.readLine();
	    System.out.println("Enter password:");
	    System.out.print(">");
	    String password = in.readLine();
	    client=new Client(args[0], Integer.parseInt(args[1]), username, password);
	} while (!canPing(client));

	
    }

    private static boolean canPing(Client client) throws Exception {
	try {
	    client.ping();
	} catch (FailedAuthenticationException e) {
	    System.out.println("Error: "+e.getLocalizedMessage());
	    return false;
	}
	return true;
    }

}
