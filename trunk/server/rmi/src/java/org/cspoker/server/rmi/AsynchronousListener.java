package org.cspoker.server.rmi;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameevents.BrokePlayerKickedOutEvent;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedTableEvent;
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
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableChangedEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.common.events.serverevents.TableRemovedEvent;
import org.cspoker.server.common.game.session.SessionManager;
import org.cspoker.server.common.util.threading.RequestExecutor;

public class AsynchronousListener implements RemoteAllEventsListener {

	private static Logger logger = Logger.getLogger(AsynchronousListener.class);
	
	private final RemoteAllEventsListener listener;
	private ExecutorService executor;

	private String name;

	public AsynchronousListener(RemoteAllEventsListener listener, String name) {
		this.listener = listener;
		this.name = name;
		this.executor = RequestExecutor.getInstance();
	}
	
	@Override
	public void onAllInEvent(final AllInEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onAllInEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onBetEvent(final BetEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onBetEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onBigBlindEvent(final BigBlindEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onBigBlindEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onCallEvent(final CallEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onCallEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onCheckEvent(final CheckEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onCheckEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onFoldEvent(final FoldEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onFoldEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onRaiseEvent(final RaiseEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onRaiseEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onSmallBlindEvent(final SmallBlindEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onSmallBlindEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onNewPocketCardsEvent(final NewPocketCardsEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onNewPocketCardsEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onNewCommunityCardsEvent(final NewCommunityCardsEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onNewCommunityCardsEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onNewDealEvent(final NewDealEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onNewDealEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onNewRoundEvent(final NewRoundEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onNewRoundEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onNextPlayerEvent(final NextPlayerEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onNextPlayerEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}


	@Override
	public void onPlayerLeftTableEvent(final PlayerLeftTableEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onPlayerLeftTableEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onShowHandEvent(final ShowHandEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onShowHandEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onWinnerEvent(final WinnerEvent event) throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onWinnerEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onGameMessageEvent(final GameMessageEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onGameMessageEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onTableCreatedEvent(final TableCreatedEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onTableCreatedEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onServerMessageEvent(final ServerMessageEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onServerMessageEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onPlayerJoinedTableEvent(final PlayerJoinedTableEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onPlayerJoinedTableEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onBrokePlayerKickedOutEvent(final BrokePlayerKickedOutEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onBrokePlayerKickedOutEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onTableChangedEvent(final TableChangedEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onTableChangedEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

	@Override
	public void onTableRemovedEvent(final TableRemovedEvent event)
			throws RemoteException {
		executor.submit(new Runnable(){
			@Override
			public void run() {
				try {
					listener.onTableRemovedEvent(event);
				} catch (RemoteException e) {
					logger.error(e);
					SessionManager.global_session_manager.killSession(name);
				}
			}
		});
	}

}
