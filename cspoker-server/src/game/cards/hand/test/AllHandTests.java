package game.cards.hand.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllHandTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for game.cards.hand.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestHand.class);
		suite.addTestSuite(TestHandEvaluator.class);
		suite.addTestSuite(TestHandQuality.class);
		//$JUnit-END$
		return suite;
	}

}
