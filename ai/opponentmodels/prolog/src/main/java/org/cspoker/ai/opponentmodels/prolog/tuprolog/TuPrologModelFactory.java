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
package org.cspoker.ai.opponentmodels.prolog.tuprolog;

import java.io.IOException;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.ai.opponentmodels.OpponentModel;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

@ThreadSafe
public class TuPrologModelFactory implements OpponentModel.Factory {

	@Override
	public OpponentModel create() {
		Prolog engine = new Prolog();
		try {
			Theory theory1 = new Theory(
					this
							.getClass()
							.getClassLoader()
							.getResourceAsStream(
									"org/cspoker/client/bots/bot/search/opponentmodel/prolog/tuprolog/theory.pl"));
			engine.setTheory(theory1);
		} catch (IOException e1) {
			throw new IllegalStateException(e1);
		} catch (InvalidTheoryException e2) {
			throw new IllegalStateException(e2);
		}
		return new TuPrologModel(engine);
	}
			
	@Override
	public String toString() {
		return "TuPrologModel";
	}
}
