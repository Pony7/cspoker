package org.cspoker.client.rmi;

import java.rmi.RemoteException;

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

public class RemoteifyingListener implements RemoteAllEventsListener {

	private final static Logger logger = Logger
	.getLogger(RemoteifyingListener.class);

	private final RemoteAllEventsListener listener;

	public RemoteifyingListener(RemoteAllEventsListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAllInEvent(AllInEvent event) throws RemoteException {
		try{
			listener.onAllInEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onBetEvent(BetEvent event) throws RemoteException {
		try{
			listener.onBetEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onBigBlindEvent(BigBlindEvent event) throws RemoteException {
		try{
			listener.onBigBlindEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onCallEvent(CallEvent event) throws RemoteException {
		try{
			listener.onCallEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onCheckEvent(CheckEvent event) throws RemoteException {
		try{
			listener.onCheckEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onFoldEvent(FoldEvent event) throws RemoteException {
		try{
			listener.onFoldEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onRaiseEvent(RaiseEvent event) throws RemoteException {
		try{
			listener.onRaiseEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onSmallBlindEvent(SmallBlindEvent event) throws RemoteException {
		try{
			listener.onSmallBlindEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}

	}

	@Override
	public void onNewPocketCardsEvent(NewPocketCardsEvent event)
	throws RemoteException {
		try{
			listener.onNewPocketCardsEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}

	}

	@Override
	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
	throws RemoteException {
		try{
			listener.onNewCommunityCardsEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onNewDealEvent(NewDealEvent event) throws RemoteException {
		try{
			listener.onNewDealEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
		try{
			listener.onNewRoundEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onNextPlayerEvent(NextPlayerEvent event) throws RemoteException {
		try{
			listener.onNextPlayerEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event)
	throws RemoteException {
		try{
			listener.onPlayerJoinedGameEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
	throws RemoteException {
		try{
			listener.onPlayerLeftTableEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
		try{
			listener.onShowHandEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onWinnerEvent(WinnerEvent event) throws RemoteException {
		try{
			listener.onWinnerEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onGameMessageEvent(GameMessageEvent event)
	throws RemoteException {
		try{
			listener.onGameMessageEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onPlayerJoinedEvent(PlayerJoinedEvent event)
	throws RemoteException {
		try{
			listener.onPlayerJoinedEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onPlayerLeftEvent(PlayerLeftEvent event) throws RemoteException {
		try{
			listener.onPlayerLeftEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onTableCreatedEvent(TableCreatedEvent event)
	throws RemoteException {
		try{
			listener.onTableCreatedEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

	@Override
	public void onServerMessageEvent(ServerMessageEvent event)
	throws RemoteException {
		try{
			listener.onServerMessageEvent(event);
		}catch(RemoteException e){
			logger.error(e);
			throw e;
		}catch(Exception e){
			logger.error(e);
			throw new RemoteException("Unexpected exception",e);
		}
	}

}
