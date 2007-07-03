package game;

import game.player.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * This class contains all game elements,
 * such as the players, table,...
 * 
 * @author Kenzo
 *
 */
public class Game {
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	private GameProperty gameProperty;
	
	private Table table;
	
	private List<Player> players = new ArrayList<Player>();
	
	private Player currentPlayer;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public Game(GameProperty gameProperty){
		this.gameProperty = gameProperty;
		this.table = new Table();
	}
	
	/**********************************************************
	 * Getters
	 **********************************************************/
	
	public GameProperty GetGameProperty(){
		return gameProperty;
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	public List<Player> getPlayers(){
		return players;
	}

}
