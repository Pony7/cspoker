package org.cspoker.ai.opponentmodels.weka;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.DetailedHoldemTableState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.client.common.gamestate.modifiers.BetState;
import org.cspoker.client.common.gamestate.modifiers.BlindState;
import org.cspoker.client.common.gamestate.modifiers.CallState;
import org.cspoker.client.common.gamestate.modifiers.CheckState;
import org.cspoker.client.common.gamestate.modifiers.ConfigChangeState;
import org.cspoker.client.common.gamestate.modifiers.FoldState;
import org.cspoker.client.common.gamestate.modifiers.JoinTableState;
import org.cspoker.client.common.gamestate.modifiers.LeaveTableState;
import org.cspoker.client.common.gamestate.modifiers.NewCommunityCardsState;
import org.cspoker.client.common.gamestate.modifiers.NewDealState;
import org.cspoker.client.common.gamestate.modifiers.NewPocketCardsState;
import org.cspoker.client.common.gamestate.modifiers.NewRoundState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.client.common.gamestate.modifiers.RaiseState;
import org.cspoker.client.common.gamestate.modifiers.ShowHandState;
import org.cspoker.client.common.gamestate.modifiers.SitInState;
import org.cspoker.client.common.gamestate.modifiers.SitOutState;
import org.cspoker.client.common.gamestate.modifiers.WinnerState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.util.Util;

import org.cspoker.ai.bots.bot.gametree.action.BetAction;
import org.cspoker.ai.bots.bot.gametree.action.CallAction;
import org.cspoker.ai.bots.bot.gametree.action.CheckAction;
import org.cspoker.ai.bots.bot.gametree.action.FoldAction;
import org.cspoker.ai.bots.bot.gametree.action.RaiseAction;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.INode;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.InnerNode;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.ai.opponentmodels.weka.ARFFPropositionalizer;
import org.cspoker.ai.opponentmodels.weka.PlayerTrackingVisitor;

import com.google.common.collect.ImmutableList;


/**
 * The ActionTrackingVisitor currently is used to observe the game
 * and delegate important states to an {@link ARFFPropositionalizer}<br>
 */
public class ActionTrackingVisitor extends PlayerTrackingVisitor {

	private final static Logger logger = Logger.getLogger(ARFFPropositionalizer.class);
	
	double truePositive = 0.0;
	double trueNegative = 0.0;
	double falsePositive = 0.0;
	double falseNegative = 0.0;
	
