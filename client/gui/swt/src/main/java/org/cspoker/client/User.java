package org.cspoker.client;

import org.cspoker.common.elements.player.Player;

/**
 * A user of this game. Immutable.
 * 
 * @author Cedric, Stephan
 */
public class User {
	
	/**
	 * The name of this user
	 */
	private final String userName;
	private final String password;
	
	/**
	 * The player represented by this user TODO Changed with the new API. Set
	 * this accordingly
	 */
	private Player player;
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	/**
	 * Creates a new User with the given name and user id
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
	 * Returns the name of this user
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return the passWord
	 */
	public String getPassword() {
		return password;
	}
	
}
