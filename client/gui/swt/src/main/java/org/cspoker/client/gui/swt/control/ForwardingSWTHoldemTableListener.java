package org.cspoker.client.gui.swt.control;

import java.util.Arrays;

import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.eclipse.swt.widgets.Display;

/**
 * Dispatch remote events to GameWindow in UI thread, allowing SWT
 * modifications.
 * 
 * @author Stephan Schmidt
 */
public class ForwardingSWTHoldemTableListener
		extends ForwardingHoldemTableListener {
	
	public ForwardingSWTHoldemTableListener(HoldemTableListener gameWindow) {
		super(Arrays.asList(gameWindow));
	}
	
	@Override
	public void onBet(final BetEvent betEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onBet(betEvent);
			}
		});
	}
	
	@Override
	public void onBigBlind(final BigBlindEvent bigBlindEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onBigBlind(bigBlindEvent);
			}
		});
	}
	
	@Override
	public void onCall(final CallEvent callEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onCall(callEvent);
			}
		});
	}
	
	@Override
	public void onCheck(final CheckEvent checkEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onCheck(checkEvent);
			}
		});
	}
	
	@Override
	public void onFold(final FoldEvent foldEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onFold(foldEvent);
			}
		});
	}
	
	@Override
	public void onLeaveGame(final LeaveGameEvent leaveGameEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onLeaveGame(leaveGameEvent);
			}
		});
	}
	
	@Override
	public void onNewCommunityCards(final NewCommunityCardsEvent newCommunityCardsEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onNewCommunityCards(newCommunityCardsEvent);
			}
		});
	}
	
	@Override
	public void onNewDeal(final NewDealEvent newDealEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onNewDeal(newDealEvent);
			}
		});
	}
	
	@Override
	public void onNewRound(final NewRoundEvent newRoundEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onNewRound(newRoundEvent);
			}
		});
	}
	
	@Override
	public void onNextPlayer(final NextPlayerEvent nextPlayerEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onNextPlayer(nextPlayerEvent);
			}
		});
	}
	
	@Override
	public void onRaise(final RaiseEvent raiseEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onRaise(raiseEvent);
			}
		});
	}
	
	@Override
	public void onShowHand(final ShowHandEvent showHandEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onShowHand(showHandEvent);
			}
		});
	}
	
	@Override
	public void onSitIn(final SitInEvent sitInEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onSitIn(sitInEvent);
			}
		});
	}
	
	@Override
	public void onSmallBlind(final SmallBlindEvent smallBlindEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onSmallBlind(smallBlindEvent);
			}
		});
	}
	
	@Override
	public void onWinner(final WinnerEvent winnerEvent) {
		Display.getCurrent().syncExec(new Runnable() {
			
			public void run() {
				ForwardingSWTHoldemTableListener.super.onWinner(winnerEvent);
			}
		});
	}
}
