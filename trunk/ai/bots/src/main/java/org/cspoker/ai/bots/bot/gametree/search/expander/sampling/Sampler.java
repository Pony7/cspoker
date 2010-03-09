package org.cspoker.ai.bots.bot.gametree.search.expander.sampling;

import java.util.List;

import org.cspoker.ai.bots.bot.gametree.action.BetAction;
import org.cspoker.ai.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.ai.bots.bot.gametree.action.RaiseAction;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

import com.google.common.collect.ImmutableList;

public abstract class Sampler {

	public abstract ImmutableList<ProbabilityAction> getProbabilityActions(
			GameState gameState, OpponentModel model, PlayerId actor, PlayerId bot);

	
	protected void addRaiseProbalities(GameState gameState, PlayerId actor, 
			List<ProbabilityAction> actions, double raiseProbability, boolean raise,
			double[] relBetSizeSamples, double[] relPBetSizeSamples) {
		int lowerRaiseBound = gameState.getLowerRaiseBound(actor);
		int upperRaiseBound = gameState.getUpperRaiseBound(actor);
		if (lowerRaiseBound < upperRaiseBound) {
			int tmpAmount = 0;
			double tmpProbability = 0;
			for(int i = 0; i < relBetSizeSamples.length; i++) {
				double probability = raiseProbability * relPBetSizeSamples[i];
				double amount = lowerRaiseBound + 
					 (upperRaiseBound - lowerRaiseBound) * relBetSizeSamples[i];
				int smallBlind = gameState.getTableConfiguration().getSmallBlind();
				int amountInt = Math.min((int) (smallBlind * Math.round(amount/smallBlind)),
						upperRaiseBound);
				
				if (Math.abs(tmpAmount - amountInt) < (2*smallBlind)) {
					tmpProbability += probability;
				} else {
					tmpAmount = amountInt;
					tmpProbability += probability;
					if (amountInt < lowerRaiseBound)
						amountInt += smallBlind;
					if (amountInt > upperRaiseBound)
						amountInt = upperRaiseBound;
					if (raise){
						actions.add(new ProbabilityAction(new RaiseAction(gameState,
								actor, amountInt), tmpProbability));
					} else
						actions.add(new ProbabilityAction(new BetAction(gameState,
								actor, amountInt), tmpProbability));
					tmpProbability = 0.0;
				}
				
			}
		} else {
			if (raise)
				actions.add(new ProbabilityAction(new RaiseAction(gameState,
						actor, lowerRaiseBound), raiseProbability));
			else
				actions.add(new ProbabilityAction(new BetAction(gameState,
						actor, lowerRaiseBound), raiseProbability));
		}
	}
}
