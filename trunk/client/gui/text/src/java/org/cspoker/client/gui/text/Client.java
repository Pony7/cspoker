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
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.cspoker.client.common.RemotePlayerCommunicationFactory;
import org.cspoker.client.common.RemotePlayerCommunicationFactory.NoProviderException;
import org.cspoker.client.gui.text.commands.AllInCommand;
import org.cspoker.client.gui.text.commands.BetCommand;
import org.cspoker.client.gui.text.commands.CallCommand;
import org.cspoker.client.gui.text.commands.CardsCommand;
import org.cspoker.client.gui.text.commands.CheckCommand;
import org.cspoker.client.gui.text.commands.Command;
import org.cspoker.client.gui.text.commands.CreateTableCommand;
import org.cspoker.client.gui.text.commands.FoldCommand;
import org.cspoker.client.gui.text.commands.GetTableCommand;
import org.cspoker.client.gui.text.commands.GetTablesCommand;
import org.cspoker.client.gui.text.commands.HelpCommand;
import org.cspoker.client.gui.text.commands.JoinTableCommand;
import org.cspoker.client.gui.text.commands.LeaveTableCommand;
import org.cspoker.client.gui.text.commands.PotCommand;
import org.cspoker.client.gui.text.commands.RaiseCommand;
import org.cspoker.client.gui.text.commands.SayCommand;
import org.cspoker.client.gui.text.commands.StartGameCommand;
import org.cspoker.client.gui.text.eventlistener.StatefulConsoleListener;
import org.cspoker.client.gui.text.savedstate.Cards;
import org.cspoker.client.gui.text.savedstate.Pot;
import org.cspoker.common.RemotePlayerCommunication;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

	private HashMap<String, Command> commands = new HashMap<String, Command>();
	private Console console;
	private final RemotePlayerCommunication rpc;

	public Client(final String username,
			final String password, Console console,RemotePlayerCommunicationFactory factory) throws NoProviderException,
			RemoteException, LoginException {
		this.console = console;
		this.rpc = factory.getRemotePlayerCommunication(username, password);
		registerCommands();
	}

	private void registerCommands() throws RemoteException {
		commands.put("CREATETABLE", new CreateTableCommand(rpc, console));
		commands.put("JOINTABLE", new JoinTableCommand(rpc, console));
		commands.put("LEAVETABLE", new LeaveTableCommand(rpc, console));
		commands.put("GETTABLE", new GetTableCommand(rpc, console));
		commands.put("GETTABLES", new GetTablesCommand(rpc, console));

		commands.put("STARTGAME", new StartGameCommand(rpc, console));
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

		rpc.subscribeAllEventsListener(new StatefulConsoleListener(console,
				cards, pot));
	}

	private Command getCommand(String name) {
		return commands.get(name.toUpperCase());
	}

	public void execute(String command, String... args) throws Exception {
		Command c = getCommand(command);
		if (c == null) {
			throw new IllegalArgumentException("Not a valid command.");
		}
		c.execute(args);
	}

	public void close() {
		// no op
	}

}
