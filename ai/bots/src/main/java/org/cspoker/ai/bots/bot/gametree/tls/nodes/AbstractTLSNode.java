package org.cspoker.ai.bots.bot.gametree.tls.nodes;

public abstract class AbstractTLSNode {
	
	protected AbstractTLSNode leftChild;
	protected AbstractTLSNode rightChild;
	
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


}
