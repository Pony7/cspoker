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

package org.cspoker.server.common.game.elements.chips;

import org.apache.log4j.Logger;

/**
 * A class to represent chips.
 * 
 * @author Kenzo
 * 
 * @invar The value of these chips should be valid. |canHaveAsValue(getValue())
 * 
 * @note The case of overflow should also be handled.
 * 
 */
public class Chips {

	private static Logger logger = Logger.getLogger(Chips.class);

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new chips pile with given initial value.
	 * 
	 * @param initalValue
	 *            The initial value for this new chips pile.
	 * @effect Set the value of these chips to the given value.
	 *         |setValue(initalValue)
	 * @throws IllegalValueException
	 *             [must] The given initial value is not a legal value for the
	 *             chips. | !canHaveAsValue(initalValue)
	 */
	public Chips(int initalValue) throws IllegalValueException {
		setValue(initalValue);
	}

	/**
	 * Construct a new chips pile with 0 as initial value.
	 * 
	 * @effect Set the value of these chips to 0. |setValue(0)
	 */
	public Chips() {

		try {
			setValue(0);
		} catch (IllegalValueException e) {
			// 0 is always a valid value.
			assert false;
		}
	}

	/***************************************************************************
	 * Value
	 **************************************************************************/

	/**
	 * This variable contains the value of the chips.
	 */
	private int value;

	/**
	 * Return the value of these chips.
	 * 
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Check whether chips can have the given value as their value.
	 * 
	 * @param value
	 *            The value to check.
	 * @return The value should be positive. | value>=0
	 */
	public static boolean canHaveAsValue(int value) {
		return value >= 0;
	}

	/**
	 * Set the value of this chips to the given value.
	 * 
	 * @param value
	 *            The new value for this chips.
	 * @post The value of this chips is set to the given value. | new.getValue() ==
	 *       value
	 * @throws IllegalValueException
	 *             [must] The given value is not a legal value for the chips. |
	 *             !canHaveAsValue(value)
	 */
	private void setValue(int value) throws IllegalValueException {
		if (!canHaveAsValue(value)) {
			throw new IllegalValueException();
		}
		this.value = value;
	}

	/***************************************************************************
	 * Mutators
	 **************************************************************************/

	/**
	 * Increase the value of the pile of chips with given amount.
	 * 
	 * @param amount
	 *            The amount to increase the value of the pile of chips with.
	 * @pre The amount should be positive. |amount>=0
	 * @effect Set the value of the pile of chips to the value increased with
	 *         given amount. |setValue(getValue()+amount)
	 * @throws IllegalValueException
	 *             [must] The new value is not a legal value for the chips. |
	 *             !canHaveAsValue(new.getValue())
	 */
	private void increaseWith(int amount) throws IllegalValueException {
		if (amount < 0) {
			throw new IllegalArgumentException();
		}
		setValue(getValue() + amount);
	}

	/**
	 * Decrease the value of the pile of chips with given amount.
	 * 
	 * @param amount
	 *            The amount to decrease the value of the pile of chips with.
	 * @pre The amount should be positive. |amount>=0
	 * @effect Set the value of the pile of chips to the value decreased with
	 *         given amount. |setValue(getValue()-amount)
	 * @throws IllegalValueException
	 *             [must] The new value is not a legal value for the chips. |
	 *             !canHaveAsValue(new.getValue())
	 */
	private void decreaseWith(int amount) throws IllegalValueException {
		if (amount < 0) {
			throw new IllegalArgumentException();
		}
		setValue(getValue() - amount);
	}

	/**
	 * Transfer the given amount from this pile of chips to the given pile of
	 * chips.
	 * 
	 * @param amount
	 *            The amount to transfer.
	 * @param receiver
	 *            The pile of chips to transfer the amount of chips to.
	 * @pre The receiver should be effective. | receiver!=null
	 * @pre The amount should be positive. | amount>=0
	 * @effect Decrease this pile with given amount. |decreaseWith(amount)
	 * @effect Increase the receiving pile with given amount.
	 *         |receiver.increaseWith(amount)
	 * @throws IllegalValueException
	 *             [must] This pile of chips can not have the new value as its
	 *             value. | !canHaveAsValue(new.getValue())
	 * @throws IllegalValueException
	 *             [must] This given pile of chips can not have the new value as
	 *             its value. | !canHaveAsValue(new.receiver.getValue())
	 * 
	 * @note This method should be synchronized because it is an atomic
	 *       operation. Otherwise there is the risk that the invariants will not
	 *       hold after exiting the method.
	 */
	public synchronized void transferAmountTo(int amount, Chips receiver)
			throws IllegalValueException {
		if (receiver == null) {
			throw new IllegalArgumentException(
					"The given receiver should be effective.");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Can not transfer " + amount
					+ " from this pile with " + toString() + " to "
					+ receiver.toString());
		}
		
		decreaseWith(amount);
		receiver.increaseWith(amount);
	}

	/**
	 * Transfer the all chips from this pile to the given pile of chips.
	 * 
	 * @param receiver
	 *            The pile of chips to transfer all the chips to.
	 * @effect All the chips of this pile are transfered to the given pile of
	 *         chips. |transferAmountTo(getValue(), receiver)
	 */
	public synchronized void transferAllChipsTo(Chips receiver) {
		try {
			transferAmountTo(getValue(), receiver);
		} catch (IllegalValueException e) {
			logger.error(e);
			assert false;
		}
	}

	public synchronized Chips getCopy() {
		try {
			return new Chips(value);
		} catch (IllegalValueException e) {
			logger.error(e);
			throw new IllegalStateException(
					"Class-invariant is not respected. Chips must have valid values.");
		}
	}

	public String toString() {
		return Integer.valueOf(getValue()).toString();
	}

}
