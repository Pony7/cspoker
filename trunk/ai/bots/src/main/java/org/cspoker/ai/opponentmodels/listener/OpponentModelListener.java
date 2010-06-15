package org.cspoker.ai.opponentmodels.listener;

import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public interface OpponentModelListener {

	public void onGetCheckProbabilities(GameState state, PlayerId actor);
	
	public void onGetFoldCallRaiseProbabilities(GameState state, PlayerId actor);
	
	public void onGetShowdownProbilities(GameState state, PlayerId actor);

	public void setOpponentModel(OpponentModel opponentModel);
}