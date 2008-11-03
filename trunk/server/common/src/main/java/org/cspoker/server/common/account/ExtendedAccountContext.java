package org.cspoker.server.common.account;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.elements.player.MutablePlayer;

public interface ExtendedAccountContext extends AccountContext {
	
	MutablePlayer getPlayer();
	
}