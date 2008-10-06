package org.cspoker.server.common;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.server.common.player.ServerPlayer;

public interface ExtendedAccountContext extends AccountContext {
	
	ServerPlayer getPlayer();
	
}