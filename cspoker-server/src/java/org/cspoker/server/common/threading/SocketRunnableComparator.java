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

package org.cspoker.server.common.threading;

import java.util.Comparator;

public class SocketRunnableComparator implements Comparator<Runnable> {

    /**
     * Note: this comparator imposes orderings that are inconsistent with equals.
     */
    public int compare(Runnable o1, Runnable o2) {
	int priority1=getPriority(o1);
	int priority2=getPriority(o2);
	//negative if 1 is less than 2; if 1 comes before 2; of 1 has a higher priority than 2
	return -priority1+priority2;
    }
    
    public int getPriority(Runnable o){
	if(o instanceof Prioritizable)
	    return ((Prioritizable)o).getPriority();
	return 0;
    }
}
