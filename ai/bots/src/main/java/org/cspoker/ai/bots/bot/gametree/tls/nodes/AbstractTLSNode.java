package org.cspoker.ai.bots.bot.gametree.tls.nodes;

import java.util.ArrayList;
import java.util.List;

import org.cspoker.ai.bots.bot.gametree.action.SearchBotAction;
import org.cspoker.ai.bots.bot.gametree.tls.SimulatedGame;
import org.cspoker.ai.bots.bot.gametree.tls.tests.Test;

public abstract class AbstractTLSNode {
	
	protected AbstractTLSNode leftChild;
	protected AbstractTLSNode rightChild;
	
	private List<Test> possibleTests = new ArrayList<Test>();
	
	public AbstractTLSNode(AbstractTLSNode parent){
		this.parent = parent;
	}
	
	private final AbstractTLSNode parent;
	
	public AbstractTLSNode getLeftChild() {
		return leftChild;
	}
	
	public AbstractTLSNode getRightChild() {
		return rightChild;
	}
	
	public abstract AbstractTLSNode selectChild();

	public AbstractTLSNode getParent() {
		return parent;
	}
	
	public void backPropagate(double value, SimulatedGame game){
		SearchBotAction action = game.pop();
		if(!isSplit()){
			for (Test test : possibleTests) {
				test.updateStats(action, value);
			}
		}
	}

	protected boolean isSplit(){
		return rightChild != null;
	}
	
	public void split(){
		leftChild = new LeafNode(getParent());
		rightChild = new LeafNode(getParent());
	}

}
