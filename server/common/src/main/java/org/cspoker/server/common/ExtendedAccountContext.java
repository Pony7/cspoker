package org.cspoker.server.common;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.elements.player.Player;

public interface ExtendedAccountContext extends AccountContext {
	
	Player getPlayer();
	
}