package org.cspoker.ai.bots.bot.gametree.tls.nodes;

import org.cspoker.common.elements.player.PlayerId;

public abstract class AbstractTree {
	
	public final PlayerId player;
	public final RootNode root;
	public final LeafNode parent;
	
	
	public AbstractTree(PlayerId player, LeafNode parent){
		this.player = player;
		this.parent = parent;
		root = new RootNode(this);
	}

}
