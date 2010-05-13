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
package org.cspoker.ai.opponentmodels.weka;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;
import org.cspoker.ai.opponentmodels.OpponentModel;
import org.cspoker.ai.opponentmodels.weka.instances.InstancesBuilder;
import org.cspoker.ai.opponentmodels.weka.instances.PostCheckBetInstances;
import org.cspoker.ai.opponentmodels.weka.instances.PostFoldCallRaiseInstances;
import org.cspoker.ai.opponentmodels.weka.instances.PreCheckBetInstances;
import org.cspoker.ai.opponentmodels.weka.instances.PreFoldCallRaiseInstances;
import org.cspoker.ai.opponentmodels.weka.instances.ShowdownInstances;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

import weka.core.Instance;

public abstract class WekaModel implements OpponentModel{

	protected static final Logger logger = Logger.getLogger(WekaRegressionModel.class);
	
	protected PlayerTrackingVisitor visitor;
	private final Deque<PlayerTrackingVisitor> visitors = new ArrayDeque<PlayerTrackingVisitor>();
	
	private final PostCheckBetInstances postCheckBetInstance;
	private final PreCheckBetInstances preCheckBetInstance;
	private final PreFoldCallRaiseInstances preFoldCallRaiseInstance;
	private final PostFoldCallRaiseInstances postFoldCallRaiseInstance;
	private final ShowdownInstances showdownInstance;
	
	public WekaModel() {
		this.visitor = new PlayerTrackingVisitor();
		
		this.preCheckBetInstance = new PreCheckBetInstances("PreCheckBet", "@attribute prob real"+InstancesBuilder.nl);
		this.postCheckBetInstance = new PostCheckBetInstances("PostCheckBet", "@attribute prob real"+InstancesBuilder.nl);
		this.preFoldCallRaiseInstance = new PreFoldCallRaiseInstances("PreFoldCallRaise", "@attribute prob real"+InstancesBuilder.nl);
		this.postFoldCallRaiseInstance = new PostFoldCallRaiseInstances("PostFoldCallRaise", "@attribute prob real"+InstancesBuilder.nl);
		this.showdownInstance = new ShowdownInstances("Showdown", "@attribute prob real"+InstancesBuilder.nl);
	}

//	public long getVisitorSize() {
//		System.out.print("<" + visitors.size() + ">");
//		return visitors.size();
//	}
	
	@Override
	public void assumePermanently(GameState gameState) {
		visitor.readHistory(gameState);
	}

	@Override
	public void assumeTemporarily(GameState gameState) {
		PlayerTrackingVisitor root = getTopVisitor();
		PlayerTrackingVisitor clonedTopVisitor = root.clone();
		clonedTopVisitor.readHistory(gameState);
		visitors.push(clonedTopVisitor);
	}

	@Override
	public void forgetLastAssumption() {
		if(!visitors.isEmpty())
			visitors.pop();
	}

	protected PlayerTrackingVisitor getTopVisitor() {
		if(visitors.isEmpty()){
			return visitor;
		}
		return visitors.peek();
	}
	
	protected Instance getPreCheckBetInstance(PlayerId actor) {
		return preCheckBetInstance.getUnclassifiedInstance(getTopVisitor().getPropz(), actor);
	}

	protected Instance getPostCheckBetInstance(PlayerId actor) {
		return postCheckBetInstance.getUnclassifiedInstance(getTopVisitor().getPropz(), actor);
	}

	protected Instance getPostFoldCallRaiseInstance(PlayerId actor) {
		return postFoldCallRaiseInstance.getUnclassifiedInstance(getTopVisitor().getPropz(), actor);
	}

	protected Instance getPreFoldCallRaiseInstance(PlayerId actor) {
		return preFoldCallRaiseInstance.getUnclassifiedInstance(getTopVisitor().getPropz(), actor);
	}

	protected Instance getShowdownInstance(PlayerId actor) {
		return showdownInstance.getUnclassifiedInstance(getTopVisitor().getPropz(), actor);
	}

}