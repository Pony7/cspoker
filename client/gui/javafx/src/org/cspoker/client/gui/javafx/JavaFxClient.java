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
package org.cspoker.client.gui.javafx;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

import java.util.List;
import java.util.Set;
import javax.security.auth.login.LoginException;
import org.cspoker.client.rmi.RemotePlayerCommunicationFactoryForRMI;
import org.cspoker.client.xml.http.RemotePlayerCommunicationFactoryForHttp;
import org.cspoker.client.xml.sockets.RemotePlayerCommunicationFactoryForSocket;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.player.Player;
import org.cspoker.common.util.Log4JPropertiesLoader;

public class JavaFxClient {

    static {
        Log4JPropertiesLoader.load("org/cspoker/client/gui/javafx/logging/log4j.properties");
    }
    /**
     * The communication used by this client
     */
    private RemotePlayerCommunication rpc;
    private RemoteAllEventsListener listener;

    /**********************************************************
     * Constructor
     **********************************************************/
    /**
     * Creates a new client core
     */
    public JavaFxClient() {
    }

    public void login(String connection, String userName, String password) {
        createCommunication(connection, userName, password);
        System.out.println("Logged in as " + userName);
    }

    public void subscribeAllEvents(org.cspoker.common.eventlisteners.RemoteAllEventsListener listener) throws java.rmi.RemoteException {
        if (this.listener != null) {
            rpc.unsubscribeAllEventsListener(this.listener);
        }
        this.listener = listener;
        System.out.println("Listener subscribed to all events");
        rpc.subscribeAllEventsListener(listener);
    }

    /**
     * Creates a new communication with a server at the given url and port
     * for a user with the given user name and password
     */
    public void createCommunication(String connection, String username, String password) throws IllegalArgumentException {
        String protocol = "http";
        String server;
        int port = 8080;
        if (connection == null) {
            throw new IllegalArgumentException("The given connection address is not effective.");
        }
        String afterProt;
        if (!connection.contains("://")) {
            afterProt = connection;
        } else {
            String[] split = connection.split("://");
            if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
                throw new IllegalArgumentException("The given connection is not well formatted.");
            }
            afterProt = split[1];
            protocol = split[0];
        }
        if (!afterProt.contains(":")) {
            server = afterProt;
        } else {
            String[] split = afterProt.split(":");
            if (split.length != 2 || split[0].isEmpty() || split[1].isEmpty()) {
                throw new IllegalArgumentException("The given connection is not well formatted.");
            }
            server = split[0];
            try {
                port = Integer.parseInt(split[1]);
                if (port < 0 || port > 65535) {
                    throw new IllegalArgumentException("The given port number is out of range.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("The given port is not a valid port number.");
            }
        }
        try {
            if (protocol.equals("http")) {
                rpc = (new RemotePlayerCommunicationFactoryForHttp(server, port)).getRemotePlayerCommunication(username, password);
            } else if (protocol.equals("socket")) {
                rpc = (new RemotePlayerCommunicationFactoryForSocket(server, port)).getRemotePlayerCommunication(username, password);
            } else if (protocol.equals("rmi")) {
                rpc = (new RemotePlayerCommunicationFactoryForRMI(server, port)).getRemotePlayerCommunication(username, password);
            } else {
                throw new IllegalArgumentException("Unknown protocol: " + protocol);
            }
        } catch (ConnectException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        } catch (LoginException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    /**********************************************************
     * Bet
     * @throws IllegalActionException 
     * @throws RemoteException 
     **********************************************************/
    public void call() throws RemoteException, IllegalActionException {
        rpc.call();
    }

    public void bet(String amount) throws RemoteException, IllegalActionException {
        try {
            bet(Integer.parseInt(amount));
        } catch (NumberFormatException e) {
            throw new IllegalActionException("Not a valid number");
        }
    }
    public void bet(int amount) throws RemoteException, IllegalActionException {
        rpc.bet(amount);
    }

    public void fold() throws RemoteException, IllegalActionException {
        rpc.fold();
    }

    public void check() throws RemoteException, IllegalActionException {
        rpc.check();
    }

    public void raise(String amount) throws RemoteException, IllegalActionException {
        try {
            raise(Integer.parseInt(amount));
        } catch (NumberFormatException e) {
            throw new IllegalActionException("Not a valid number");
        }
    }
    public void raise(int amount) throws RemoteException, IllegalActionException {
        rpc.raise(amount);
    }

    public void allIn() throws RemoteException, IllegalActionException {
        rpc.allIn();
    }

    public void say(String message) throws RemoteException, IllegalActionException {
        rpc.say(message);
    }

    public Table joinTable(TableId id) throws IllegalActionException, RemoteException {
        return rpc.joinTable(id);
    }

    public Table getTable(TableId id) throws IllegalActionException, RemoteException {
        return rpc.getTable(id);
    }

    public void leaveTable() throws RemoteException, IllegalActionException {
        rpc.leaveTable();
    }

    public Table createTable(String name) throws RemoteException, IllegalActionException {
        return rpc.createTable(name);
    }

    public void startGame() throws RemoteException, IllegalActionException {
        rpc.startGame();
    }

    public Table[] getTableList() throws RemoteException {
        return rpc.getTables().getTables().toArray(new Table[rpc.getTables().getTables().size()]);
    }

    public static Card[] toArray(Set<Card> cards) {
        return cards.toArray(new Card[cards.size()]);
    }

    public static Player[] toArray(List<Player> players) {
        if(players==null)
            return new Player[]{};
        System.out.println("nbplayers in java is"+players.size());
        return players.toArray(new Player[players.size()]);
    }
}