	public ActionTrackingVisitor(OpponentModel opponentModel, PlayerId bot) {
		super(opponentModel);
		try {
			this.propz = new ARFFPropositionalizer(bot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ARFFPropositionalizer getPropz() {
		return (ARFFPropositionalizer) this.propz;
	}
	
	private InnerNode getNode(GameState state) {
		try {
			return (InnerNode) parentOpponentModel.getChosenNode();
		} catch (ClassCastException e) {
//			System.out.println("-------------\nNO CLASS CAST " + state.getLastEvent() + "\n-------------"); 
			// do nothing
			return null;
		}
	}
	
	public void printAccuracy() {
		System.out.println(" Accuracy : " + 
				(trueNegative + truePositive) / 
				(trueNegative + truePositive + falseNegative + falsePositive));
	}
	
	private Prediction getProbability(GameState gameState) {
		return getProbability(gameState, 0);
	}
	
	/**
	 * To calculate the accuracy of the opponentmodel we need the probabilities
	 * of considered actions of opponents when the {@link MCTSBot} calculates
	 * the best action for him to take. For this we use the {@link INode} that
	 * contains the action made by the bot. If it is an {@link InnerNode} the
	 * children contain the probabilities of actions the opponent could make.
	 * Probalities of raises are grouped to give a probability for the action
	 * Raise. To consider consecutive actions by opponents we adjust the
	 * {@link INode} to become the correct child, which is the actual action
	 * made by the opponent. In case of a raise, the raise amongst the children
	 * nearest to the actual one is chosen.
	 * The probabilities are returned as a {@link Prediction}.
	 * 
	 * @param gameState
	 *            the {@link GameState} for which we want to calculate the
	 *            probability
	 * @param raiseAmount
	 *            the bet/raise amount, otherwise 0
	 * @return a {@link Prediction} of the considered action.
	 * 
	 * <br>
	 * <br>
	 *         TODO: grouping probalities of raises could be improved.
	 */
	private Prediction getProbability(GameState gameState, double raiseAmount) {
		HashMap<Class<?>, Double> probs = new HashMap<Class<?>, Double>();
		Class<?> cProb = null;
		RaiseAction raiseAction = null;
		BetAction betAction = null;
		InnerNode node = getNode(gameState);
		if (node != null) {
//			System.out.println(">-----------------------------");
			ImmutableList<INode> children = node.getChildren();
			if (children != null) {
				for (INode n : children) {
					Class<?> c = n.getLastAction().getAction().getClass();
					// Same actions are grouped to make one probability (bet/raise)
					if (!probs.containsKey(c))
						probs.put(c, n.getLastAction().getProbability());
					else
						probs.put(c, n.getLastAction().getProbability() + probs.get(c));
	
					if (gameState.getClass().equals(
							n.getLastAction().getAction()
									.getUnwrappedStateAfterAction().getClass())) {
						cProb = c;
						if (raiseAction == null && c.equals(RaiseAction.class))
							raiseAction = (RaiseAction) n.getLastAction().getAction();
						else if (betAction == null && c.equals(BetAction.class))
							betAction = (BetAction) n.getLastAction().getAction(); 
						else if (!c.equals(RaiseAction.class) && !c.equals(BetAction.class))
							parentOpponentModel.setChosenNode(n);
					}
					
					// Correct child node is chosen for bet/raise
					if (cProb != null) {
						if (raiseAction != null && c.equals(RaiseAction.class)) {
							RaiseAction newRaiseAction = (RaiseAction) n.getLastAction().getAction();
							if (Math.abs(newRaiseAction.amount - raiseAmount) < 
									Math.abs(raiseAction.amount - raiseAmount)) {
								raiseAction = newRaiseAction;
								parentOpponentModel.setChosenNode(n);
							} 
						}
						else if (betAction != null && c.equals(BetAction.class)) {
							BetAction newBetAction = (BetAction) n.getLastAction().getAction();
							if (Math.abs(newBetAction.amount - raiseAmount) < 
									Math.abs(betAction.amount - raiseAmount)) {
								betAction = newBetAction;
								parentOpponentModel.setChosenNode(n);
							}
						}
					}
//					System.out.println("State "
//							+ gameState.getClass()
//							+ " with action "
//							+ n.getLastAction().getAction()
//							+ "\t with probability "
//							+ (double) Math.round(n.getLastAction().getProbability() * 10000) / 100
//							+ "% and totalProb "
//							+ (double) Math.round(probs.get(c) * 10000) / 100 + "%");
				}
//				System.out.println("> Chosen child with action " + 
//						parentOpponentModel.getChosenNode().getLastAction().getAction());
			}
//			System.out.println("-----------------------------<");
		}
		
		return new Prediction(parentOpponentModel.getChosenNode().getLastAction().getAction(),
				1, (cProb == null ? 0.0 : probs.get(cProb)));
	}
	
	private void assimilatePrediction(Prediction p) {
		truePositive += p.getTruePositive();
		trueNegative += p.getTrueNegative();
		falsePositive += p.getFalsePositive();
		falseNegative += p.getFalseNegative();
		printAccuracy();
	}
	
	@Override
	public void visitCallState(CallState callState) {
		InnerNode node = getNode(callState);
		if (node != null && !callState.getNextToAct().equals(parentOpponentModel.getBotId())) {
			Prediction p = getProbability(callState);
			if (p.getAction() instanceof CallAction) {
				assimilatePrediction(p);
				logger.trace(getPlayerName(callState) + " " + p);
			} else 
				System.err.println(getPlayerName(callState) + " CallState != " + p.getAction());
		} else {
			logger.trace(getPlayerName(callState) + " CallState");
		}
		propz.signalCall(false, callState.getEvent().getPlayerId());
	}
	
	@Override
	public void visitRaiseState(RaiseState raiseState) {
		InnerNode node = getNode(raiseState);
		if (node != null && !raiseState.getNextToAct().equals(parentOpponentModel.getBotId())) {
			Prediction p = getProbability(raiseState,raiseState.getLargestBet());
			if (p.getAction() instanceof RaiseAction) {
				assimilatePrediction(p);
				logger.trace(getPlayerName(raiseState) +
					" Raise " + Util.parseDollars(raiseState.getLargestBet()) + 
					" - with <" + p + ">");
			} else
				System.err.println(getPlayerName(raiseState) + " RaiseState: " + 
						Util.parseDollars(raiseState.getLargestBet()) + " != " + p.getAction());
		} else {
			logger.trace(getPlayerName(raiseState) + " RaiseState: " + Util.parseDollars(raiseState.getLargestBet()));
		}
		propz.signalRaise(false, raiseState.getLastEvent().getPlayerId(), raiseState.getLargestBet());
	}
	
	@Override
	public void visitFoldState(FoldState foldState) {
		InnerNode node = getNode(foldState);
		if (node != null && !foldState.getNextToAct().equals(parentOpponentModel.getBotId())) {
			Prediction p = getProbability(foldState);
			if (p.getAction() instanceof FoldAction) {
				assimilatePrediction(p);
				logger.trace(getPlayerName(foldState) + " " + p);
			} else 
				System.err.println(getPlayerName(foldState) + " FoldState" + " != " + p.getAction());
		} else {
			logger.trace(getPlayerName(foldState) + " FoldState");
		}
		propz.signalFold(foldState.getEvent().getPlayerId());
	}
	
	@Override
	public void visitCheckState(CheckState checkState) {
		InnerNode node = getNode(checkState);
		if (node != null && !checkState.getNextToAct().equals(parentOpponentModel.getBotId())) {
			Prediction p = getProbability(checkState);
			if (p.getAction() instanceof CheckAction) {
				assimilatePrediction(p);
				logger.trace(getPlayerName(checkState) + " " + p);
			} else 
				System.err.println(getPlayerName(checkState) + "CheckState" + " != " + p.getAction());
		} else {
			logger.trace(getPlayerName(checkState) + " CheckState");
		}
		propz.signalCheck(checkState.getEvent().getPlayerId());
	}
	
	@Override
	public void visitBetState(BetState betState) {
		InnerNode node = getNode(betState);
		if (node != null && !betState.getNextToAct().equals(parentOpponentModel.getBotId())) {
			Prediction p = getProbability(betState, betState.getEvent().getAmount());
			if (p.getAction() instanceof BetAction) {
				assimilatePrediction(p);
				logger.trace(getPlayerName(betState) +
					" Bet " + Util.parseDollars(betState.getEvent().getAmount()) + 
					" - with <" + p + ">");
			} else
				System.err.println(getPlayerName(betState) + " BetState: " + 
						Util.parseDollars(betState.getEvent().getAmount()) + " != " + p.getAction());
		} else {
			logger.trace(getPlayerName(betState) + " BetState: " + Util.parseDollars(betState.getEvent().getAmount()));
		}
		propz.signalBet(false, betState.getEvent().getPlayerId(), betState.getEvent().getAmount());
	}
	
	@Override
	public void visitAllInState(AllInState allInState) {
		InnerNode node = getNode(allInState);
		if (node != null && !allInState.getNextToAct().equals(parentOpponentModel.getBotId())) {
			Prediction p = getProbability(allInState, allInState.getEvent().getMovedAmount());
			if (p.getAction() instanceof BetAction ||
					p.getAction() instanceof RaiseAction||
					p.getAction() instanceof CallAction) {
				assimilatePrediction(p);
				logger.trace(getPlayerName(allInState) +
					" All-in " + Util.parseDollars(allInState.getEvent().getMovedAmount()) + 
					" - with <" + p + ">");
			} else
				System.err.println(getPlayerName(allInState) + " AllInState" + " != " + p.getAction());
		} else {
			logger.trace(getPlayerName(allInState) + " AllInState");
		}
		propz.signalAllIn(allInState.getEvent().getPlayerId(), allInState.getEvent().getMovedAmount());
	}
	
//	@Override
//	public void visitInitialGameState(DetailedHoldemTableState initialGameState) {
//		
//	}
//
//	@Override
//	public void visitJoinTableState(JoinTableState joinTableState) {
//
//	}
//
//	@Override
//	public void visitLeaveTableState(LeaveTableState leaveTableState) {
//
//	}
//
//	@Override
//	public void visitNewCommunityCardsState(
//			NewCommunityCardsState newCommunityCardsState) {
//		System.out.println("NewCommunityCardsState: " + newCommunityCardsState.getRound() + " ");
//		System.out.println("   " + newCommunityCardsState.getCommunityCards());
//		propz.signalCommunityCards(newCommunityCardsState.getCommunityCards());
//	}
//
//	@Override
//	public void visitNewDealState(NewDealState newDealState) {
//		System.out.println("(" + newDealState.getPlayer(newDealState.getDealer()).getName() + ") NewDealState");
//		propz.signalBBAmount(newDealState.getTableConfiguration().getBigBlind());
//		propz.signalNewGame();
//		for (PlayerState player : newDealState.getAllSeatedPlayers()) {
//			propz.signalSeatedPlayer(player.getStack(), player.getPlayerId());
//		}
//	}
//
//	@Override
//	public void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState) {
////		System.out.println("--------------------");
////		System.out.print("(" + newPocketCardsState.getPlayer().getName() + ") Pocket cards: ");
//		newPocketCardsState.getPlayerCards();
//	}
//
//	@Override
//	public void visitNewRoundState(NewRoundState newRoundState) {
//		System.out.println("NewRoundState: " + newRoundState.getRound());
//		if(newRoundState.getRound()==Round.FLOP){
//			propz.signalFlop();
//		}else if(newRoundState.getRound()==Round.TURN){
//			propz.signalTurn();
//		}else if(newRoundState.getRound()==Round.FINAL){
//			propz.signalRiver();
//		}
//	}
//
//	@Override
//	public void visitNextPlayerState(NextPlayerState nextPlayerState) {
//
//	}
//
//	@Override
//	public void visitShowHandState(ShowHandState showHandState) {
//		Card[] cardset = new Card[]{};
//		cardset = showHandState.getLastEvent().getShowdownPlayer()
//				.getHandCards().toArray(cardset);
//		System.out.println("("
//				+ showHandState.getPlayer(
//						showHandState.getLastEvent().getShowdownPlayer()
//								.getPlayerId()).getName() + ") ShowHandState: "
//				+ cardset[0] + ", " + cardset[1]);
//		propz.signalCardShowdown(showHandState.getLastEvent().getShowdownPlayer().getPlayerId(), cardset[0], cardset[1]);
//	}
//
//	@Override
//	public void visitSitInState(SitInState sitInState) {
//
//	}
//
//	@Override
//	public void visitSitOutState(SitOutState sitOutState) {
//
//	}
//
//	@Override
//	public void visitBlindState(BlindState blindState) {
//		System.out.println("(" + blindState.getPlayer(blindState.getLastEvent().getPlayerId()).getName() + ") BlindState: " + blindState.getRound());
//		propz.signalBlind(false, blindState.getLastEvent().getPlayerId(), blindState.getLastEvent().getAmount());
//	}
//
//	@Override
//	public void visitWinnerState(WinnerState winnerState) {
//		System.out.println("(" + winnerState.getLastEvent() + ") WinnerState: " + winnerState.getRound());
//	}
//
//	@Override
//	public void visitConfigChangeState(ConfigChangeState configChangeState) {
//		propz.signalBBAmount(configChangeState.getLastEvent().getTableConfig().getBigBlind());
//	}
}