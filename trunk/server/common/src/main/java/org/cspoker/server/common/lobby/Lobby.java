package org.cspoker.server.common.lobby;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.server.common.ExtendedAccountContext;
import org.cspoker.server.common.elements.id.TableId;
import org.cspoker.server.common.gamecontrol.PokerTable;

public class Lobby{
	
	/**
	 * Singleton lobby
	 */
	private static Lobby lobby = new Lobby();
	
	/**
	 * The atomic variable used as atomic counter.
	 */
	private AtomicLong counter = new AtomicLong(0);
	
	private final List<LobbyListener> lobbyListeners = new CopyOnWriteArrayList<LobbyListener>();

	/**
	 * The hash map containing all the tables of this lobby.
	 */
	private ConcurrentHashMap<TableId, PokerTable> tables = new ConcurrentHashMap<TableId, PokerTable>();
	
	protected Lobby(){
		
	}

	public DetailedHoldemTable createTable(ExtendedAccountContext accountContext, String name,
			TableConfiguration configuration) {
		TableId tableId = new TableId(counter.getAndIncrement());
		PokerTable table = new PokerTable(tableId, name, configuration, accountContext);		
		tables.put(tableId, table);
		
		for(LobbyListener listener:lobbyListeners){
			listener.onTableCreated(new TableCreatedEvent(accountContext.getPlayer().getMemento(), new Table(tableId.getId(), name)));
		}
		
		return table.getTableInformation();
	}

	public DetailedHoldemTable getTableInformation(long tableId) {
		PokerTable table = tables.get(tableId);
		return table!=null? table.getTableInformation(): null;
	}

	public TableList getTableList() {
		Collection<PokerTable> currentTables = tables.values();
		Set<Table> tableList = new TreeSet<Table>();
		for(PokerTable table:currentTables){
			tableList.add(table.getShortTableInformation());
		}
		return new TableList(new ArrayList<Table>(tableList));
	}

	public HoldemTableContext joinTable(TableId tableId, HoldemTableListener holdemTableListener, ExtendedAccountContext accountContext) throws IllegalActionException{
		if(!tables.containsKey(tableId)){
			throw new IllegalActionException("The provided table #"+tableId+" to join does not exist.");
		}
		if(holdemTableListener==null)
			throw new IllegalArgumentException("The given holdem table listener is not effective.");
		
		
		PokerTable table = tables.get(tableId);
		
		return table.joinTable(accountContext.getPlayer(), holdemTableListener);
	}

	public void removeTable(long tableId) {
		PokerTable table = tables.get(tableId);
		
		if(table!=null && table.isEmpty()){
			tables.remove(tableId);
		}
		Table tableInfo = table.getShortTableInformation();
		table.terminate();
		for(LobbyListener listener:lobbyListeners){
			listener.onTableRemoved(new TableRemovedEvent(tableInfo));
		}
		
		//TODO Concurrency issue: after removal joining table.
	}

	public void subscribe(LobbyListener lobbyListener) {
		lobbyListeners.add(lobbyListener);
	}

	public void unSubscribe(LobbyListener lobbyListener) {
		lobbyListeners.remove(lobbyListener);
	}
	
	public static Lobby getInstance(){
		return lobby;
	}

}
