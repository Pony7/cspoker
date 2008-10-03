package org.cspoker.server.common;

import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.server.common.lobby.Lobby;

public class LobbyContextImpl implements LobbyContext {
	
	private ExtendedAccountContext accountContext;
	private Lobby lobby;

	public LobbyContextImpl(ExtendedAccountContext accountContext, Lobby lobby) {
		this.accountContext = accountContext;
		this.lobby = lobby;
	}

	public DetailedTable createTable(String name,
			TableConfiguration configuration) {
		return lobby.createTable(accountContext, name, configuration);
	}

	public HoldemTableContext getHoldemTableContext(long tableId) {
		return null; //TODO
	}

	public DetailedTable getTableInformation(long tableId) {
		return lobby.getTableInformation(tableId);
	}

	public TableList getTableList() {
		return lobby.getTableList();
	}

	public DetailedTable joinTable(long tableId) {
		return null; //TODO
	}

	public void removeTable(long tableId) {
		lobby.removeTable(tableId);
	}

	public void subscribe(LobbyListener lobbyListener) {
		lobby.subscribe(lobbyListener);
	}

	public void unSubscribe(LobbyListener lobbyListener) {
		lobby.unSubscribe(lobbyListener);
	}

}
