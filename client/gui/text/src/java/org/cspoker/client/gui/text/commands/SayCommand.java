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

public class SayCommand extends AbstractCommand {

	public SayCommand(RemotePlayerCommunication rpc, Console console) {
		super(rpc, console);
	}

	public void execute(String... args) throws Exception {
		rpc.say(toString(args));
	}

	private String toString(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String a : args) {
			sb.append(a);
			sb.append(" ");
		}
		return sb.toString();
	}

}
