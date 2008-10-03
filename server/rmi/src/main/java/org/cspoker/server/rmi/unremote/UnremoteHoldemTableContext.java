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
package org.cspoker.server.rmi.unremote;

import org.cspoker.common.api.lobby.holdemtable.context.ExternalHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.ForwardingHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
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
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.ExternalHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.RemoteHoldemTableListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class UnremoteHoldemTableContext extends ForwardingHoldemTableContext implements
ExternalHoldemTableContext {

	private final Killable connection;
	private UnremoteHoldemTableListener remoteListener;
	private UnremoteHoldemPlayerContext playerContext;

	public UnremoteHoldemTableContext(Killable connection, HoldemTableContext chatContext) {
		super(chatContext);
		this.connection = connection;
		this.playerContext = new UnremoteHoldemPlayerContext(connection,super.getHoldemPlayerContext());
	}

	@Override
	protected HoldemTableListener wrapListener(HoldemTableListener listener) {
		remoteListener = new UnremoteHoldemTableListener();
		remoteListener.subscribe(listener);
		return remoteListener;
	}
	
	@Override
	public ExternalHoldemPlayerContext getHoldemPlayerContext() {
		return playerContext;
	}

	public void subscribe(RemoteHoldemTableListener listener) {
		remoteListener.subscribe(listener);
	}

	public void unSubscribe(RemoteHoldemTableListener chatListener) {
		remoteListener.unSubscribe(chatListener);
	}

	public class UnremoteHoldemTableListener extends ForwardingListener<RemoteHoldemTableListener> implements HoldemTableListener{

		public void onBet(BetEvent betEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onBet(betEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onBigBlind(BigBlindEvent bigBlindEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onBigBlind(bigBlindEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onCall(CallEvent callEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onCall(callEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onCheck(CheckEvent checkEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onCheck(checkEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onFold(FoldEvent foldEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onFold(foldEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onLeaveGame(LeaveGameEvent leaveGameEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onLeaveGame(leaveGameEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onNewCommunityCards(
				NewCommunityCardsEvent newCommunityCardsEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onNewCommunityCards(newCommunityCardsEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onNewDeal(NewDealEvent newDealEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onNewDeal(newDealEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onNewRound(NewRoundEvent newRoundEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onNewRound(newRoundEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onNextPlayer(nextPlayerEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onRaise(RaiseEvent raiseEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onRaise(raiseEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onShowHand(ShowHandEvent showHandEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onShowHand(showHandEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onSitIn(SitInEvent sitInEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onSitIn(sitInEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onSmallBlind(smallBlindEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

		public void onWinner(WinnerEvent winnerEvent) {
			try {
				for (RemoteHoldemTableListener listener : listeners) {
					listener.onWinner(winnerEvent);
				}
			} catch (Exception exception) {
				connection.die();
			}
		}

	}

}
