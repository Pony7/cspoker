package org.cspoker.client;

import org.cspoker.common.elements.player.Player;

/**
 * A user of this game. Will hopefully be Immutable once API is complete.
 * 
 * @author Cedric, Stephan
 */
public class User {
	
	/** Flag indicating whether the user is currently logged in */
	private boolean loggedIn = false;
	/**
	 * The name of this user
	 */
	private final String userName;
	private final String password;
	
	private Player player;
	
	/**
	 * @return The player represented by this user
	 *         <p>
	 *         TODO Changed with the new API. Set this accordingly, preferably
	 *         as a final variable
	 */
	public Player getPlayer() {
		return player;
	}
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new User with the given name and user id
	 * 
	 * @param name The screenname of the user, i.e. "Donk42"
	 * @param password The password associated with the screenname
	 */
	public User(String name, String password) {
		this.userName = name;
		this.password = password;
		// TODO This is not good, do we get our player id from somewhere else??
		// It's nice to have this in the GameWindow
		this.player = new Player(0, name);
	}
	
	/***************************************************************************
	 * Methods
	 **************************************************************************/
	/**
	 * @return the name of this user
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @return <code>true</code> if the user has an active established server
	 *         connection, <code>false</code> otherwise
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	/**
	 * @param b The <i>logged in</i> - status
	 */
	public void setLoggedIn(boolean b) {
		assert (loggedIn == !b) : "Tried to set logged in to " + b + " but status was already like this";
		loggedIn = b;
		
	}
	
}
