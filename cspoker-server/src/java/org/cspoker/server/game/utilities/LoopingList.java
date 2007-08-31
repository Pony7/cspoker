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

package org.cspoker.server.game.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to represent a looping list.
 * If the end of the list is reached, it wraps back to the first element.
 * 
 * @author Kenzo
 *
 * @param <T> The type of the elements in this looping list.
 * 
 * @invar Every element in the looping list is unique.
 * 
 * @note This implementation is not thread-safe.
 */
public class LoopingList<T> {
	
	/**
	 * The internal representation of the looping list.
	 */
	private final List<T> list = new ArrayList<T>();
	
	/**
	 * The position of the current element.
	 */
	private volatile int currentPosition;
	
	/**
	 * Construct a new looping list from given list.
	 * There is no dependence between the given list
	 * and this looping list.
	 * 
	 * @param 	list
	 * 			The list to make a looping list with.
	 * @pre 	The given list should be effective.
	 *			|list!=null
	 * @pre 	The list should contain at least 1 element.
	 *			|list.size()>0
	 * @pre 	Every element in the given list should be unique.
	 * @post 	The first element of the given list is initialized as
	 * 			the first element of the looping list.
	 * 			|list.get(0)==new.getCurrent()
	 * @post 	The size of this looping list is the same as the given list.
	 *		 	|list.size()==new.size()
	 */
	public LoopingList(List<T> list){
		this.list.addAll(list);
		currentPosition = 0;
	}
	
	/**
	 * Set the current element to the next element in the looping list.
	 * 
	 */
	public void next(){
		currentPosition = (currentPosition+1)%(size());
	}
	
	/**
	 * Set the current element to the previous element in the looping list.
	 *
	 */
	public void goBack(){
		currentPosition = (currentPosition+size()-1)%(size());
	}
	
	/**
	 * Returns the current element in the looping list.
	 * 
	 * @return The current element in the looping list.
	 * 
	 */
	public T getCurrent(){
		return list.get(currentPosition);
	}
	
	/**
	 * Set the current element to the given element.
	 * 
	 * @param 	o
	 * 			The element to set the current element to.
	 * @post 	If the given element is an element of this looping list,
	 * 			the current element is set to the given element,
	 * 			else the current element remains the same.
	 *		 	|if(contains(o))
	 *			|	then o==null ? new.getCurrent()==null : new.getCurrent().equals(o)
	 *			|	else getCurrent()==new.getCurrent()
	 */
	public void setCurrent(T o){
		int index = list.indexOf(o);
		if(index>=0){
			currentPosition = index;
		}
	}
	
	/**
	 * Returns the next element of this looping list.
	 * 
	 * @return The next element of this looping list.
	 */
	public T getNext(){
		return list.get((currentPosition+1)%size());
	}
	
	/**
	 * Returns the previous element of this looping list.
	 * 
	 * @return The previous element of this looping list.
	 */
	public T getPrevious(){
		return list.get((currentPosition+size()-1)%size());
	}
	
	/**
	 * Returns the element next to the given element
	 * in this looping list.
	 * 
	 * @param 	o
	 * 			The element for which the next element
	 * 			should be returned.
	 * @return	The element next to the given element
	 * 			in this looping list.
	 * @return	Null, if the given element is not an element
	 * 			of this looping list. 
	 */
	public T getNextTo(T o){
		int index = list.indexOf(o);
		if(index>=0)
			return list.get((index+1)%size());
		return null;
	}
	
	/**
	 * Remove the occurrence of the given element of the list.
	 * 
	 * After removal, there are no instances present of the given element,
	 * because the class-invariance imposes no duplicate elements.
	 * 
	 * @param 	o
	 * 			Element to be removed from this list, if present.
	 * @return	True, if this list contained the specified element,
	 * 			False otherwise.
	 * 			| result == contains(o)
	 * @post 	If the given element is present in this looping list,
	 * 			the given element is removed from this list.
	 * 			After deletion, whether the given element is contained or not, 
	 * 			there is no more instance of the given element present in the list.
	 *		 	|!new.contains(o)
	 * @post 	If the current element is removed, the current element
	 * 			will be set to the next element in the looping list.
	 */
	public boolean remove(T o){
		if(!contains(o))
			return false;
		if(currentPosition>list.indexOf(o)){
			currentPosition--;
		}
		list.remove(o);
		if(size()!=0){
			currentPosition = currentPosition%(size());
		}else{
			currentPosition=0;
		}
		return true;
	}

	/**
	 * Returns the number of elements in this list.
	 * If this list contains more than Integer.MAX_VALUE elements,
	 * returns Integer.MAX_VALUE.
	 * 
	 * @return The number of elements in this list.
	 */
	public int size(){
		return list.size();
	}
	
	/**
	 * Check whether this looping list contains the given element.
	 * 
	 * @param 	o
	 * 			Element whose presence in this list is to be tested.
	 * @return 	Returns true if this list contains the specified element.
	 * 			More formally, returns true if and only if this list contains
	 * 			at least one element e such that (o==null ? e==null : o.equals(e)). 
	 */
	public boolean contains(T o){
		return list.contains(o);
	}
	
	/**
	 * Returns an unmodifiable list,
	 * containing all elements of this looping list.
	 * 
	 * The current element of the looping list
	 * does not necessarily correspond to the
	 * first element of the returned list.
	 * 
	 * @return A list containing all elements in the looping list.
	 * 
	 */
	public List<T> getList(){
		return Collections.unmodifiableList(list);
	}
}
