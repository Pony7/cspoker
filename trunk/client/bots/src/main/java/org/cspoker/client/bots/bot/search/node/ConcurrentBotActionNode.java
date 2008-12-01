package org.cspoker.client.bots.bot.search.node;

import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class ConcurrentBotActionNode extends BotActionNode {

	private final static Logger logger = Logger.getLogger(ConcurrentBotActionNode.class);

	private final ExecutorService executor;
	
	public ConcurrentBotActionNode(ExecutorService executor, PlayerId botId, GameState gameState,
			AllPlayersModel opponentModeler, String prefix) {
		super(botId, gameState, opponentModeler, prefix);
		this.executor = executor;
	}
	
}
