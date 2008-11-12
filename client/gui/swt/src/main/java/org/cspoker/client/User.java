package org.cspoker.client;

import java.rmi.RemoteException;

import org.cspoker.common.api.account.context.RemoteAccountContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;

/**
 * A user of this game. Will hopefully be Immutable once API is complete.
 * 
 * @author Cedric, Stephan
 */
public class User {
	
	/** {@link RemoteAccountContext} retrieved from server after login */
	private RemoteAccountContext accountContext;
	
	/**
	 * Persistency support for user preferences
	 */
	public static final class Prefs {
		
		/**
		 * Key for deck style preferences. The value stored is the path to the
		 * file.
		 */
		public static final String CARDS = "CSPoker.Cards";
		
		/**
		 * Key for chip style preferences. The value stored is the path to the
		 * file.
		 */
		public static final String CHIPS = "CSPoker.Chips";
		
		/** Key for sound preference (sound on/off) */
		public static final String SOUND = "CS.Poker.Sound";
	}
	
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
	 * 
	 * @param name The screenname of the user, i.e. "Donk42"
	 * @param password The password associated with the screenname
	 */
	public User(String name, String password) {
		this.userName = name;
		this.password = password;
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
		return (accountContext != null);
	}
	
	/**
	 * @param accountContext
	 */
	public void setAccountContext(RemoteAccountContext accountContext) {
		this.accountContext = accountContext;
		
	}
	
	/**
	 * @return
	 * @throws IllegalStateException When there is no account context
	 */
	public PlayerId getPlayerId() {
		if (accountContext == null) {
			throw new IllegalStateException("No account context");
		}
		try {
			return accountContext.getPlayerID();
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		} catch (IllegalActionException e) {
			throw new IllegalStateException(e);
		}
	}
}
