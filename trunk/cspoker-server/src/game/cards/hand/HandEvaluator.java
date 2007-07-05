package game.cards.hand;

public class HandEvaluator {

	/**
	    * Compares two hands against each other.
	    * 
	    * @param h1
	    *           The first hand
	    * @param h2
	    *           The second hand
	    * @return 1 = first hand is best, 2 = second hand is best, 0 = tie
	    */
	   public int compareHands(Hand h1, Hand h2) {
	      int r1 = rankHand(h1);
	      int r2 = rankHand(h2);

	      if (r1 > r2)
	         return 1;
	      if (r1 < r2)
	         return 2;
	      return 0;
	   }

	   /**
	    * Compares two 5-7 card hands against each other.
	    * 
	    * @param rank1
	    *           The rank of the first hand
	    * @param h2
	    *           The second hand
	    * @return 1 = first hand is best, 2 = second hand is best, 0 = tie
	    */
	   public int compareHands(int rank1, Hand h2) {
	      int r1 = rank1;
	      int r2 = rankHand(h2);

	      if (r1 > r2)
	         return 1;
	      if (r1 < r2)
	         return 2;
	      return 0;
	   }

	   /**
	    * Get the best 5 card poker hand from a 7 card hand
	    * 
	    * @param h
	    *           Any 7 card poker hand
	    * @return A Hand containing the highest ranked 5 card hand possible from the
	    *         input.
	    */
	   public Hand getBest5CardHand(Hand h) {
	      int[] ch = h.getCardArray();
	      int[] bh = new int[6];
	      int j = Find_Hand(ch, bh);
	      Hand nh = new Hand();
	      for (int i = 0; i < 5; i++)
	         nh.addCard(bh[i + 1]);
	      return nh;
	   }


}
