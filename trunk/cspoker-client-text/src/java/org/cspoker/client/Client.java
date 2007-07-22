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
import java.net.URLConnection;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.client.xmlrpc.exceptions.LoginFailedException;
import org.cspoker.client.xmlrpc.exceptions.TableFullException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

    public static void main(String[] args) throws NumberFormatException,
	    IOException, TableFullException {
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
	    System.out.println(e.getMessage());

	}
	if (loggedin) {
	    System.out.println("What table do you want to join?:");
	    System.out.print(">");
	    String tableName = in.readLine();

	    String[] players = client.joinTable(tableName);
	    System.out.println("Joined table " + tableName);
	    System.out.println("The current players are:");
	    for (String name : players) {
		System.out.println(name);
	    }
	}

    }

    private URL url;

    private long id;

    private LoginHandler loginHandler;

    public Client(String server, int port) throws IOException {
	url = new URL("http://" + server + ":" + port + "/cspoker/login");
	loginHandler = new LoginHandler(url);
    }

    private void login(String username) throws IOException,
	    LoginFailedException {
	loginHandler.login(username);
    }

    private String[] joinTable(String tableName) throws IOException {
	URLConnection connection = url.openConnection();
	connection.setUseCaches(false);
	connection.setDoOutput(true);

	StreamResult requestResult = new StreamResult(connection
		.getOutputStream());
	SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
		.newInstance();
	TransformerHandler request;

	try {
	    request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    request.startDocument();
	    String comment = "CSPoker jointable request";
	    request.comment(comment.toCharArray(), 0, comment.length());
	    Attributes noattrs = new AttributesImpl();
	    request.startElement("", "jointable", "jointable", noattrs);
	    request.startElement("", "id", "id", noattrs);
	    request.characters(String.valueOf(id).toCharArray(), 0, String
		    .valueOf(id).length());
	    request.endElement("", "id", "id");
	    request.startElement("", "table", "table", noattrs);
	    request.characters(tableName.toCharArray(), 0, tableName.length());
	    request.endElement("", "table", "table");
	    request.endElement("", "jointable", "jointable");
	    request.endDocument();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	BufferedReader reader = new BufferedReader(new InputStreamReader(
		connection.getInputStream()));
	String line;
	while ((line = reader.readLine()) != null) {
	    System.out.println(line);
	}
	reader.close();

	return new String[0];
    }

}
