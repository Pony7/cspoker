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
package java.org.cspoker.client;

import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.org.cspoker.client.commands.CommandExecutor;
import java.org.cspoker.client.commands.GameEventsCommand;
import java.org.cspoker.client.commands.HelpCommand;
import java.org.cspoker.client.request.AllInRequest;
import java.org.cspoker.client.request.BetRequest;
import java.org.cspoker.client.request.CallRequest;
import java.org.cspoker.client.request.CheckRequest;
import java.org.cspoker.client.request.CreateTableRequest;
import java.org.cspoker.client.request.DealRequest;
import java.org.cspoker.client.request.FoldRequest;
import java.org.cspoker.client.request.JoinTableRequest;
import java.org.cspoker.client.request.LeaveTableRequest;
import java.org.cspoker.client.request.ListTablesRequest;
import java.org.cspoker.client.request.PingRequest;
import java.org.cspoker.client.request.RaiseRequest;
import java.org.cspoker.client.request.StartGameRequest;
import java.util.HashMap;



/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

    private HashMap<String, CommandExecutor> commands = new HashMap<String,CommandExecutor>();
    
    public Client(String serverIP, int port, final String user, final String pass) throws IOException {
	Authenticator.setDefault(new Authenticator() {
	    @Override
		protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication (user, pass.toCharArray());
	    }
	});
	String address = "http://"+serverIP + ":" + port;
	
	registerCommands(address);
    }

    private void registerCommands(String address) throws MalformedURLException {
	commands.put("PING", new PingRequest(address));
	commands.put("LISTTABLES", new ListTablesRequest(address));
	commands.put("CREATETABLE", new CreateTableRequest(address));
	commands.put("JOINTABLE", new JoinTableRequest(address));
	commands.put("LEAVETABLE", new LeaveTableRequest(address));
	
	commands.put("STARTGAME", new StartGameRequest(address));
	commands.put("DEAL", new DealRequest(address));
	commands.put("CALL", new CallRequest(address));
	commands.put("BET", new BetRequest(address));
	commands.put("CHECK", new CheckRequest(address));
	commands.put("FOLD", new FoldRequest(address));
	commands.put("RAISE", new RaiseRequest(address));
	commands.put("ALLIN", new AllInRequest(address));
	
	commands.put("GAMEEVENTS", new GameEventsCommand(address));
	
	
	HelpCommand help = new HelpCommand();
	commands.put("HELP", help);
    }
    
    private CommandExecutor getCommand(String name){
	return commands.get(name.toUpperCase());
    }
    
    public String execute(String command, String... args) throws Exception{
	CommandExecutor c=getCommand(command);
	if(c==null)
	    throw new IllegalArgumentException("Not a valid command.");
	return c.execute(args);
    }

}
