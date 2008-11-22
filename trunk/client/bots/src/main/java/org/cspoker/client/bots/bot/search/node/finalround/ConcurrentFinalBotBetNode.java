package org.cspoker.client.bots.bot.search.node.finalround;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.OpponentModel;
import org.cspoker.client.bots.bot.search.action.SimulatedBotAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class ConcurrentFinalBotBetNode extends FinalBotBetNode {

	private final static Logger logger = Logger.getLogger(ConcurrentFinalBotBetNode.class);

	private final ExecutorService executor;
	private final Queue<Future<?>> futures = new LinkedList<Future<?>>();
	
	public ConcurrentFinalBotBetNode(ExecutorService executor, PlayerId botId, GameState gameState,
			Map<PlayerId, OpponentModel> opponentModel, int depth) {
		super(botId, gameState, opponentModel, depth);
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
				ConcurrentFinalBotBetNode.super.expandAction(action);
				return null;
			}
		};
		Future<Void> future = executor.submit(callable);
		futures.add(future);
	}
	
}
