package game.odds;

import game.Game;
import game.GameControl;
import game.player.Player;

/**
 * Class for calculating and estimating the odds involved for a certain
 * hand in a game
 * @author Cedric
 *
 */
public class OddsCalculator {

	/**********************************************************
	 * Constructor
	 **********************************************************/
	public OddsCalculator(GameControl gameControl,Player owner){
		if(gameControl==null || owner==null)
			throw new IllegalArgumentException();
		this.gameControl=gameControl;
		this.owner=owner;
	}
	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The game for which the odds are calculated
	 */
	private final GameControl gameControl;
	/**
	 * The owner of this oddsCalculator
	 */
	private final Player owner;
	/**********************************************************
	 * Methods
	 **********************************************************/
	/**********************************************************
	 * Auxiliary Methods
	 **********************************************************/
	/**
	 * Returns the current pot odds; being the amount of chips to continue competing
	 * in the current game and the amount of chips you get if you win the game
	 */
	public double getPotOdds(){
		return 0;
	}
	
}
