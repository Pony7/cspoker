package org.cspoker.client.user;

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
