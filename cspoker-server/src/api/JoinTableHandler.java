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

import game.PlayerId;
import game.TableId;
import game.elements.player.Player;
import game.elements.table.PlayerListFullException;
import game.elements.table.Table;
import game.gameControl.GameProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class JoinTableHandler {

    private final Map<String, Table> tables = new HashMap<String, Table>();

    private final LoginHandler loginHandler;

    public JoinTableHandler(LoginHandler loginHandler) {
	this.loginHandler = loginHandler;
	tables.put("default", new Table(new TableId(0),new GameProperty()));
    }

    public void joinTable(PlayerId id, String tablename)
	    throws PlayerListFullException {

	Player player = loginHandler.getPlayer(id);
	if (player == null){
	    System.out.println("Join table from bad player.");
	    throw new IllegalStateException("You must be logged in("+id+").");
	}
	Table table = tables.get(tablename);
	if (table == null){
	    System.out.println(player.getName() +" tried to join non existent table "+tablename);
	    throw new IllegalArgumentException("There is no table called "
		    + tablename);
	}
	synchronized (table) {
	    table.addPlayer(player);

	}
	System.out.println(player.getName()+" joined table "+tablename);

    }

    public List<String> getPlayersExceptFor(PlayerId id, String tablename){
	Player player = loginHandler.getPlayer(id);
	if (player == null)
	    throw new IllegalStateException("You must be logged in.");

	Table table = tables.get(tablename);
	if (table == null)
	    throw new IllegalArgumentException("There is no table called "
		    + tablename);

	List<Player> players = table.getPlayers();
	List<String> playernames = new ArrayList<String>(players.size()-1);

	for(Player p:players){
	    if(!p.equals(player)){
		playernames.add(p.getName());
	    }
	}

	return playernames;

    }

    public void handle(InputStream requestBody, OutputStream responseBody) throws IOException {
	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    JoinTableContentHandler handler = new JoinTableContentHandler(this, responseBody);
	    xr.setContentHandler(handler);
	    xr.setErrorHandler(handler);
	    xr.parse(new InputSource(requestBody));
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
