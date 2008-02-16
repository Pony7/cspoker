package org.cspoker.common;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteLoginServer extends Remote{

    public RemotePlayerCommunication login(String username, String password) throws RemoteException;
    
}
