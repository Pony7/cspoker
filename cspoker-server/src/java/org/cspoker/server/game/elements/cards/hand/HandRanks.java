/**
 * Copyright (C) 2007 Craig Motlin
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package org.cspoker.server.game.elements.cards.hand;

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
import org.cspoker.common.game.elements.cards.cardElements.Rank;


/**
 * @author Craig Motlin
 */
public final class HandRanks {
	private static Logger logger = Logger.getLogger(HandRanks.class);
	private static HandRanks instance = new HandRanks();
	private final Map<Integer, Integer> unsuitedRankMap = new HashMap<Integer, Integer>();
	private final Map<Integer, String> unsuitedShortDescriptionMap = new HashMap<Integer, String>();
	private final Map<Integer, String> unsuitedLongDescriptionMap = new HashMap<Integer, String>();

	private final Map<Integer, Integer> flushRankMap = new HashMap<Integer, Integer>();
	private final Map<Integer, String> flushShortDescriptionMap = new HashMap<Integer, String>();
	private final Map<Integer, String> flushLongDescriptionMap = new HashMap<Integer, String>();

	private HandRanks() {
		this.loadHandRanks();
	}

	public static HandRanks getInstance() {
		return HandRanks.instance;
	}

	public Integer getHandRank(final Integer product, final boolean flush) {
		if (flush) {
			return this.flushRankMap.get(product);
		}
		return this.unsuitedRankMap.get(product);
	}

	public String getLongDescription(final Integer product, final boolean flush) {
		if (flush) {
			return this.flushLongDescriptionMap.get(product);
		}
		return this.unsuitedLongDescriptionMap.get(product);
	}

	private void addHandRank(final Integer product, final Integer rank, final String shortDescription, final String longDescription,
			final boolean flush) {
		if (flush) {
			this.flushRankMap.put(product, rank);
			this.flushShortDescriptionMap.put(product, shortDescription);
			this.flushLongDescriptionMap.put(product, longDescription);
		} else {
			this.unsuitedRankMap.put(product, rank);
			this.unsuitedShortDescriptionMap.put(product, shortDescription);
			this.unsuitedLongDescriptionMap.put(product, longDescription);
		}
	}

	private void loadHandRanks() {
		final InputStream in = getClass().getClassLoader().getResourceAsStream("org/cspoker/server/game/elements/cards/hand/handRanks.txt");
		try {
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

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
				this.addHandRank(Integer.valueOf(product), Integer.valueOf(rank), shortDescription, longDescription, flush);
			}

			bufferedReader.close();
		} catch (final FileNotFoundException e) {
			HandRanks.logger.error(e.getLocalizedMessage(), e);
		} catch (final IOException e) {
			HandRanks.logger.error(e.getLocalizedMessage(), e);
		}
	}
}
