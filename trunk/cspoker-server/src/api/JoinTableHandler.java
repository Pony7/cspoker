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
package api;

import game.GameProperty;
import game.PlayerListFullException;
import game.Table;
import game.player.Player;

import java.util.HashMap;
import java.util.Map;

public class JoinTableHandler {

    private Map<String, Table> tables = new HashMap<String, Table>();

    private LoginHandler loginHandler;

    public JoinTableHandler(LoginHandler loginHandler) {
	this.loginHandler = loginHandler;
	tables.put("default", new Table(new GameProperty()));
    }

    public void joinTable(long id, String tablename)
	    throws PlayerListFullException {

	Player player = loginHandler.getPlayer(id);
	if (player == null)
	    throw new IllegalStateException("You must be logged in.");

	Table table = tables.get(tablename);
	if (table == null)
	    throw new IllegalArgumentException("There is no table called "
		    + tablename);

	synchronized (table) {
	    table.addPlayer(player);
	}

    }

}
