package org.cspoker.ai.bots.bot.gametree.tls.nodes;

public class LeafNode extends InnerNode {
	
	public TLSTree childTree;

	public LeafNode(AbstractTLSNode parent) {
		super(parent);
	}

	@Override
	public AbstractTLSNode selectChild() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void expand(){
		
	}

}
