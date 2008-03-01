package org.cspoker.client.gui.javafx;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginException;
import org.cspoker.client.gui.javafx.elements.TableInterface;
import org.cspoker.client.gui.javafx.elements.TableImpl;
import org.cspoker.client.rmi.RemotePlayerCommunicationFactoryForRMI;
import org.cspoker.client.xml.http.RemotePlayerCommunicationFactoryForHttp;
import org.cspoker.client.xml.sockets.RemotePlayerCommunicationFactoryForSocket;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.RemotePlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;

/**
 * The core of any client
 * @author Cedric
 *
 */
public class JavaFxClient {

    /**
     * The client of this client core
     */
    private User user;
    /**
     * The communication used by this client
     */
    private RemotePlayerCommunication rpc;

    /**********************************************************
     * Constructor
     **********************************************************/
    /**
     * Creates a new client core
     */
    public JavaFxClient() {
    }

    /**********************************************************
     * Communication
     **********************************************************/
    public void subscribeAllEvents(RemoteAllEventsListener listener) throws RemoteException {
        //communication.subscribeAllEventsListener(listener);
        System.out.println("Listener subscribed to all events");
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
           throw new IllegalArgumentException(ex.getMessage(),ex);
        } catch (LoginException ex) {
           throw new IllegalArgumentException(ex.getMessage(),ex);
        }
    }

    /**********************************************************
     * Login
     **********************************************************/
    /**
     * Logs in a new user with the given username and password to the given
     * server url and port.
     * @param url
     * 			the given server url
     * @param port
     * 			the given server port
     * @param userName
     * 			the given user name
     * @param password
     * 			the given password
     */
    public void login(String connection, String userName, String password) {
        this.user = new User(userName);
        createCommunication(connection, userName, password);
    }

    /**********************************************************
     * Bet
     * @throws IllegalActionException 
     * @throws RemoteException 
     **********************************************************/
    public void call() throws RemoteException, IllegalActionException {
        rpc.call();
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

    public void raise(int amount) throws RemoteException, IllegalActionException {
        rpc.raise(amount);
    }

    public void deal() throws RemoteException, IllegalActionException {
        rpc.deal();
    }

    public void allIn() throws RemoteException, IllegalActionException {
        rpc.allIn();
    }

    public void say(String message) throws RemoteException, IllegalActionException {
        rpc.say(message);
    }

    public void joinTable(int n) {
    //TODO: change when communication is fully implemented and tested
//		try {
//			communication.joinTable(id);
//		} catch (Exception e) {
//			gui.displayErrorMessage(e.getMessage());
//		}
    }

    public void leaveTable() throws RemoteException, IllegalActionException {
        rpc.leaveTable();
    }

    public TableId createTable() {
        //TODO: change when communication is fully implemented and tested
//		try {
//			return communication.createTable();
//		} catch (Exception e) {
//			gui.displayErrorMessage(e.getMessage());
//			return null;
//		}
        return new TableId(0);
    }

    public void startGame() throws RemoteException, IllegalActionException {
        rpc.startGame();
    }

    public TableInterface[] getTableList() {
        // TODO create method to ask the server for a list of table id's
        List<TableInterface> result = new ArrayList<TableInterface>();
        Random generator = new Random();
        int random = generator.nextInt(25);
        for (int j = 0; j < random; j++) {
            result.add(new TableImpl(j, 5, 2, 4));
        }
        TableInterface[] r = new TableInterface[result.size()];
        result.toArray(r);
        return r;
    }
}
