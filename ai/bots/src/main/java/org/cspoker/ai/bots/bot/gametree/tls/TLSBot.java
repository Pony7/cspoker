package org.cspoker.ai.bots.bot.gametree.tls;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.bot.AbstractBot;
import org.cspoker.ai.bots.bot.gametree.mcts.MCTSBot;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.Config;
import org.cspoker.ai.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;



public class TLSBot extends AbstractBot {
	
	private final static Logger logger = Logger.getLogger(MCTSBot.class);
	private final Config config;
	private final int decisionTime;
	

	public TLSBot(PlayerId botId, TableId tableId, SmartLobbyContext lobby,
			int buyIn, ExecutorService executor, BotListener[] botListeners, Config config, int decisionTime) {
		super(botId, tableId, lobby, buyIn, executor, botListeners);
		
		this.config = config;
		this.decisionTime = decisionTime;
		// TODO Auto-generated constructor stub
	}
	
	public Config getConfig(){
		return config;
	}

	@Override
	public void doNextAction() throws RemoteException, IllegalActionException {
		// TODO Auto-generated method stub
		
	}

}
