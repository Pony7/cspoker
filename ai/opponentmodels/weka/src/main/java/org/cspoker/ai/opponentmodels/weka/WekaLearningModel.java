package org.cspoker.ai.opponentmodels.weka;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.ai.opponentmodels.listener.OpponentModelListener;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

/**
 * This OpponentModel delegates to a provided default {@link WekaModel} for its opponent-model.
 * In addition it observes the game and (configured by {@link WekaOptions}) replaces
 * the opponent-model for each villain after enough data has been collected.
 *
 */
public class WekaLearningModel implements OpponentModel {

	protected static final Logger logger = Logger.getLogger(WekaLearningModel.class);

	private PlayerTrackingVisitor permanentVisitor;
	private ActionTrackingVisitor actionTrackingVisitor;
	private final Deque<PlayerTrackingVisitor> visitors = new ArrayDeque<PlayerTrackingVisitor>();

	Map<PlayerId, WekaRegressionModel> opponentModels = new HashMap<PlayerId, WekaRegressionModel>();
	private final WekaRegressionModel defaultModel;
	private final WekaOptions config;
	
	private final PlayerId bot;
	
	private final OpponentModelListener[] listeners;
	
	public WekaLearningModel(PlayerId botId, WekaRegressionModel defaultModel, WekaOptions config, 
			OpponentModelListener... listeners) {
		this.permanentVisitor = new PlayerTrackingVisitor();
		this.visitors.add(permanentVisitor);
		this.defaultModel = defaultModel;
		this.config = config;
		this.bot = botId;
		this.listeners = listeners;
		for (int i = 0; i < listeners.length; i++)
			listeners[i].setOpponentModel(this);
		if (config.useOnlineLearning()) {
			this.actionTrackingVisitor = new ActionTrackingVisitor(bot);
		}
	}
	
	public WekaOptions getConfig() {
		return config;
	}
	
	// thse methods are used by KullbackLeiblerListener
	// TODO: better design (this is messy)
	public Map<PlayerId, WekaRegressionModel> getOpponentModels() {
		return opponentModels;
	}
	public WekaRegressionModel getDefaultModel() {
		return defaultModel;
	}
	public Propositionalizer getCurrentGamePropositionalizer() {
		return visitors.peek().getPropz();
	}
	// *************************************************
	
	@Override
	public void assumePermanently(GameState gameState) {
		// make sure we have created Models for all players
		Set<PlayerState> seatedPlayers = gameState.getAllSeatedPlayers();
		for (PlayerState playerState : seatedPlayers) {
			getWekaModel(playerState.getPlayerId());
		}
		permanentVisitor.readHistory(gameState);
		if (actionTrackingVisitor != null) {
			actionTrackingVisitor.readHistory(gameState);
		}
	}

	@Override
	public void assumeTemporarily(GameState gameState) {
		PlayerTrackingVisitor root = visitors.peek();
		PlayerTrackingVisitor clonedTopVisitor = root.clone();
		clonedTopVisitor.readHistory(gameState);
		visitors.push(clonedTopVisitor);
	}

	@Override
	public void forgetLastAssumption() {
		visitors.pop();
		// the permanentVisitor should never be popped
		if (visitors.isEmpty()) {
			throw new IllegalStateException("'forgetAssumption' was called more often than 'assumeTemporarily'");
		}
	}

	private WekaRegressionModel getWekaModel(PlayerId actor) {
		WekaRegressionModel model = opponentModels.get(actor);
		if (model == null) {
			model = new WekaRegressionModel(defaultModel);
			if (config.useOnlineLearning() && !actor.equals(bot)) {
				opponentModels.put(actor, model);
				actionTrackingVisitor.getPropz().addPlayer(actor, new ARFFPlayer(actor, model, config));
			}
		}
		return model;
	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities(GameState gameState, PlayerId actor) {
		for (int i = 0; i < listeners.length; i++)
			listeners[i].onGetCheckProbabilities(gameState, actor);
//		if (actor.equals(bot)) System.out.println("botAction - " + getWekaModel(actor).toString().length());
//		else System.out.println("opponentAction - " + getWekaModel(actor).toString().length());
		Pair<Double, Double> pair = getWekaModel(actor).getCheckBetProbabilities(actor, getCurrentGamePropositionalizer());
//		if (actor.equals(bot)) System.out.println("botAction - (" + pair.getLeft() + "," + pair.getRight() + ")");
//		else System.out.println("opponentAction - (" + pair.getLeft() + "," + pair.getRight() + ")");
		return pair;
	}

	@Override
	public Triple<Double, Double, Double> getFoldCallRaiseProbabilities(GameState gameState, PlayerId actor) {
		for (int i = 0; i < listeners.length; i++)
			listeners[i].onGetFoldCallRaiseProbabilities(gameState, actor);
//		if (actor.equals(bot)) System.out.println("botAction - " + getWekaModel(actor).toString().length());
//		else System.out.println("opponentAction" + getWekaModel(actor).toString().length());
		Triple<Double, Double, Double> triple = 
			getWekaModel(actor).getFoldCallRaiseProbabilities(actor, getCurrentGamePropositionalizer());
//		if (actor.equals(bot)) System.out.println("botAction - (" + triple.getLeft() + "," + triple.getMiddle() + "," + triple.getRight() + ")");
//		else System.out.println("opponentAction - (" + triple.getLeft() + "," + triple.getMiddle() + "," + triple.getRight() + ")");
		return triple;
	}

	@Override
	public double[] getShowdownProbabilities(GameState gameState, PlayerId actor) throws UnsupportedOperationException {
		for (int i = 0; i < listeners.length; i++)
			listeners[i].onGetShowdownProbilities(gameState, actor);
//		if (actor.equals(bot)) System.out.println("botAction - " + getWekaModel(actor).toString().length());
//		else System.out.println("opponentAction" + getWekaModel(actor).toString().length());
		double[] list = getWekaModel(actor).getShowdownProbabilities(actor, getCurrentGamePropositionalizer());
//		if (actor.equals(bot)) System.out.print("botAction - (");
//		else System.out.print("opponentAction - (");
//		for (int i = 0; i < list.length; i++) {
//			if (actor.equals(bot)) System.out.print(list[i] + ",");
//			else System.out.print(list[i] + ",");
//		}
//		if (actor.equals(bot)) System.out.println(")");
//		else System.out.println(")");
		return list;		
	}

}
