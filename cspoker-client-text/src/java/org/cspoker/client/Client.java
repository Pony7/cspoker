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

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.cspoker.client.request.PingRequest;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

    PingRequest ping;
    
    public Client(String serverIP, int port, final String user, final String pass) throws IOException {
	Authenticator.setDefault(new Authenticator() {
	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication (user, pass.toCharArray());
	    }
	});
	String address = "http://"+serverIP + ":" + port;
	ping= new PingRequest(address);
    }

    public void ping() throws Exception{
	ping.send();
	
    }

}
