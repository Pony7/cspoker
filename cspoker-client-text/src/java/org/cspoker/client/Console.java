/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cspoker.client.exceptions.FailedAuthenticationException;
import org.cspoker.client.exceptions.StackTraceWrapper;

public class Console {

    public static void main(String[] args) throws Exception {
	new Console(args);
    }
    
    private boolean verbose=false;
    
    /**
     * @param args
     * @throws Exception
     */
    public Console(String[] args) throws Exception {
	if (args.length != 2 && args.length != 3) {
	    System.out.println("usage: java -jar cspoker-client-text.jar [server] [portnumber] -[options]");
	    System.out.println("options:");
	    System.out.println(" -v verbose");
	    System.exit(0);
	}
	if(args.length==3){
	    if(args[2].contains("v")){
		verbose=true;
	    }
	}
	
	Client client;
	BufferedReader in = new BufferedReader(new InputStreamReader(
		System.in));
	do {

	    System.out.println("Enter username:");
	    System.out.print(">");
	    String username = in.readLine();
	    System.out.println("Enter password:");
	    System.out.print(">");
	    String password = in.readLine();
	    client=new Client(args[0], Integer.parseInt(args[1]), username, password);
	} while (!canPing(client));

	System.out.println("     __________________");
	System.out.println("    /Welcome to CSPoker\\");
	System.out.println("   /____________________\\");
	System.out.println("");
	System.out.println("Enter HELP for a list of supported commands.");
	System.out.println("");

	boolean running=true;

	while(running){
	    System.out.print(">");
	    String line = in.readLine();
	    if(line.equalsIgnoreCase("QUIT")||line.equalsIgnoreCase("EXIT")){
		System.out.println("Shutting down...");
		running=false;
	    } else{
		try {
		    System.out.println(parse(client,line));
		} catch (Exception e) {
		    handle(e);
		}
	    }
	}
    }



    private void handle(Exception e) {
	System.out.println("ERROR: "+e.getMessage());
	System.out.println("");
	if (verbose) {
	    System.out.println("-----details-----");
	    if (e instanceof StackTraceWrapper) {
		System.out.println(((StackTraceWrapper) e)
			.getStackTraceString());
	    } else {
		e.printStackTrace(System.out);
	    }
	    System.out.println("-----------------");
	    System.out.println("");
	}
    }



    private String parse(Client client, String line) throws Exception {
	String[] words=line.split(" ");
	if(words.length<1 || words[0].equalsIgnoreCase(""))
	    return parse(client, "GAMEEVENTS");
	List<String> list=new ArrayList<String>();
	list.addAll(Arrays.asList(words));
	String command=list.remove(0);
	return client.execute(command, list.toArray(new String[list.size()]));

    }



    private boolean canPing(Client client) {
	try {
	    client.execute("PING");
	} catch (FailedAuthenticationException e) {
	    System.out.println("Error: "+e.getLocalizedMessage());
	    System.out.println("");
	    return false;
	} catch (Exception e){
	    handle(e);
	    return false;
	}
	return true;
    }

}
