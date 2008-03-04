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

public class HelpCommand implements Command {

	private Console console;

	public HelpCommand(Console console) {
		this.console = console;
	}

	public void execute(String... args) throws Exception {
		console.print("Supported commands:" + n + "" + n + "-General:" + n
				+ "  HELP - what you're looking at now" + n
				+ n + "  EXIT - close the client" + n + "" + n + "-Tables:" + n
				+ "  CREATETABLE [name] - create your table" + n
				+ "  GETTABLE [id] - see info on a table" + n
				+ "  JOINTABLE [id] - join the table with id [id]" + n
				+ "  LEAVETABLE - leave the table you're at" + n + "" + n
				+ "-Game:" + n + "  STARTGAME - start a new game at your table"
				+ n
				+ "  BET [amount] - bet a certain amount" + n + "  CALL - call"
				+ n + "  FOLD - fold" + n
				+ "  RAISE [amount] - raise with a certain amount" + n
				+ "  ALLIN - go all in" + n + "  CARDS - see all card" + n
				+ "  POT - see what's in the pot" + n + "");
	}
}
