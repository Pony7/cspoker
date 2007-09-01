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
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.util.HashMap;

import org.cspoker.client.commands.CardsCommand;
import org.cspoker.client.commands.CommandExecutor;
import org.cspoker.client.commands.HelpCommand;
import org.cspoker.client.commands.PotCommand;
import org.cspoker.client.request.AllInRequest;
import org.cspoker.client.request.BetRequest;
import org.cspoker.client.request.CallRequest;
import org.cspoker.client.request.CheckRequest;
import org.cspoker.client.request.CreateTableRequest;
import org.cspoker.client.request.DealRequest;
import org.cspoker.client.request.FoldRequest;
import org.cspoker.client.request.JoinTableRequest;
import org.cspoker.client.request.LeaveTableRequest;
import org.cspoker.client.request.ListTablesRequest;
import org.cspoker.client.request.PingRequest;
import org.cspoker.client.request.RaiseRequest;
import org.cspoker.client.request.StartGameRequest;
import org.cspoker.client.savedstate.Cards;
import org.cspoker.client.savedstate.Pot;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

    private HashMap<String, CommandExecutor> commands = new HashMap<String,CommandExecutor>();
    private Console console;
    private EventsThread eventsThread;
    
    public Client(String serverIP, int port, final String user, final String pass, Console console) throws IOException {
	Authenticator.setDefault(new Authenticator() {
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication (user, pass.toCharArray());
	    }
	});
	String address = "http://"+serverIP + ":" + port;
	this.console = console;
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
	
	Cards cards = new Cards();
	Pot pot = new Pot();
	CardsCommand cardsCommand = new CardsCommand(cards);
	commands.put("CARDS", cardsCommand);
	PotCommand potCommand = new PotCommand(pot);
	commands.put("POT", potCommand);
	
	eventsThread = new EventsThread(address, cards, pot);
	(new Thread(eventsThread)).start();
	
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
	String result = c.execute(args);
	if(c.requiresEventUpdate())
	    eventsThread.resetWait();
	return result;
    }

    public void close() {
	eventsThread.setRunning(false);	
    }

}
