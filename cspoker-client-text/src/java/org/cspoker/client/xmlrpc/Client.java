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
package org.cspoker.client.xmlrpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.cspoker.client.xmlrpc.exceptions.TableFullException;


/**
 * Connect to the given server and passes on user commands.
 */
public class Client {
    
    public static void main(String[] args) throws NumberFormatException, XmlRpcException, IOException {
	if(args.length!=2){
	    System.out.println("usage: java -jar cspoker-client-text.jar [server] [portnumber]");
	    System.exit(0);
	}
	Client client = new Client(args[0], Integer.parseInt(args[1]));
	
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
	System.out.println("Enter username:");
	System.out.print(">");
	String username = in.readLine();
	 
	client.login(username);
	
	System.out.println("What table do you want to join?:");
	System.out.print(">");
	String tableName = in.readLine();
	 
	try {
	    client.joinTable(tableName);
	    System.out.println("Joined table"+ tableName);
	} catch (TableFullException e) {
	    System.out.println("The table is full.");
	}

    }
    
    private XmlRpcClient client; 
    private long id;
    
    public Client(String server, int port) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://"+server+":"+port+"/xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setContentLengthOptional(true);
        config.setReplyTimeout(60 * 1000);
        client = new XmlRpcClient();

        // set configuration
        client.setConfig(config);
    }

    private void login(String username) throws XmlRpcException { 
	    Object[] params = new Object[] { username };
	    id = (Long) client.execute("CSPoker.login", params);
	    System.out.println("Recieved ID " + id);
	
    }
    
    private void joinTable(String tableName) throws TableFullException, XmlRpcException { 
	    Object[] params = new Object[] { id, tableName };
	    int code=(Integer) client.execute("CSPoker.joinTable", params);
	    if(code==1){
		throw new TableFullException(tableName);
	    }
	    
	 
    }

}
