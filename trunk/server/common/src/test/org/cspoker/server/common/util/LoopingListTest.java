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

package org.cspoker.server.common.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class LoopingListTest extends TestCase {

	private LoopingList<Object> list;

	private List<Object> initialList;

	
	protected void setUp() {
		initialList = new ArrayList<Object>();
		initialList.add(new Object());
		initialList.add(new Object());
		initialList.add(new Object());
		list = new LoopingList<Object>(initialList);
	}

	public void testLoopingList() {
		assertEquals(initialList.get(0), list.getCurrent());
		assertTrue(list.size() == initialList.size());
	}

	public void testNext() {
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < initialList.size(); i++) {
				assertEquals(initialList.get(i), list.getCurrent());
				list.next();
			}
		}
	}

	public void testGoBack() {
		list.goBack();
		for (int j = 0; j < 4; j++) {
			for (int i = initialList.size() - 1; i >= 0; i--) {
				assertEquals(initialList.get(i), list.getCurrent());
				list.goBack();
			}
		}
	}
	
	public void testGetBefore() {
		assertEquals(initialList.get(initialList.size()-1), list.getPreviousTo(initialList.get(0)));
		for (int i = 1; i < initialList.size(); i++) {
			assertEquals(initialList.get(i-1), list.getPreviousTo(initialList.get(i)));
		}
	}

	public void testSetCurrent() {
		list.next();
		list.next();
		assertEquals(list.getCurrent(), initialList.get(2));
		list.setCurrent(initialList.get(0));
		assertEquals(initialList.get(0), list.getCurrent());
		list.setCurrent(initialList.get(1));
		assertEquals(initialList.get(1), list.getCurrent());
	}

	/**
	 * Normal case, current element index is less than the removed element.
	 * 
	 */
	public void testRemove1() {
		assertTrue(list.remove(initialList.get(1)));
		assertFalse(list.contains(initialList.get(1)));

		assertTrue(list.contains(initialList.get(0)));
		assertTrue(list.contains(initialList.get(2)));
		assertTrue(list.size() == 2);
		assertEquals(initialList.get(0), list.getCurrent());
	}

	/**
	 * Special case, current element is deleted. The current element must become
	 * the previous next element.
	 * 
	 */
	public void testRemove2() {
		list.next();
		assertTrue(list.remove(initialList.get(1)));

		assertFalse(list.contains(initialList.get(1)));
		assertTrue(list.contains(initialList.get(0)));
		assertTrue(list.contains(initialList.get(2)));
		assertTrue(list.size() == 2);
		assertEquals(initialList.get(2), list.getCurrent());
	}

	/**
	 * Special case, an element with higher index than the current element is
	 * deleted.
	 * 
	 */
	public void testRemove3() {
		list.setCurrent(initialList.get(2));

		assertTrue(list.remove(initialList.get(1)));

		assertFalse(list.contains(initialList.get(1)));
		assertTrue(list.contains(initialList.get(0)));
		assertTrue(list.contains(initialList.get(2)));
		assertTrue(list.size() == 2);
		assertEquals(initialList.get(2), list.getCurrent());
	}

	/**
	 * Special case, the last element is deleted, and the last element is the
	 * current element. The current element should wrap back to the first
	 * element.
	 * 
	 */
	public void testRemove4() {
		list.setCurrent(initialList.get(2));

		assertTrue(list.remove(initialList.get(2)));

		assertFalse(list.contains(initialList.get(2)));
		assertTrue(list.contains(initialList.get(0)));
		assertTrue(list.contains(initialList.get(1)));
		assertTrue(list.size() == 2);
		assertEquals(initialList.get(0), list.getCurrent());
	}

}
