package game;

import game.player.Player;

/**
 * This class is responsable to control the flow of the game.
 * This class can change the state of users and the table.
 * 
 * @author Kenzo
 *
 */
public class GameControl {
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	/**
	 * The last event player is the last player
	 * that has done significant change,
	 * such as a raise.
	 * 
	 * If the next player is the last event player,
	 * the betting round is over.
	 * 
	 * It is initialised in each game as the first better
	 * after the big blind.
	 */
	private Player lastEventPlayer;
	
	/**
	 * This variable contains all game elements,
	 * such as players and table.
	 */
	private Game game;
	
	private Round round;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public GameControl(){
		round = Round.PREFLOP_ROUND;
		
	}
	
	/**********************************************************
	 * Player methods
	 **********************************************************/
	
	public void check(Player player){
		
	}
	
	public void raise(Player player, int amount){
		lastEventPlayer = player;
	}
	
	public void deal(Player player, int amount){
		
	}
	
	private boolean roundEnded(){
		return lastEventPlayer == game.getCurrentPlayer();
	}
	
	private void endRound(){

	}

}
