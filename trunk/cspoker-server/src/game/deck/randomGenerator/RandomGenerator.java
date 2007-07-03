package game.deck.randomGenerator;

/**
 * An interface for random generators for card games.
 * 
 * @author Kenzo
 *
 */
public interface RandomGenerator {
	
	/**
	 * Returns a random sequence between 0 and 51,
	 * consisting of 52 numbers of wich each
	 * number is unique.
	 * 
	 * This number can be used to get the card index.
	 * 
	 * @return	The array is effective.
	 * 			| result!=null
	 * @return	The length is exactly 52.
	 * 			| result.length == 52
	 * @return 	Each number is unique.
	 * 			| for each number1 and number2 in result:
	 * 			| if(result[index1]==number1 && result[index2]==number2
	 * 			|		&& index1!=index2)
	 * 			|	then number1!=number2
	 * @return 	Each number is between 0 and 51.
	 * 			| for each number in result:
	 * 			|	0<=number && number<=51
	 */
	public int[] getRandomSequence();

}
