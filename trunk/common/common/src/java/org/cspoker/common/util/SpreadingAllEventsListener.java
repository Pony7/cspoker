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
package org.cspoker.common.util;

import java.rmi.RemoteException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameevents.ShowHandEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.events.serverevents.PlayerJoinedEvent;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;

public class SpreadingAllEventsListener implements RemoteAllEventsListener {

	private final static Logger logger = Logger
			.getLogger(SpreadingAllEventsListener.class);

	private final Set<RemoteAllEventsListener> listeners;

	public SpreadingAllEventsListener(Set<RemoteAllEventsListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void onAllInEvent(AllInEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onAllInEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	private void handle(RemoteAllEventsListener listener, RemoteException e) {
		logger.error(e);
		listeners.remove(listener);
	}

	@Override
	public void onBetEvent(BetEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onBetEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onBigBlindEvent(BigBlindEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onBigBlindEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onCallEvent(CallEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onCallEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onCheckEvent(CheckEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onCheckEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onFoldEvent(FoldEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onFoldEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onRaiseEvent(RaiseEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onRaiseEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onSmallBlindEvent(SmallBlindEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onSmallBlindEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onNewPocketCardsEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onNewCommunityCardsEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onNewDealEvent(NewDealEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onNewDealEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onNewRoundEvent(NewRoundEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onNewRoundEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onNextPlayerEvent(NextPlayerEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onNextPlayerEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onPlayerJoinedGameEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onPlayerLeftTableEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onShowHandEvent(ShowHandEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onShowHandEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onWinnerEvent(WinnerEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onWinnerEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onGameMessageEvent(GameMessageEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onGameMessageEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onPlayerJoinedEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onPlayerLeftEvent(PlayerLeftEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onPlayerLeftEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onTableCreatedEvent(TableCreatedEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onTableCreatedEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}

	@Override
	public void onServerMessageEvent(ServerMessageEvent event) {
		for (RemoteAllEventsListener listener : listeners) {
			try {
				listener.onServerMessageEvent(event);
			} catch (RemoteException e) {
				handle(listener, e);
			}
		}
	}
}
