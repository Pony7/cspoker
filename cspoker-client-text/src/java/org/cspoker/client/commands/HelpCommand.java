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
package java.org.cspoker.client.commands;



public class HelpCommand implements CommandExecutor {

    @Override
    public String execute(String... args) throws Exception {
	
	return "Supported commands:"+n +
			"-General:"+n +
			"  HELP - what you're looking at now"+n +
			"  PING - ping the server"+n +
			"  QUIT - close the client"+n +
			"  EXIT - close the client"+n +
			"-Tables:"+n +
			"  CREATETABLE - create your table"+n +
			"  LISTTABLES - list all available tables"+n +
			"  JOINTABLE [id] - join the table with id [id]"+n +
			"-Game:"+n +
			"  STARTGAME - start a new game at your table"+n +
			"  GAMEEVENTS - get recent game events"+n +
			"  	      - just press enter to get all game events quickly"+n+
			"  GAMEEVENTS [ackID] - get recent game events and acknowledge all previous ones"+n +
			"  DEAL - deal"+n +
			"  CHECK - check"+n +
			"  BET [amount] - bet a certain amount"+n+
			"  CALL - call"+n +
			"  FOLD - fold"+n +
			"  RAISE [amount] - raise with a certain amount"+n+
			"  ALLIN - go all in"+n+
			"";
    }

}
