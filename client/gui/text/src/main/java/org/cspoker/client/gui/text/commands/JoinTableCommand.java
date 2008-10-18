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
package org.cspoker.client.gui.text.commands;

import org.cspoker.client.gui.text.Client;
import org.cspoker.client.gui.text.Console;
import org.cspoker.client.gui.text.eventlistener.PrintListener;
import org.cspoker.common.api.chat.listener.UniversalTableChatListener;
import org.cspoker.common.api.lobby.holdemtable.listener.UniversalTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public class JoinTableCommand extends RemoteCommand {
	

	public JoinTableCommand(Client client, Console console) {
		super(client, console);
	}

	public void execute(String... args) throws Exception {
		if(client.getCurrentTableContext()!= null){
			throw new IllegalActionException("Joining multiple tables isn't supported by this client.");
		}
		long tableID = Long.parseLong(args[0]);
		client.setCurrentTableContext(client.getLobbyContext().joinHoldemTable(tableID, 
				new UniversalTableListener(new PrintListener(console),tableID)
		));
		client.setCurrentTableID(tableID);
		client.setCurrentTableChatContext(client.getServerContext().getTableChatContext(new UniversalTableChatListener(new PrintListener(console), client.getCurrentTableID()), client.getCurrentTableID()));
	}

}
