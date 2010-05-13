package org.cspoker.ai.opponentmodels.weka;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.DetailedHoldemTableState;
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
import org.cspoker.common.elements.table.Round;

public class ActionTrackingVisitor extends PlayerTrackingVisitor {
	
	private final static Logger logger = Logger.getLogger(ARFFPropositionalizer.class);
	
	public ActionTrackingVisitor(WekaRegressionModel baseModel) {
		try {
			this.propz = new ARFFPropositionalizer(baseModel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ARFFPropositionalizer getPropz() {
		return (ARFFPropositionalizer) this.propz;
	}
	
	@Override
	public void setPropz(Propositionalizer propz) {
		this.propz = propz;
		getPropz().setNewDeal(false);		
	}
	
	@Override
	public void visitAllInState(AllInState allInState) {
		if(getPropz().isNewDeal()) logger.trace("(" + allInState.getPlayer(allInState.getNextToAct()).getName() + ") AllInState: " + allInState.getRound());
		propz.signalAllIn(allInState.getEvent().getPlayerId(), allInState.getEvent().getMovedAmount());
	}

	@Override
	public void visitBetState(BetState betState) {
		if(getPropz().isNewDeal()) logger.trace("(" + betState.getPlayer(betState.getNextToAct()).getName() + ") BetState: " + betState.getEvent().getAmount());
		propz.signalBet(false, betState.getEvent().getPlayerId(), betState.getEvent().getAmount());
	}

	@Override
	public void visitCallState(CallState callState) {
		if(getPropz().isNewDeal()) logger.trace("(" + callState.getPlayer(callState.getNextToAct()).getName() + ") CallState");
		propz.signalCall(false, callState.getEvent().getPlayerId());
	}

	@Override
	public void visitCheckState(CheckState checkState) {
		if(getPropz().isNewDeal()) logger.trace("(" + checkState.getPlayer(checkState.getNextToAct()).getName() + ") CheckState");
		propz.signalCheck(checkState.getEvent().getPlayerId());
	}

	@Override
	public void visitFoldState(FoldState foldState) {
		if(getPropz().isNewDeal()) logger.trace("(" + foldState.getPlayer(foldState.getNextToAct()).getName() + ") FoldState");
		propz.signalFold(foldState.getEvent().getPlayerId());
	}

	@Override
	public void visitInitialGameState(DetailedHoldemTableState initialGameState) {
		
	}

	@Override
	public void visitJoinTableState(JoinTableState joinTableState) {

	}

	@Override
	public void visitLeaveTableState(LeaveTableState leaveTableState) {

	}

	@Override
	public void visitNewCommunityCardsState(
			NewCommunityCardsState newCommunityCardsState) {
		if(getPropz().isNewDeal()) {
			logger.trace("NewCommunityCardsState: " + newCommunityCardsState.getRound() + " ");
			for(Card c : newCommunityCardsState.getCommunityCards())
				logger.trace("   ("+ c +")");
		}
		propz.signalCommunityCards(newCommunityCardsState.getCommunityCards());
	}

	@Override
	public void visitNewDealState(NewDealState newDealState) {
		logger.trace("(" + newDealState.getPlayer(newDealState.getDealer()).getName() + ") NewDealState");
//		setPropz(ARFFPropositionalizerFactory.getPropz(player))
		getPropz().setNewDeal(true);
		propz.signalBBAmount(newDealState.getTableConfiguration().getBigBlind());
		propz.signalNewGame();
		for(PlayerState player: newDealState.getAllSeatedPlayers()){
			propz.signalSeatedPlayer(player.getStack(), player.getPlayerId());
		}
	}

	@Override
	public void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState) {

	}

	@Override
	public void visitNewRoundState(NewRoundState newRoundState) {
		if(getPropz().isNewDeal()) logger.trace("NewRoundState: " + newRoundState.getRound());
		if(newRoundState.getRound()==Round.FLOP){
			propz.signalFlop();
		}else if(newRoundState.getRound()==Round.TURN){
			propz.signalTurn();
		}else if(newRoundState.getRound()==Round.FINAL){
			propz.signalRiver();
		}
	}

	@Override
	public void visitNextPlayerState(NextPlayerState nextPlayerState) {

	}

	@Override
	public void visitRaiseState(RaiseState raiseState) {
		if(getPropz().isNewDeal()) logger.trace("(" + raiseState.getPlayer(raiseState.getNextToAct()).getName() + ") RaiseState: " + raiseState.getLargestBet());
		propz.signalRaise(false, raiseState.getLastEvent().getPlayerId(), raiseState.getLargestBet());
	}

	@Override
	public void visitShowHandState(ShowHandState showHandState) {
		Card[] cardset = new Card[]{};
		cardset = showHandState.getLastEvent().getShowdownPlayer()
				.getHandCards().toArray(cardset);
		if(getPropz().isNewDeal()) logger.trace("("
				+ showHandState.getPlayer(
						showHandState.getLastEvent().getShowdownPlayer()
								.getPlayerId()).getName() + ") ShowHandState: "
				+ cardset[0] + ", " + cardset[1]);
		propz.signalCardShowdown(showHandState.getLastEvent().getShowdownPlayer().getPlayerId(), cardset[0], cardset[1]);
	}

	@Override
	public void visitSitInState(SitInState sitInState) {

	}

	@Override
	public void visitSitOutState(SitOutState sitOutState) {

	}

	@Override
	public void visitBlindState(BlindState blindState) {
		if(getPropz().isNewDeal()) logger.trace("(" + blindState.getPlayer(blindState.getLastEvent().getPlayerId()).getName() + ") BlindState: " + blindState.getRound());
		propz.signalBlind(false, blindState.getLastEvent().getPlayerId(), blindState.getLastEvent().getAmount());
	}

	@Override
	public void visitWinnerState(WinnerState winnerState) {
		if(getPropz().isNewDeal()) logger.trace("(" + winnerState.getLastEvent().getWinners().toArray()[0] + ") WinnerState: " + winnerState.getRound());
	}

	@Override
	public void visitConfigChangeState(ConfigChangeState configChangeState) {
		propz.signalBBAmount(configChangeState.getLastEvent().getTableConfig().getBigBlind());
	}
}
