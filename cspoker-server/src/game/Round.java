package game;

/**
 * All the rounds a game can be in.
 * 
 * @author Kenzo
 *
 */
public enum Round {
	
	PREFLOP_ROUND{
		public Round getNextRound(){
			return FLOP_ROUND;
		}
	}, 
	
	
	FLOP_ROUND{
		public Round getNextRound(){
			return TURN_ROUND;
		}
	}, 
	
	
	TURN_ROUND{
		public Round getNextRound(){
			return FINAL_ROUND;
		}
		
	}, 
	
	
	FINAL_ROUND{
		public Round getNextRound(){
			return null;
		}
		
	};
	
	public abstract Round getNextRound();
	}
