package org.cspoker.common.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.game.RemotePlayerCommunication;

public interface RemoteRMIServer extends Remote{

    public RemotePlayerCommunication login(String username, String password) throws RemoteException;
    
}
