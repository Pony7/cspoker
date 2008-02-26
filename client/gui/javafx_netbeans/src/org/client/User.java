package org.client;
/**
 * A user of this game
 * @author Cedric
 *
 */
public class User {

	/**
	 * The name of this user
	 */
	private final String userName;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new User with the given name and user id
	 */
	public User(String name){
		this.userName=name;
	}
	/**********************************************************
	 * Methods
	 **********************************************************/
	/**
	 * Returns the name of this user
	 */
	public String getUserName(){
		return userName;
	}
}
