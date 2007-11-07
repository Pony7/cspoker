import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.RemotePlayerCommunication;
import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.common.game.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.game.eventlisteners.game.RemoteGameMessageListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNewCommunityCardsListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNewDealListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNewRoundListener;
import org.cspoker.common.game.eventlisteners.game.RemoteNextPlayerListener;
import org.cspoker.common.game.eventlisteners.game.RemotePlayerJoinedGameListener;
import org.cspoker.common.game.eventlisteners.game.RemotePlayerLeftTableListener;
import org.cspoker.common.game.eventlisteners.game.RemoteShowHandListener;
import org.cspoker.common.game.eventlisteners.game.RemoteWinnerListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteAllInListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteBetListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteBigBlindListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteCallListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteCheckListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteFoldListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteRaiseListener;
import org.cspoker.common.game.eventlisteners.game.actions.RemoteSmallBlindListener;
import org.cspoker.common.game.eventlisteners.game.privatelistener.RemoteNewPocketCardsListener;
import org.cspoker.common.game.eventlisteners.server.RemotePlayerJoinedListener;
import org.cspoker.common.game.eventlisteners.server.RemotePlayerLeftListener;
import org.cspoker.common.game.eventlisteners.server.RemoteServerMessageListener;
import org.cspoker.common.game.eventlisteners.server.RemoteTableCreatedListener;
import org.cspoker.common.rmi.RemoteRMIServer;


public class RemotePlayerCommunicationFactory {

    private RemoteRMIServer server;

    public RemotePlayerCommunicationFactory(String server, int port) throws AccessException, RemoteException, NotBoundException{
	System.setSecurityManager(null);
	Registry registry= LocateRegistry.getRegistry(server,port);
	this.server = (RemoteRMIServer)registry.lookup("CSPokerServer");
    }

