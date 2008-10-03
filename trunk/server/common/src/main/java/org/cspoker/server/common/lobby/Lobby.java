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
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.server.common.ExtendedAccountContext;
import org.cspoker.server.common.PokerTable;

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
	private ConcurrentHashMap<Long, PokerTable> tables = new ConcurrentHashMap<Long, PokerTable>();
	
	protected Lobby(){
		
	}

	public DetailedHoldemTable createTable(ExtendedAccountContext accountContext, String name,
			TableConfiguration configuration) {
		long tableId = counter.getAndIncrement();
		PokerTable table = new PokerTable(tableId, name, configuration);		
		tables.put(tableId, table);
		
		for(LobbyListener listener:lobbyListeners){
			listener.onTableCreated(new TableCreatedEvent(accountContext.getPlayer(), new Table(tableId, name)));
		}
		
		return table.getTableInformation();
	}

	public HoldemTableContext getHoldemTableContext(ExtendedAccountContext accountContext, long tableId) {
		PokerTable mediator = tables.get(tableId);
		return mediator!=null? mediator.getHolemTableContext(accountContext): null;
	}

	public DetailedHoldemTable getTableInformation(long tableId) {
		PokerTable mediator = tables.get(tableId);
		return mediator!=null? mediator.getTableInformation(): null;
	}

	public TableList getTableList() {
		Collection<PokerTable> currentTables = tables.values();
		Set<Table> tableList = new TreeSet<Table>();
		for(PokerTable table:currentTables){
			tableList.add(table.getTableId());
		}
		return new TableList(new ArrayList<Table>(tableList));
	}

	public HoldemTableContext joinTable(long tableId) {
		PokerTable table = tables.get(tableId);
		return null;//TODO
	}

	public void removeTable(long tableId) {
		PokerTable table = tables.get(tableId);
		
		if(table!=null && table.isEmpty()){
			tables.remove(tableId);
		}
		Table tableInfo = table.getTableId();
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
