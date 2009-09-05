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
package org.cspoker.common.api.lobby.holdemtable.listener;

import java.util.concurrent.Executor;

import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ConfigChangeEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;

public class AsynchronousHoldemTableListener implements HoldemTableListener{

	private final HoldemTableListener holdemTableListener;
	private Executor executor;
	public AsynchronousHoldemTableListener(Executor executor, HoldemTableListener holdemTableListener) {
		this.executor = executor;
		this.holdemTableListener = holdemTableListener;
	}

	public void onBet(final BetEvent betEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onBet(betEvent);
			}
		});
	}

	public void onBigBlind(final BigBlindEvent bigBlindEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onBigBlind(bigBlindEvent);
			}
		});
	}

	public void onCall(final CallEvent callEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onCall(callEvent);
			}
		});
	}

	public void onCheck(final CheckEvent checkEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onCheck(checkEvent);
			}
		});
	}

	public void onFold(final FoldEvent foldEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onFold(foldEvent);
			}
		});
	}

	public void onNewCommunityCards(
			final NewCommunityCardsEvent newCommunityCardsEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onNewCommunityCards(newCommunityCardsEvent);
			}
		});
	}

	public void onNewDeal(final NewDealEvent newDealEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onNewDeal(newDealEvent);
			}
		});
	}

	public void onNewRound(final NewRoundEvent newRoundEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onNewRound(newRoundEvent);
			}
		});
	}

	public void onNextPlayer(final NextPlayerEvent nextPlayerEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onNextPlayer(nextPlayerEvent);
			}
		});
	}

	public void onRaise(final RaiseEvent raiseEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onRaise(raiseEvent);
			}
		});
	}

	public void onShowHand(final ShowHandEvent showHandEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onShowHand(showHandEvent);
			}
		});
	}

	public void onSitIn(final SitInEvent sitInEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onSitIn(sitInEvent);
			}
		});
	}

	public void onSmallBlind(final SmallBlindEvent smallBlindEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onSmallBlind(smallBlindEvent);
			}
		});
	}

	public void onWinner(final WinnerEvent winnerEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onWinner(winnerEvent);
			}
		});
	}

	public void onAllIn(final AllInEvent allInEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onAllIn(allInEvent);
			}
		});
	}

	public void onJoinTable(final JoinTableEvent joinTableEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onJoinTable(joinTableEvent);
			}
		});
	}

	public void onLeaveTable(final LeaveTableEvent leaveGameEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onLeaveTable(leaveGameEvent);
			}
		});
	}

	public void onSitOut(final SitOutEvent sitOutEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onSitOut(sitOutEvent);
			}
		});
	}
	
	@Override
	public void onConfigChange(final ConfigChangeEvent configChangeEvent) {
		executor.execute(new Runnable() {
			public void run() {
				holdemTableListener.onConfigChange(configChangeEvent);
			}
		});
	}

}
