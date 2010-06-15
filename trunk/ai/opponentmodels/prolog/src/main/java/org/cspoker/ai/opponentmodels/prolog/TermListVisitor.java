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
package org.cspoker.ai.opponentmodels.prolog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;

public class TermListVisitor extends ToPrologTermVisitor {

	private final List<StructureTerm> terms = new ArrayList<StructureTerm>();

	public TermListVisitor() {
		super();
	}

	public TermListVisitor(LoggingVisitor root) {
		super(root);
	}

	@Override
	protected void addTerm(StructureTerm term) {
		terms.add(term);
	}

	public List<StructureTerm> getTerms() {
		return Collections.unmodifiableList(terms);
	}
}
