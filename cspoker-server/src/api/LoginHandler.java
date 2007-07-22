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

import game.chips.IllegalValueException;
import game.player.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class LoginHandler {

    public static int DEFAULT_NB_CHIPS = 100;

    private static List<Player> players = new ArrayList<Player>(8);

    private static Random random = new Random();

    public LoginHandler() {
	super();
    }

    /**
         * Log in to this game server.
         * 
         * @param username
         *                The name of the new user.
         * @return The user id of the new user.
         * @throws IllegalValueException
         */

    private Object playerNameAndIdLock = new Object();

    public long login(String username) {
	if (username == null || !username.matches("[A-Za-z][A-Za-z0-9]*")) {
	    System.out.println("Login denied.");
	    throw new IllegalArgumentException(
		    "You must provide a correct username.");
	}
	long id;

	synchronized (playerNameAndIdLock) {
	    if (getPlayer(username) != null) {
		System.out.println("Login denied.");
		throw new IllegalArgumentException(
			"The username is already in use.");
	    }
	    do {
		id = random.nextLong();
	    } while (getPlayer(id) != null);

	    try {
		players.add(new Player(id, username, DEFAULT_NB_CHIPS));
	    } catch (IllegalValueException e) {
		e.printStackTrace();
	    }
	}
	System.out.println(username + " logged in");
	return id;

    }

    public void handle(InputStream in, OutputStream out) throws IOException {
	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    LoginContentHandler handler = new LoginContentHandler(this, out);
	    xr.setContentHandler(handler);
	    xr.setErrorHandler(handler);
	    xr.parse(new InputSource(in));
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private Player getPlayer(String username) {
	Iterator<Player> iter = players.iterator();
	while (iter.hasNext()) {
	    Player player = iter.next();
	    if (player.getName().equalsIgnoreCase(username))
		return player;
	}
	return null;
    }

    protected Player getPlayer(long id) {
	Iterator<Player> iter = players.iterator();
	while (iter.hasNext()) {
	    Player player = iter.next();
	    if (player.getId() == id)
		return player;
	}
	return null;
    }

}
