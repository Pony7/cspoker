package org.cspoker.server.common;

import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;

public class LobbyContextImpl implements LobbyContext {

	public LobbyContextImpl(ExtendedAccountContext accountContext) {
	}

	public DetailedTable createTable(String name,
			TableConfiguration configuration) {
		return null;
	}

	public HoldemTableContext getHoldemTableContext(long tableId) {
		return null;
	}

	public DetailedTable getTableInformation(long tableId) {
		return null;
	}

	public TableList getTableList() {
		return null;
	}

	public DetailedTable joinTable(long tableId) {
		return null;
	}

	public void removeTable(long tableId) {
	}

	public void subscribe(LobbyListener lobbyListener) {
	}

	public void unSubscribe(LobbyListener lobbyListener) {
	}

}
