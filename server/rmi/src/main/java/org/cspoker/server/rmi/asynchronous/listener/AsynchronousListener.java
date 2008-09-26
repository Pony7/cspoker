package org.cspoker.server.rmi.asynchronous.listener;

import java.util.concurrent.Executor;

import org.cspoker.common.api.shared.Killable;

public class AsynchronousListener implements Killable{

	protected final Killable connection;
	protected final Executor executor;

	public AsynchronousListener(Killable connection, Executor executor) {
		this.connection = connection;
		this.executor = executor;
	}
	
	public void die() {
		connection.die();
	}
	
}
