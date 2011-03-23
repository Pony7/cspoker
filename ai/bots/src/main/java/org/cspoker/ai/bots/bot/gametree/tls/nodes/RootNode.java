package org.cspoker.ai.bots.bot.gametree.tls.nodes;

public class RootNode extends AbstractTLSNode {
	
	public final AbstractTree tree;

	public RootNode(AbstractTree tree) {
		super(null);
		this.tree = tree;
		this.leftChild = new LeafNode(this);
	}

	@Override
	public AbstractTLSNode selectChild() {
		// TODO Auto-generated method stub
		return null;
	}

}
