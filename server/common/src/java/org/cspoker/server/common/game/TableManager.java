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

package org.cspoker.server.common.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.elements.table.GameTable;

/**
 * A class to manage tables.
 * 
 * @author Kenzo
 * 
 */
public class TableManager {
	
	public final static TableManager global_table_manager = new TableManager();

	/**
	 * The atomic variable used as atomic counter.
	 */
	private AtomicLong counter = new AtomicLong(0);

	/**
	 * The hash map containing all the tables of this server.
	 */
	private ConcurrentHashMap<TableId, GameTable> hashMap = new ConcurrentHashMap<TableId, GameTable>();

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
	public GameTable getTable(TableId id) {
		if (!hasATableWithId(id)) {
			throw new IllegalArgumentException("No such table.");
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

	public GameTable createTable(PlayerId id, String name) {
		TableId tableId = new TableId(counter.getAndIncrement());
		GameTable table = new GameTable(tableId, name, new GameProperty());
		hashMap.put(tableId, table);
		return table;
	}

	public Set<TableId> getAllTableIds() {
		return Collections.unmodifiableSet(hashMap.keySet());
	}
	
	public List<Table> getAllTables(){
		List<Table> tables = new ArrayList<Table>();
		for(GameTable table:hashMap.values()){
			tables.add(table.getSavedTable());
		}
		return tables;
	}

}
