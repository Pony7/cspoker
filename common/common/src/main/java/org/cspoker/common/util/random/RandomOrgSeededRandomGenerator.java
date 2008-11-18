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

package org.cspoker.common.util.random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * A random.org seeded random generator.
 * 
 * @author Kenzo
 * 
 */
public class RandomOrgSeededRandomGenerator implements RandomSource {
	private static Logger logger = Logger
			.getLogger(RandomOrgSeededRandomGenerator.class);

	private static RandomOrgSeededRandomGenerator instance = new RandomOrgSeededRandomGenerator();

	private Random random;

	/**
	 * Construct a new random.org seeded random generator.
	 * 
	 */
	private RandomOrgSeededRandomGenerator() {

		try {
			final URL url = new URL(
					"http://www.random.org/strings/?num=10&len=10&digits=on&unique=on&format=plain&rnd=new");
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			final StringBuilder stringBuilder = new StringBuilder();

			String line;
			while ((line = in.readLine()) != null) {
				stringBuilder.append(line);
			}

			in.close();

			final byte[] seed = stringBuilder.toString().getBytes();

			random = new SecureRandom(seed);
		} catch (final MalformedURLException e) {
			logger.info("Default secure random is used.");
			random = new SecureRandom();
		} catch (final IOException e) {
			logger.info("Default secure random is used.");
			random = new SecureRandom();
		}
	}

	public static RandomOrgSeededRandomGenerator getInstance() {
		return RandomOrgSeededRandomGenerator.instance;
	}

	/**
	 * Returns a random-object.
	 * 
	 * The default implementation uses the current time as seed.
	 * 
	 * @return A random-object.
	 */
	public Random getRandom() {
		return random;
	}
}
