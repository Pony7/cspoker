package org.cspoker.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

public interface RemoteLoginServer extends Remote {

	public RemotePlayerCommunication login(String username, String password)
			throws RemoteException, LoginException;

}
