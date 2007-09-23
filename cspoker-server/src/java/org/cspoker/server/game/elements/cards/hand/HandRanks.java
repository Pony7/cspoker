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
import org.cspoker.server.game.elements.cards.hand.Hand.HandInfo;

/**
 * @author Craig Motlin
 */
public final class HandRanks {
    private static Logger logger = Logger.getLogger(HandRanks.class);

    private static HandRanks instance = new HandRanks();

    private final Map<HandInfo, Integer> rankMap = new HashMap<HandInfo, Integer>();

    private final Map<HandInfo, String> shortDescriptionMap = new HashMap<HandInfo, String>();

    private final Map<HandInfo, String> longDescriptionMap = new HashMap<HandInfo, String>();

    private HandRanks() {
	this.loadHandRanks();
    }

    public static HandRanks getInstance() {
	return HandRanks.instance;
    }

    public Integer getHandRank(final Hand hand) {
	return this.rankMap.get(hand.getHandInfo());
    }

    public String getShortDescription(final Hand hand) {
	return this.shortDescriptionMap.get(hand.getHandInfo());
    }

    public String getLongDescription(final Hand hand) {
	return this.longDescriptionMap.get(hand.getHandInfo());
    }

    private void addHandRank(final HandInfo handInfo, final Integer rank,
	    final String shortDescription, final String longDescription) {
	this.rankMap.put(handInfo, rank);
	this.shortDescriptionMap.put(handInfo, shortDescription);
	this.longDescriptionMap.put(handInfo, longDescription);
    }

    private void loadHandRanks() {
	final InputStream in = this
		.getClass()
		.getClassLoader()
		.getResourceAsStream(
			"org/cspoker/server/game/elements/cards/hand/handRanks.txt");
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

		final HandInfo handInfo = new HandInfo(product, flush);

		this.addHandRank(handInfo, Integer.valueOf(rank),
			shortDescription, longDescription);
	    }

	    bufferedReader.close();
	} catch (final FileNotFoundException e) {
	    HandRanks.logger.error(e.getLocalizedMessage(), e);
	} catch (final IOException e) {
	    HandRanks.logger.error(e.getLocalizedMessage(), e);
	}
    }
}
