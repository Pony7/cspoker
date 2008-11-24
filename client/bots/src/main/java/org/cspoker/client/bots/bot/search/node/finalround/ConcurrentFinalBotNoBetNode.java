package org.cspoker.client.bots.bot.search.node.finalround;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.SimulatedBotAction;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class ConcurrentFinalBotNoBetNode extends FinalBotNoBetNode {

	private final static Logger logger = Logger.getLogger(ConcurrentFinalBotNoBetNode.class);

	private final ExecutorService executor;
	private final Queue<Future<?>> futures = new LinkedList<Future<?>>();
	
	public ConcurrentFinalBotNoBetNode(ExecutorService executor, PlayerId botId, GameState gameState,
			AllPlayersModel opponentModeler, int depth) {
		super(botId, gameState, opponentModeler, depth);
		this.executor = executor;
	}
	
	@Override
	public void expand() {
		super.expand();
		Future<?> future;
		while((future=futures.poll())!=null){
			try {
				future.get();
			} catch (InterruptedException e) {
				logger.fatal(e);
				throw new IllegalStateException(e);
			} catch (ExecutionException e) {
				logger.fatal(e);
				throw new IllegalStateException(e);
			}
		}
	}
	
	@Override
	public void expandAction(final SimulatedBotAction action) {
		Callable<Void> callable = new Callable<Void>(){
			@Override
			public Void call() {
				ConcurrentFinalBotNoBetNode.super.expandAction(action);
				return null;
			}
		};
		Future<Void> future = executor.submit(callable);
		futures.add(future);
	}
	
}
