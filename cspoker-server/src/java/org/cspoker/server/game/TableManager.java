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

package org.cspoker.server.game;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.gameControl.GameProperty;

/**
 * A class to manage tables.
 * 
 * @author Kenzo
 * 
 */
public class TableManager {

    /**
     * The atomic variable used as atomic counter.
     */
    private static AtomicLong counter = new AtomicLong(0);

    /**
     * The hash map containing all the tables of this server.
     */
    private static ConcurrentHashMap<TableId, Table> hashMap = new ConcurrentHashMap<TableId, Table>();

    /**
     * Get the table with the given id.
     * 
     * @param id
     *                The id of the player to return.
     * @pre The given id should be effective. |id!=null
     * @return The table with the given id.
     * @throws IllegalArgumentException
     *                 [must] There does not exist a table with given table id. |
     *                 !hasATableWithId(id)
     */
    public static Table getTable(TableId id) {
	if (!hasATableWithId(id))
	    throw new IllegalArgumentException();
	return hashMap.get(id);
    }

    /**
     * Check whether this table manager has a table with given id.
     * 
     * @param id
     *                The table id to check.
     * @return True if there exists a table with given id, False otherwise.
     */
    public static boolean hasATableWithId(TableId id) {
	return hashMap.containsKey(id);
    }

    public static void removeTable(Table id) {
	hashMap.remove(id);
    }

    public static Table createTable(PlayerId id) {
	TableId tableId = new TableId(counter.getAndIncrement());
	Table table = new Table(tableId, new GameProperty());
	hashMap.put(tableId, table);
	return table;
    }

    public static Set<TableId> getAllTableIds() {
	return Collections.unmodifiableSet(hashMap.keySet());
    }

}
