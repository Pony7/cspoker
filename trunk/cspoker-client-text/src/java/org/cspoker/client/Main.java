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
