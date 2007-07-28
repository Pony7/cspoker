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
import java.net.URL;
import java.util.List;

import org.cspoker.client.exceptions.JoinTableFailedException;
import org.cspoker.client.exceptions.LoginFailedException;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

    public static void main(String[] args) throws NumberFormatException,
	    IOException {
	if (args.length != 2) {
	    System.out
		    .println("usage: java -jar cspoker-client-text.jar [server] [portnumber]");
	    System.exit(0);
	}
	Client client = new Client(args[0], Integer.parseInt(args[1]));

	boolean loggedin = false;

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Enter username:");
	System.out.print(">");
	String username = in.readLine();
	try {
	    client.login(username);
	    loggedin = true;
	} catch (LoginFailedException e) {
	    System.out.println("Login failed: ");
	    System.out.println("-"+e.getMessage());

	}
	if (loggedin) {
	    System.out.println("What table do you want to join?:");
	    System.out.print(">");
	    String tableName = in.readLine();

	    try {
		client.joinTable(tableName);
		List<String> players = client.getOtherPlayerNames();
		System.out.println("Joined table " + tableName);
		if (players.size() > 0)
		    System.out.println("The current players are:");
		for (String name : players) {
		    System.out.println(name);
		}
	    } catch (JoinTableFailedException e) {
		System.out.println("Login failed:");
		System.out.println("-"+e.getMessage());
	    }

	}

    }

    private String server;

    private LoginHandler loginHandler;

    private JoinTableHandler joinTableHandler;

    public Client(String url, int port) throws IOException {
	url = "http://" + url + ":" + port + "/cspoker/";
	loginHandler = new LoginHandler(url);
	joinTableHandler = new JoinTableHandler(url, loginHandler);
    }

    private void login(String username) throws IOException,
	    LoginFailedException {
	loginHandler.login(username);
    }

    private void joinTable(String tableName) throws IOException,
	    JoinTableFailedException {
	joinTableHandler.joinTable(tableName);
    }

    public List<String> getOtherPlayerNames() {
	return joinTableHandler.getOtherPlayerNames();
    }

}
