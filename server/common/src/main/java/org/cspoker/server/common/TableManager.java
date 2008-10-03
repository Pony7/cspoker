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

package org.cspoker.server.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.cspoker.common.elements.table.DetailedTable;
<<<<<<< .mine
import org.cspoker.server.common.exception.TableDoesNotExistException;
import org.cspoker.server.common.gamecontrol.WaitingTableState;
=======
import org.cspoker.server.common.elements.table.GameTable;
import org.cspoker.server.common.exception.TableDoesNotExistException;

/**
 * A class to manage tables.
 * 
 * @author Kenzo
 * 
 */
@Deprecated
public class TableManager {

	public final static TableManager global_table_manager = new TableManager();

	/**
	 * The atomic variable used as atomic counter.
	 */
	private AtomicLong counter = new AtomicLong(0);

	/**
	 * The hash map containing all the tables of this server.
	 */
	private ConcurrentHashMap<TableId, WaitingTableState> hashMap = new ConcurrentHashMap<TableId, WaitingTableState>();

	/**
	 * Get the table with the given id.
	 * 
	 * @param id
	 *            The id of the player to return.
	 * @pre The given id should be effective. |id!=null
	 * @return The table with the given id.
	 * @throws IllegalArgumentException
	 *             [must] There does not exist a table with given table id. |
	 *             !hasATableWithId(id)
	 */
	public WaitingTableState getTable(TableId id) throws TableDoesNotExistException {
		if (!hasATableWithId(id)) {
			throw new TableDoesNotExistException(id);
		}
		return hashMap.get(id);
	}

	/**
	 * Check whether this table manager has a table with given id.
	 * 
	 * @param id
	 *            The table id to check.
	 * @return True if there exists a table with given id, False otherwise.
	 */
	public boolean hasATableWithId(TableId id) {
		return hashMap.containsKey(id);
	}

	public void removeTable(TableId id) {
		hashMap.remove(id);
	}

	public WaitingTableState createTable(PlayerId id, String name,
			GameProperty gameProperty) {
		TableId tableId = new TableId(counter.getAndIncrement());
		WaitingTableState table = new WaitingTableState(tableId, name, gameProperty);
		hashMap.put(tableId, table);
		return table;
	}

	public Set<TableId> getAllTableIds() {
		return Collections.unmodifiableSet(hashMap.keySet());
	}

	public List<DetailedTable> getAllTables() {
		List<DetailedTable> tables = new ArrayList<DetailedTable>();
		for (WaitingTableState table : hashMap.values()) {
			tables.add(table.getSavedTable());
		}
		return tables;
	}
}
