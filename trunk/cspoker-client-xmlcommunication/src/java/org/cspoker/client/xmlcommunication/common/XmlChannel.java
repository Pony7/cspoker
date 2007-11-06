package org.cspoker.client.xmlcommunication.common;

import java.io.IOException;

import org.cspoker.client.xmlcommunication.sockets.exceptions.LoginFailedException;
import org.cspoker.common.xmlcommunication.XmlEventListener;

public interface XmlChannel {

    public void open() throws IOException, LoginFailedException;
    
    public void send(final String xml) throws IOException, LoginFailedException;
    
    public void registerXmlEventListener(XmlEventListener listener);
    
    public void unRegisterXmlEventListener(XmlEventListener listener);
    
    public void close();
    
}
