/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.common.elements.hand;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.oro.text.perl.Perl5Util;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.hand.Hand.UniqueHandHash;

/**
 * @author Craig Motlin
 */
public final class HandRanks {
	
	private static Logger logger = Logger.getLogger(HandRanks.class);

	private static HandRanks instance = new HandRanks();

	private final Map<UniqueHandHash, Integer> rankMap = new HashMap<UniqueHandHash, Integer>();

	private final Map<UniqueHandHash, String> shortDescriptionMap = new HashMap<UniqueHandHash, String>();

	private final Map<UniqueHandHash, String> longDescriptionMap = new HashMap<UniqueHandHash, String>();

	private HandRanks() {
		loadHandRanks();
	}

	public static HandRanks getInstance() {
		return HandRanks.instance;
	}

	public int getHandRank(final Hand hand) {
		return rankMap.get(hand.handHash);
	}

	public String getShortDescription(final Hand hand) {
		return shortDescriptionMap.get(hand.handHash);
	}

	public String getLongDescription(final Hand hand) {
		return longDescriptionMap.get(hand.handHash);
	}

	private void addHandRank(final UniqueHandHash handInfo, final int rank,
			final String shortDescription, final String longDescription) {
		rankMap.put(handInfo, rank);
		shortDescriptionMap.put(handInfo, shortDescription);
		longDescriptionMap.put(handInfo, longDescription);
	}

	private void loadHandRanks() {		
		final InputStream in = this
				.getClass()
				.getClassLoader()
				.getResourceAsStream(
						"org/cspoker/common/elements/hand/handRanks.txt");
		try {
			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				final Perl5Util util = new Perl5Util();
				final List<String> rankStrings = new ArrayList<String>();
				util.split(rankStrings, "/\\s*,\\s*/", line);

				int product = 1;
				// First 5 columns are card ranks
				for (int i = 0; i < 5; i++) {
					product *= Rank.valueOf(rankStrings.get(i)).getPrime();
				}

				final boolean flush = "true".equals(rankStrings.get(7));

				final int rank = Integer.parseInt(rankStrings.get(8).trim());
				final String shortDescription = rankStrings.get(5);
				final String longDescription = rankStrings.get(6);

				final UniqueHandHash handInfo = new UniqueHandHash(product, flush);

				addHandRank(handInfo, rank, shortDescription,
						longDescription);
			}

			bufferedReader.close();
		} catch (final FileNotFoundException e) {
			HandRanks.logger.error(e.getLocalizedMessage(), e);
		} catch (final IOException e) {
			HandRanks.logger.error(e.getLocalizedMessage(), e);
		}
	}
}
