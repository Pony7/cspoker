package org.cspoker.server.rmi.unremote.listener;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveGameEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.RemoteHoldemTableListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class UnremoteHoldemTableListener extends ForwardingListener<RemoteHoldemTableListener> implements HoldemTableListener{

	private final Killable connection;

	public UnremoteHoldemTableListener(Killable connection, RemoteHoldemTableListener tableListener) {
		super(tableListener);
		this.connection = connection;
	}

	public void onBet(BetEvent betEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onBet(betEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onBigBlind(bigBlindEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onCall(CallEvent callEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onCall(callEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onCheck(CheckEvent checkEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onCheck(checkEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onFold(FoldEvent foldEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onFold(foldEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onLeaveGame(LeaveGameEvent leaveGameEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onLeaveGame(leaveGameEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNewCommunityCards(newCommunityCardsEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onNewDeal(NewDealEvent newDealEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNewDeal(newDealEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onNewRound(NewRoundEvent newRoundEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNewRound(newRoundEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNextPlayer(nextPlayerEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onRaise(RaiseEvent raiseEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onRaise(raiseEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onShowHand(ShowHandEvent showHandEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onShowHand(showHandEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onSitIn(SitInEvent sitInEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onSitIn(sitInEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onSmallBlind(smallBlindEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

	public void onWinner(WinnerEvent winnerEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onWinner(winnerEvent);
			}
		} catch (Exception exception) {
			connection.kill();
		}
	}

}