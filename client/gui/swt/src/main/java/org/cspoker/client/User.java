package org.cspoker.client;

import org.cspoker.common.elements.player.Player;

/**
 * A user of this game
 * 
 * @author Cedric
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
