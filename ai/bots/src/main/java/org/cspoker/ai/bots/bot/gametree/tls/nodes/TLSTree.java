package org.cspoker.ai.bots.bot.gametree.tls.nodes;

import org.cspoker.common.elements.player.PlayerId;

public class TLSTree {
	
	public final PlayerId player;
	public final RootNode root;
	public final LeafNode parent;
	
	
	public TLSTree(PlayerId player, LeafNode parent){
		this.player = player;
		this.parent = parent;
		root = new RootNode(this);
	}

}
