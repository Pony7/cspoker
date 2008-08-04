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
package org.cspoker.server.common.game.gamecontrol;

import org.cspoker.common.util.Log4JPropertiesLoader;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllGameControlTests {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/common/logging/log4j.properties");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.cspoker.server.common.game.gamecontrol");
		//$JUnit-BEGIN$
		suite.addTestSuite(JoinAndLeaveGameTest.class);
		suite.addTestSuite(BettingRulesTest.class);
		suite.addTestSuite(GameFlowTest.class);
		//$JUnit-END$
		return suite;
	}

}
