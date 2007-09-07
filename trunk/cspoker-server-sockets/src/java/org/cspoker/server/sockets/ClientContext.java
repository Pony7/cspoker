package org.cspoker.server.sockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class ClientContext {

    private static Logger logger = Logger.getLogger(ClientContext.class);
    
    private final StringBuilder buffer;
    
    private volatile String useragent="unknown";
    private volatile String username="John Doe";
    private volatile String password="";
    
    private volatile boolean authenticated=false;
    
    private final List<ByteBuffer> writeBuffer = new ArrayList<ByteBuffer>();
    private final Object writeBufferLock = new Object();
    
    private final SocketChannel client;
    private final Selector selector;

    private final Charset charset;
    private final CharsetEncoder encoder;
    
    
    
    public ClientContext(SocketChannel client, Selector selector) {
	this.client = client;
	this.selector = selector;
	buffer = new StringBuilder();
	
	charset=Charset.forName("UTF-8");
	encoder = charset.newEncoder();
    }
    
    public StringBuilder getBuffer(){
	return buffer;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isAuthenticated(){
	return authenticated;
    }
    
    public void setAuthenticated(){
	authenticated=true;
    }
    
    /**
     * Writes the current buffer to the client. 
     * If the buffer is emptied, the selector is removed 
     * from the client.
     * @throws IOException 
     */
    public void writeBufferToClient() throws IOException{
	synchronized (writeBufferLock) {
	    Iterator<ByteBuffer> i = writeBuffer.iterator();
	    while(i.hasNext()){
		ByteBuffer bytes = i.next();
		logger.trace("trying to write "+bytes.remaining()+" bytes.");
		client.write(bytes);
		if(bytes.remaining()>0){
		    logger.trace("stopping write early because there are "+bytes.remaining()+" bytes unwritten.");
		    //registerWriteInterest(); //bug workaround?
		    return;
		}
		logger.trace("removing bytebuffer from the buffer list.");
		i.remove();
	    }
	    unregisterWriteInterest();
	    logger.trace("finished entire write operation");
	}
    }
    
    private void unregisterWriteInterest(){
	client.keyFor(selector).interestOps(SelectionKey.OP_READ);
	logger.trace("removed write interest");
	
    }
    
    private void registerWriteInterest(){
	client.keyFor(selector).interestOps(SelectionKey.OP_READ);
	client.keyFor(selector).interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }
    
    public void appendToWriteBuffer(ByteBuffer bytes){
	synchronized (writeBufferLock) {
	    writeBuffer.add(bytes);
	    logger.trace("adding write interest, added "+bytes.remaining()+" bytes to the buffer.");
	    registerWriteInterest();
	}
    }

    public void closeConnection() throws IOException {
	client.close();
    }
    
    public void send(String xml){
	try {
	    appendToWriteBuffer(encoder.encode(CharBuffer.wrap(xml+((char)0x00))));
	    logger.trace("wrote reply to write buffer list");
	} catch (CharacterCodingException e) {
	    logger.error(e.getMessage());
	    throw new IllegalStateException(e);
	}
    }
    
    
}
