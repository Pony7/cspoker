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

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.HashMap;

import org.cspoker.client.commands.AllInCommand;
import org.cspoker.client.commands.BetCommand;
import org.cspoker.client.commands.CallCommand;
import org.cspoker.client.commands.CardsCommand;
import org.cspoker.client.commands.CheckCommand;
import org.cspoker.client.commands.Command;
import org.cspoker.client.commands.CreateTableCommand;
import org.cspoker.client.commands.DealCommand;
import org.cspoker.client.commands.FoldCommand;
import org.cspoker.client.commands.HelpCommand;
import org.cspoker.client.commands.JoinTableCommand;
import org.cspoker.client.commands.LeaveTableCommand;
import org.cspoker.client.commands.PotCommand;
import org.cspoker.client.commands.RaiseCommand;
import org.cspoker.client.commands.SayCommand;
import org.cspoker.client.commands.StartGameCommand;
import org.cspoker.client.common.RemotePlayerCommunicationFactoryImpl;
import org.cspoker.client.common.RemotePlayerCommunicationFactory.NoProviderException;
import org.cspoker.client.eventlistener.StatefulConsoleListener;
import org.cspoker.client.savedstate.Cards;
import org.cspoker.client.savedstate.Pot;
import org.cspoker.common.game.RemotePlayerCommunication;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

    private HashMap<String, Command> commands = new HashMap<String,Command>();
    private Console console;
    private final RemotePlayerCommunication rpc;
    
    public Client(String server, int port, final String username
	    , final String password, Console console) throws NoProviderException, RemoteException {
	this.console = console;
	rpc = RemotePlayerCommunicationFactoryImpl.global_factory
		.getRemotePlayerCommunication(server, port, username, password);
	registerCommands();
    }

    private void registerCommands() throws RemoteException {
	commands.put("CREATETABLE", new CreateTableCommand(rpc, console));
	commands.put("JOINTABLE", new JoinTableCommand(rpc, console));
	commands.put("LEAVETABLE", new LeaveTableCommand(rpc, console));
	
	commands.put("STARTGAME", new StartGameCommand(rpc, console));
	commands.put("DEAL", new DealCommand(rpc, console));
	commands.put("CALL", new CallCommand(rpc, console));
	commands.put("BET", new BetCommand(rpc, console));
	commands.put("CHECK", new CheckCommand(rpc, console));
	commands.put("FOLD", new FoldCommand(rpc, console));
	commands.put("RAISE", new RaiseCommand(rpc, console));
	commands.put("ALLIN", new AllInCommand(rpc, console));
	
	commands.put("SAY", new SayCommand(rpc, console));
	
	Cards cards = new Cards();
	Pot pot = new Pot();
	CardsCommand cardsCommand = new CardsCommand(console, cards);
	commands.put("CARDS", cardsCommand);
	PotCommand potCommand = new PotCommand(console, pot);
	commands.put("POT", potCommand);
	
	HelpCommand help = new HelpCommand(console);
	commands.put("HELP", help);
	
	rpc.subscribeAllEventsListener(new StatefulConsoleListener(console,cards,pot));
    }
    
    private Command getCommand(String name){
	return commands.get(name.toUpperCase());
    }
    
    public void execute(String command, String... args) throws Exception{
	Command c=getCommand(command);
	if(c==null)
	    throw new IllegalArgumentException("Not a valid command.");
	c.execute(args);
    }

    public void close() {
	// no op
    }

}
