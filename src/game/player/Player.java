package game.player;

import game.cards.PrivateCards;
import game.chips.Chips;

/**
 * A class to represent players: bots or humans.
 * 
 * @author Kenzo
 *
 */
public class Player {
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	/**
	 * This id should be guarenteed unique upon creation
	 * of a player.
	 */
	private final long id;
	
	/**
	 * The name of the player.
	 */
	private final String name;
	
	/**
	 * The available chips of this player.
	 */
	private Chips chips;
	
	/**
	 * The hidden cards.
	 */
	private PrivateCards privateCards;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public Player(long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	
	/**
	 * State pattern welke acties de player mag doen als een state modelleren?
	 */
	
	public void fold(){
		
	}
	
	public long getId(){
		return 0;
	}
	
	public long getPrivateKey(){
		return 0;
	}

}
