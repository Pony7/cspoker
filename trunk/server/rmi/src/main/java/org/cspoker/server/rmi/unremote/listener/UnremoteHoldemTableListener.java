/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.server.rmi.unremote.listener;
import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.RemoteHoldemTableListener;
import org.cspoker.common.api.shared.Trigger;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class UnremoteHoldemTableListener extends ForwardingListener<RemoteHoldemTableListener> implements HoldemTableListener{

	private final static Logger logger = Logger.getLogger(UnremoteHoldemPlayerListener.class);
	
	private final Trigger connection;

	public UnremoteHoldemTableListener(Trigger connection, RemoteHoldemTableListener tableListener) {
		super(tableListener);
		this.connection = connection;
	}

	public void onBet(BetEvent betEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onBet(betEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onBigBlind(bigBlindEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onCall(CallEvent callEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onCall(callEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onCheck(CheckEvent checkEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onCheck(checkEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onFold(FoldEvent foldEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onFold(foldEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNewCommunityCards(newCommunityCardsEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onNewDeal(NewDealEvent newDealEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNewDeal(newDealEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onNewRound(NewRoundEvent newRoundEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNewRound(newRoundEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onNextPlayer(nextPlayerEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onRaise(RaiseEvent raiseEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onRaise(raiseEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onShowHand(ShowHandEvent showHandEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onShowHand(showHandEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onSitIn(SitInEvent sitInEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onSitIn(sitInEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onSmallBlind(smallBlindEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onWinner(WinnerEvent winnerEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onWinner(winnerEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onAllIn(AllInEvent allInEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onAllIn(allInEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onJoinTable(JoinTableEvent joinTableEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onJoinTable(joinTableEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onLeaveTable(leaveGameEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

	public void onSitOut(SitOutEvent sitOutEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onSitOut(sitOutEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}
	
	public void onPotsChanged(PotsChangedEvent potsChangedEvent) {
		try {
			for (RemoteHoldemTableListener listener : listeners) {
				listener.onPotsChanged(potsChangedEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}

}