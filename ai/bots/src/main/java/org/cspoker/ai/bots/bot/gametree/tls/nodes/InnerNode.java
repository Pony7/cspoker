package org.cspoker.ai.bots.bot.gametree.tls.nodes;

public class InnerNode extends AbstractTLSNode {

	public InnerNode(AbstractTLSNode parent) {
		super(parent);
	}

	@Override
	public AbstractTLSNode selectChild() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void split(){
		getParent().leftChild = new LeafNode(getParent());
		getParent().rightChild = new LeafNode(getParent());
	}

}
