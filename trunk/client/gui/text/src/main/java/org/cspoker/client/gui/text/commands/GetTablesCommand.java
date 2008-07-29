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

import org.cspoker.client.gui.text.Console;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.table.Table;

public class GetTablesCommand extends AbstractCommand {

	public GetTablesCommand(RemotePlayerCommunication rpc, Console console) {
		super(rpc, console);
	}

	public void execute(String... args) throws Exception {
		String s = "Tables:" + n;
		for (Table t : rpc.getTables().getTables()) {
			s += " - [" + t.getId().getId() + "]" + t.getName() + " ("
					+ t.getNbPlayers() + "/"
					+ t.getGameProperty().getMaxNbPlayers() + " players) ("
					+ t.getGameProperty().getSmallBlind() + "/"
					+ t.getGameProperty().getBigBlind() + " blinds)" + n;
		}
		console.print(s);
	}

}
