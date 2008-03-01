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
package org.cspoker.client.gui.text;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.cspoker.client.allcommunication.LoadProvidersFromXml;
import org.cspoker.client.common.CommunicationProvider;
import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.client.common.RemotePlayerCommunicationFactory.NoProviderException;
import org.cspoker.client.rmi.RemotePlayerCommunicationFactoryForRMI;
import org.cspoker.client.xml.http.RemotePlayerCommunicationFactoryForHttp;
import org.cspoker.client.xml.sockets.RemotePlayerCommunicationFactoryForSocket;
import org.cspoker.common.util.Log4JPropertiesLoader;

/**
 * A console poker client.
 */
public class Console {
	
	static {
		Log4JPropertiesLoader
				.load("org/cspoker/client/gui/text/logging/log4j.properties");
	}
	
	public static void main(String[] args)  {
		new LoadProvidersFromXml(CommunicationProvider.global_provider);
		new Console(args);
	}

	private boolean verbose = false;

	private Client client = null;

	public Console(String[] args) {
		if (args.length != 0 && args.length != 1) {
			System.out
					.println("usage: java -jar cspoker-client-text.jar -[options]");
			System.out.println("options:");
			System.out.println(" -v verbose");
			System.exit(0);
		}
		if (args.length == 3) {
			if (args[2].contains("v")) {
				verbose = true;
			}
		}
		RemotePlayerCommunicationFactory factory = null;
		boolean running = true;
		boolean logedin = false;

		Scanner in = new Scanner(System.in);
		do {
			if (client != null) {
				client.close();
			}
			System.out.println("Select a server connection:");
			for(int i=0;i<CommunicationProvider.global_provider.getProviders().size();i++){
				System.out.println(" ("+(i+1)+") - "+CommunicationProvider.global_provider.getProviders().get(i));
			}
			System.out.println(" ("+(CommunicationProvider.global_provider.getProviders().size()+1)+") - Create a new server connection");
			System.out.print(">");
			int connection;
			try {
				connection = Integer.parseInt(in.nextLine());
				if(connection<1||connection>CommunicationProvider.global_provider.getProviders().size()+1)
					throw new NumberFormatException();
			} catch (NumberFormatException e) {
				connection=1;
			}
			System.out.println();
			
			if(connection==CommunicationProvider.global_provider.getProviders().size()+1){
				System.out.println("Select type:");
				System.out.println(" (1) - HTTP");
				System.out.println(" (2) - SOCKET");
				System.out.println(" (3) - RMI");
				System.out.print(">");
				int ctype;
				try {
					ctype = Integer.parseInt(in.nextLine());
					if(ctype<1||ctype>3)
						throw new NumberFormatException();
				} catch (NumberFormatException e) {
					ctype=1;
				}
				System.out.println();
				
				System.out.println("Enter address:");
				System.out.print(">");
				String address = in.nextLine();
				System.out.println();

				System.out.println("Enter port:");
				System.out.print(">");	
				int port;
				try {
					port = Integer.parseInt(in.nextLine());
				} catch (NumberFormatException e) {
					port=8080;
				}
				System.out.println();
				if(ctype==1)
					factory = new RemotePlayerCommunicationFactoryForHttp(address, port);
				else if(ctype==2)
					factory = new RemotePlayerCommunicationFactoryForSocket(address, port);
				else if(ctype==3)
					factory = new RemotePlayerCommunicationFactoryForRMI(address, port);
			}else{
				factory = CommunicationProvider.global_provider.getProviders().get(connection-1);
			}
			System.out.println("Enter username:");
			System.out.print(">");
			String username = in.nextLine();
			System.out.println();
			System.out.println("Enter password:");
			System.out.print(">");
			String password = in.nextLine();
			System.out.println();
			if (username.equalsIgnoreCase("QUIT")
					|| username.equalsIgnoreCase("EXIT")
					|| password.equalsIgnoreCase("QUIT")
					|| password.equalsIgnoreCase("EXIT")) {
				System.out.println("Shutting down...");
				running = false;
			} else {
				
					try {
						client = new Client(username, password,this,factory);
						logedin = true;
					} catch (RemoteException e) {
						System.out.println("Could not connect to "+factory);
						System.out.println();
						handle(e);
					} catch (LoginException e) {
						System.out.println("Login failed for "+username);
						System.out.println();
						handle(e);
					} catch (NoProviderException e) {
						System.out.println("Could not connect to "+factory);
						System.out.println();
						handle(e);
					}
				
			}
		} while (running && !logedin);

		if (running) {
			System.out.println("     ____________________________");
			System.out.println("    /Welcome to CSPoker 0.1 beta \\");
			System.out.println("   /______________________________\\");
			System.out.println("");
			System.out.println("Enter HELP for a list of supported commands.");
			System.out.println("");
		}

		while (running) {
			System.out.print(">");
			String line = in.nextLine();
			if (line.equalsIgnoreCase("QUIT") || line.equalsIgnoreCase("EXIT")) {
				System.out.println("Shutting down...");
				running = false;
			} else {
				try {
					parse(line);
				} catch (Exception e) {
					handle(e);
				}
			}
		}
		if (client != null) {
			client.close();
		}
	}

	private void handle(Exception e) {
		System.out.println("ERROR: "+e.getMessage());
		if (verbose) {
			System.out.println();
			System.out.println("-----details-----");
			e.printStackTrace(System.out);
			System.out.println("-----------------");
			System.out.println("");
		}
	}

	private void parse(String line) throws Exception {
		String[] words = line.split(" ");
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(words));
		String command = list.remove(0);
		client.execute(command, list.toArray(new String[list.size()]));

	}

	public void print(String result) {
		System.out.println(result);
		System.out.println(">");
	}

}
