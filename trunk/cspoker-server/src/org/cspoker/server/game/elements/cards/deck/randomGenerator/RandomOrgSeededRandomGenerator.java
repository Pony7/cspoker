/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.game.elements.cards.deck.randomGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * A random.org seeded random generator.
 * 
 * After a threshold number of times a random is returned,
 * a new random is used in stead, with a seed from random.org
 * 
 * @author Kenzo
 *
 */
public class RandomOrgSeededRandomGenerator extends RandomGenerator{
	
	private final static int THRESHOLD = 20; 
	
	/**
	 * An internal counter.
	 */
	private volatile int counter;
	
	/**
	 * Construct a new random.org seeded random generator.
	 *
	 */
	public RandomOrgSeededRandomGenerator(){
		super();
		counter = 0;
	}	
	
	/**
	 * Returns a random-object.
	 * 
	 * This implementation uses a seed obtained from random.org
	 * 
	 * After several random-objects have been requested,
	 * a fresh seed from random.org is used.
	 * 
	 * @return A random-object.
	 */
	@Override
	public Random getRandom() {
		if(counter>THRESHOLD){
			setNewRandom();
			counter = 0;
		}
		counter++;
		return random;
	}
	
	/**
	 * Returns a random long, obtained from random.org
	 * 
	 * The long is constructed as the multiplication of
	 * 2 integers, returned by the random.org
	 * 
	 * No overflow occurs, but not the whole range of long is covered.
	 * The result is between -10^18 and 10^18.
	 */
	@Override
	protected long getRandomSeed(){
		try {
	        URL url = new URL("http://www.random.org/integers/?num=2&min=-1000000000&max=1000000000&col=1&base=10&format=plain&rnd=new");
	    
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	        String str;
	        
	        int[] randomValues = new int[2];
	        
	        for (int i = 0; i < 2; i++) {
	        	str = in.readLine();
				randomValues[i] = Integer.parseInt(str);
			}
	        in.close();
	        return ((long)randomValues[0]) * randomValues[1];
	    } catch (MalformedURLException e) {
	    } catch (IOException e) {
	    }
	    System.out.println("Exception occured, default seed is used.");
	    return super.getRandomSeed();
	}
}
