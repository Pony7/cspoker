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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.cspoker.client.exceptions.FailedAuthenticationException;
import org.cspoker.client.exceptions.StackTraceWrapper;

/**
 * A console poker client.
 */
public class Console {
	private static Logger logger = Logger.getLogger(Console.class);

	public static void main(String[] args) throws Exception {
		new Console(args);
	}

	private boolean verbose = false;

	/**
	 * @param args
	 * @throws Exception
	 */
	public Console(String[] args) throws Exception {
		if (args.length != 2 && args.length != 3) {
			Console.logger.fatal("usage: java -jar cspoker-client-text.jar [server] [portnumber] -[options]");
			Console.logger.fatal("options:");
			Console.logger.fatal(" -v verbose");
			System.exit(0);
		}
		if (args.length == 3) {
			if (args[2].contains("v")) {
				verbose = true;
			}
		}

		boolean running = true;

		Client client = null;
		Scanner in = new Scanner(System.in);
		do {
			if (client != null) {
				client.close();
			}
			Console.logger.info("Enter username:");
			System.out.print(">");
			String username = in.nextLine();
			Console.logger.info("Enter password:");
			System.out.print(">");
			String password = in.nextLine();
			if (username.equalsIgnoreCase("QUIT") || username.equalsIgnoreCase("EXIT") || password.equalsIgnoreCase("QUIT")
					|| password.equalsIgnoreCase("EXIT")) {
				Console.logger.info("Shutting down...");
				running = false;
			} else {
				client = new Client(args[0], Integer.parseInt(args[1]), username, password, this);
			}
		} while (running && !canPing(client));

		if (running) {
			Console.logger.info("     ____________________________");
			Console.logger.info("    /Welcome to CSPoker 0.1 alpha\\");
			Console.logger.info("   /______________________________\\");
			Console.logger.info("");
			Console.logger.info("Enter HELP for a list of supported commands.");
			Console.logger.info("");
		}

		while (running) {
			System.out.print(">");
			String line = in.nextLine();
			if (line.equalsIgnoreCase("QUIT") || line.equalsIgnoreCase("EXIT")) {
				Console.logger.info("Shutting down...");
				running = false;
			} else {
				try {
					Console.logger.info(parse(client, line));
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
		Console.logger.error(e.getLocalizedMessage(), e);
		if (verbose) {

			Console.logger.info("-----details-----");
			if (e instanceof StackTraceWrapper) {
				Console.logger.info(((StackTraceWrapper) e).getStackTraceString());
			} else {
				e.printStackTrace(System.out);
			}
			Console.logger.info("-----------------");
			Console.logger.info("");
		}
	}

	private String parse(Client client, String line) throws Exception {
		String[] words = line.split(" ");
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(words));
		String command = list.remove(0);
		return client.execute(command, list.toArray(new String[list.size()]));

	}

	private boolean canPing(Client client) {
		try {
			client.execute("PING");
		} catch (FailedAuthenticationException e) {
			Console.logger.error(e.getLocalizedMessage(), e);
			return false;
		} catch (Exception e) {
			handle(e);
			return false;
		}
		return true;
	}

}