    public RemotePlayerCommunication login(String username, String password) throws RemoteException{

	final RemotePlayerCommunication p =  server.login(username, password);

	return new RemotePlayerCommunication(){

	    public void allIn() throws IllegalActionException, RemoteException {
		p.allIn();
	    }

	    public void bet(int amount) throws IllegalActionException,
	    RemoteException {
		p.bet(amount);

	    }

	    public void call() throws IllegalActionException, RemoteException {
		p.call();
	    }

	    public void check() throws IllegalActionException, RemoteException {
		p.check();
	    }

	    public TableId createTable() throws IllegalActionException,RemoteException {
		return p.createTable();
	    }

	    public void deal() throws IllegalActionException, RemoteException {
		p.deal();

	    }

	    public void fold() throws IllegalActionException, RemoteException {
		p.fold();
	    }

	    public void joinTable(TableId id) throws IllegalActionException,
	    RemoteException {
		p.joinTable(id);
	    }

	    public void leaveTable() throws IllegalActionException,
	    RemoteException {
		p.leaveTable();
	    }

	    public void raise(int amount) throws IllegalActionException,
	    RemoteException {
		p.raise(amount);
	    }

	    public void say(String message) throws RemoteException {
		p.say(message);
	    }

	    public void startGame() throws IllegalActionException,
	    RemoteException {
		p.startGame();
	    }

	    public void subscribeAllEventsListener(
		    RemoteAllEventsListener listener) throws RemoteException {
		RemoteAllEventsListener listenerStub 
		= (RemoteAllEventsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeAllEventsListener(listenerStub);
	    }

	    public void subscribeAllInListener(RemoteAllInListener listener)
	    throws RemoteException {
		RemoteAllInListener listenerStub 
		= (RemoteAllInListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeAllInListener(listenerStub);

	    }

	    public void subscribeBetListener(RemoteBetListener listener)
	    throws RemoteException {
		RemoteBetListener listenerStub 
		= (RemoteBetListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeBetListener(listenerStub);


	    }

	    public void subscribeBigBlindListener(
		    RemoteBigBlindListener listener) throws RemoteException {
		RemoteBigBlindListener listenerStub 
		= (RemoteBigBlindListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeBigBlindListener(listenerStub);

	    }

	    public void subscribeCallListener(RemoteCallListener listener)
	    throws RemoteException {
		RemoteCallListener listenerStub 
		= (RemoteCallListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeCallListener(listenerStub);


	    }

	    public void subscribeCheckListener(RemoteCheckListener listener)
	    throws RemoteException {
		RemoteCheckListener listenerStub 
		= (RemoteCheckListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeCheckListener(listenerStub);


	    }

	    public void subscribeFoldListener(RemoteFoldListener listener)
	    throws RemoteException {
		RemoteFoldListener listenerStub 
		= (RemoteFoldListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeFoldListener(listenerStub);

	    }

	    public void subscribeGameMessageListener(
		    RemoteGameMessageListener listener) throws RemoteException {
		RemoteGameMessageListener listenerStub 
		= (RemoteGameMessageListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeGameMessageListener(listenerStub);


	    }

	    public void subscribeNewCommonCardsListener(
		    RemoteNewCommunityCardsListener listener)
	    throws RemoteException {
		RemoteNewCommunityCardsListener listenerStub 
		= (RemoteNewCommunityCardsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeNewCommonCardsListener(listenerStub);


	    }

	    public void subscribeNewDealListener(RemoteNewDealListener listener)
	    throws RemoteException {
		RemoteNewDealListener listenerStub 
		= (RemoteNewDealListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeNewDealListener(listenerStub);


	    }

	    public void subscribeNewPocketCardsListener(
		    RemoteNewPocketCardsListener listener)
	    throws RemoteException {
		RemoteNewPocketCardsListener listenerStub 
		= (RemoteNewPocketCardsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeNewPocketCardsListener(listenerStub);


	    }

	    public void subscribeNewRoundListener(
		    RemoteNewRoundListener listener) throws RemoteException {
		RemoteNewRoundListener listenerStub 
		= (RemoteNewRoundListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeNewRoundListener(listenerStub);


	    }

	    public void subscribeNextPlayerListener(
		    RemoteNextPlayerListener listener) throws RemoteException {
		RemoteNextPlayerListener listenerStub 
		= (RemoteNextPlayerListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeNextPlayerListener(listenerStub);


	    }

	    public void subscribePlayerJoinedGameListener(
		    RemotePlayerJoinedGameListener listener)
	    throws RemoteException {
		RemotePlayerJoinedGameListener listenerStub 
		= (RemotePlayerJoinedGameListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribePlayerJoinedGameListener(listenerStub);


	    }

	    public void subscribePlayerJoinedListener(
		    RemotePlayerJoinedListener listener) throws RemoteException {
		RemotePlayerJoinedListener listenerStub 
		= (RemotePlayerJoinedListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribePlayerJoinedListener(listenerStub);


	    }

	    public void subscribePlayerLeftListener(
		    RemotePlayerLeftListener listener) throws RemoteException {
		RemotePlayerLeftListener listenerStub 
		= (RemotePlayerLeftListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribePlayerLeftListener(listenerStub);


	    }

	    public void subscribePlayerLeftTableListener(
		    RemotePlayerLeftTableListener listener)
	    throws RemoteException {
		RemotePlayerLeftTableListener listenerStub 
		= (RemotePlayerLeftTableListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribePlayerLeftTableListener(listenerStub);

	    }

	    public void subscribeRaiseListener(RemoteRaiseListener listener)
	    throws RemoteException {
		RemoteRaiseListener listenerStub 
		= (RemoteRaiseListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeRaiseListener(listenerStub);

	    }

	    public void subscribeServerMessageListener(
		    RemoteServerMessageListener listener)
	    throws RemoteException {
		RemoteServerMessageListener listenerStub 
		= (RemoteServerMessageListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeServerMessageListener(listenerStub);

	    }

	    public void subscribeShowHandListener(
		    RemoteShowHandListener listener) throws RemoteException {
		RemoteShowHandListener listenerStub 
		= (RemoteShowHandListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeShowHandListener(listenerStub);

	    }

	    public void subscribeSmallBlindListener(
		    RemoteSmallBlindListener listener) throws RemoteException {
		RemoteSmallBlindListener listenerStub 
		= (RemoteSmallBlindListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeSmallBlindListener(listenerStub);

	    }

	    public void subscribeTableCreatedListener(
		    RemoteTableCreatedListener listener) throws RemoteException {
		RemoteTableCreatedListener listenerStub 
		= (RemoteTableCreatedListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeTableCreatedListener(listenerStub);

	    }

	    public void subscribeWinnerListener(RemoteWinnerListener listener)
	    throws RemoteException {
		RemoteWinnerListener listenerStub 
		= (RemoteWinnerListener)UnicastRemoteObject.exportObject(listener, 0);
		p.subscribeWinnerListener(listenerStub);

	    }

	    public void unsubscribeAllEventsListener(
		    RemoteAllEventsListener listener) throws RemoteException {
		RemoteAllEventsListener listenerStub 
		= (RemoteAllEventsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeAllEventsListener(listenerStub);

	    }

	    public void unsubscribeAllInListener(RemoteAllInListener listener)
	    throws RemoteException {
		RemoteAllInListener listenerStub 
		= (RemoteAllInListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeAllInListener(listenerStub);

	    }

	    public void unsubscribeBetListener(RemoteBetListener listener)
	    throws RemoteException {
		RemoteBetListener listenerStub 
		= (RemoteBetListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeBetListener(listenerStub);

	    }

	    public void unsubscribeBigBlindListener(
		    RemoteBigBlindListener listener) throws RemoteException {
		RemoteBigBlindListener listenerStub 
		= (RemoteBigBlindListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeBigBlindListener(listenerStub);

	    }

	    public void unsubscribeCallListener(RemoteCallListener listener)
	    throws RemoteException {
		RemoteCallListener listenerStub 
		= (RemoteCallListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeCallListener(listenerStub);

	    }

	    public void unsubscribeCheckListener(RemoteCheckListener listener)
	    throws RemoteException {
		RemoteCheckListener listenerStub 
		= (RemoteCheckListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeCheckListener(listenerStub);

	    }

	    public void unsubscribeFoldListener(RemoteFoldListener listener)
	    throws RemoteException {
		RemoteFoldListener listenerStub 
		= (RemoteFoldListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeFoldListener(listenerStub);

	    }

	    public void unsubscribeGameMessageListener(
		    RemoteGameMessageListener listener) throws RemoteException {
		RemoteGameMessageListener listenerStub 
		= (RemoteGameMessageListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeGameMessageListener(listenerStub);

	    }

	    public void unsubscribeNewCommonCardsListener(
		    RemoteNewCommunityCardsListener listener)
	    throws RemoteException {
		RemoteNewCommunityCardsListener listenerStub 
		= (RemoteNewCommunityCardsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeNewCommonCardsListener(listenerStub);

	    }

	    public void unsubscribeNewDealListener(
		    RemoteNewDealListener listener) throws RemoteException {
		RemoteNewDealListener listenerStub 
		= (RemoteNewDealListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeNewDealListener(listenerStub);

	    }

	    public void unsubscribeNewPocketCardsListener(
		    RemoteNewPocketCardsListener listener)
	    throws RemoteException {
		RemoteNewPocketCardsListener listenerStub 
		= (RemoteNewPocketCardsListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeNewPocketCardsListener(listenerStub);

	    }

	    public void unsubscribeNewRoundListener(
		    RemoteNewRoundListener listener) throws RemoteException {
		RemoteNewRoundListener listenerStub 
		= (RemoteNewRoundListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeNewRoundListener(listenerStub);

	    }

	    public void unsubscribeNextPlayerListener(
		    RemoteNextPlayerListener listener) throws RemoteException {
		RemoteNextPlayerListener listenerStub 
		= (RemoteNextPlayerListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeNextPlayerListener(listenerStub);

	    }

	    public void unsubscribePlayerJoinedGameListener(
		    RemotePlayerJoinedGameListener listener)
	    throws RemoteException {
		RemotePlayerJoinedGameListener listenerStub 
		= (RemotePlayerJoinedGameListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribePlayerJoinedGameListener(listenerStub);

	    }

	    public void unsubscribePlayerJoinedListener(
		    RemotePlayerJoinedListener listener) throws RemoteException {
		RemotePlayerJoinedListener listenerStub 
		= (RemotePlayerJoinedListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribePlayerJoinedListener(listenerStub);

	    }

	    public void unsubscribePlayerLeftListener(
		    RemotePlayerLeftListener listener) throws RemoteException {
		RemotePlayerLeftListener listenerStub 
		= (RemotePlayerLeftListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribePlayerLeftListener(listenerStub);

	    }

	    public void unsubscribePlayerLeftTableListener(
		    RemotePlayerLeftTableListener listener)
	    throws RemoteException {
		RemotePlayerLeftTableListener listenerStub 
		= (RemotePlayerLeftTableListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribePlayerLeftTableListener(listenerStub);

	    }

	    public void unsubscribeRaiseListener(RemoteRaiseListener listener)
	    throws RemoteException {
		RemoteRaiseListener listenerStub 
		= (RemoteRaiseListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeRaiseListener(listenerStub);

	    }

	    public void unsubscribeServerMessageListener(
		    RemoteServerMessageListener listener)
	    throws RemoteException {
		RemoteServerMessageListener listenerStub 
		= (RemoteServerMessageListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeServerMessageListener(listenerStub);

	    }

	    public void unsubscribeShowHandListener(
		    RemoteShowHandListener listener) throws RemoteException {
		RemoteShowHandListener listenerStub 
		= (RemoteShowHandListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeShowHandListener(listenerStub);

	    }

	    public void unsubscribeSmallBlindListener(
		    RemoteSmallBlindListener listener) throws RemoteException {
		RemoteSmallBlindListener listenerStub 
		= (RemoteSmallBlindListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeSmallBlindListener(listenerStub);

	    }

	    public void unsubscribeTableCreatedListener(
		    RemoteTableCreatedListener listener) throws RemoteException {
		RemoteTableCreatedListener listenerStub 
		= (RemoteTableCreatedListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeTableCreatedListener(listenerStub);

	    }

	    public void unsubscribeWinnerListener(RemoteWinnerListener listener)
	    throws RemoteException {
		RemoteWinnerListener listenerStub 
		= (RemoteWinnerListener)UnicastRemoteObject.exportObject(listener, 0);
		p.unsubscribeWinnerListener(listenerStub);

	    }

	};
    }

}
