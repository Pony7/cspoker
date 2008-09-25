package org.cspoker.server.rmi.listener;

import java.util.concurrent.Executor;

import org.cspoker.common.api.shared.ServerContext;

public class AsynchronousListener {

	protected final ServerContext serverContext;
	protected final Executor executor;

	public AsynchronousListener(ServerContext serverContext, Executor executor) {
		this.serverContext = serverContext;
		this.executor = executor;
	}
	
	public void die() {
		serverContext.die();
	}
	
}
