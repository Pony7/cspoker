package org.cspoker.client.bots.bot.search.node;

import java.rmi.RemoteException;

import org.cspoker.client.bots.bot.search.action.SimulatedBotAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public interface IBotActionNode {

	void expand();

	double getEV();

	void performbestAction(RemoteHoldemPlayerContext context)
			throws RemoteException, IllegalActionException;

	void expandAction(SimulatedBotAction action);
}