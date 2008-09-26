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
package org.cspoker.server.rmi.asynchronous.listener;

import java.rmi.RemoteException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveGameEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RemoteHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.HoldemPlayerListener;
import org.cspoker.common.api.shared.Killable;

public class AsynchronousHoldemTableListener extends AsynchronousListener implements HoldemTableListener{

	private final RemoteHoldemTableListener holdemTableListener;
	private Executor executor;
	private AtomicReference<AsynchronousHoldemPlayerListener> holdemPlayerListener = new AtomicReference<AsynchronousHoldemPlayerListener>();

	public AsynchronousHoldemTableListener(Killable connection, Executor executor, RemoteHoldemTableListener holdemTableListener) {
		super(connection, executor);
		this.holdemTableListener = holdemTableListener;
	}

	public HoldemPlayerListener getHoldemPlayerListener() {
		if(holdemPlayerListener.get()==null){
			try {
				holdemPlayerListener.compareAndSet(null, new AsynchronousHoldemPlayerListener(connection, executor, holdemTableListener.getHoldemPlayerListener()));
			} catch (RemoteException exception) {
				die();
			}
		}
		return holdemPlayerListener.get();
	}

	public void onBet(final BetEvent betEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onBet(betEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onBigBlind(final BigBlindEvent bigBlindEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onBigBlind(bigBlindEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onCall(final CallEvent callEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onCall(callEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onCheck(final CheckEvent checkEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onCheck(checkEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onFold(final FoldEvent foldEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onFold(foldEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onLeaveGame(final LeaveGameEvent leaveGameEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onLeaveGame(leaveGameEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onNewCommunityCards(
			final NewCommunityCardsEvent newCommunityCardsEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onNewCommunityCards(newCommunityCardsEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onNewDeal(final NewDealEvent newDealEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onNewDeal(newDealEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onNewRound(final NewRoundEvent newRoundEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onNewRound(newRoundEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onNextPlayer(final NextPlayerEvent nextPlayerEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onNextPlayer(nextPlayerEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onRaise(final RaiseEvent raiseEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onRaise(raiseEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onShowHand(final ShowHandEvent showHandEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onShowHand(showHandEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onSitIn(final SitInEvent sitInEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onSitIn(sitInEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onSmallBlind(final SmallBlindEvent smallBlindEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onSmallBlind(smallBlindEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

	public void onWinner(final WinnerEvent winnerEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					holdemTableListener.onWinner(winnerEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

}
