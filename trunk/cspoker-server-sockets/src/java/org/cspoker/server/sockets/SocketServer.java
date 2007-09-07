/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.sockets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.sockets.threading.LoggingThreadPool;
import org.cspoker.server.sockets.threading.Prioritizable;
import org.cspoker.server.sockets.threading.SocketRunnableComparator;
import org.cspoker.server.utils.Log4JPropertiesLoader;

public class SocketServer 
{

    public static void main(String[] args) throws NumberFormatException, IOException {
	
	Log4JPropertiesLoader.load("org/cspoker/server/sockets/logging/log4j.properties");
	
	if (args.length < 1) {
	    usage();
	}
	
	int port=0;
	try {
	    port=Integer.parseInt(args[0]);
	} catch (NumberFormatException e) {
	    usage();
	}

	SocketServer server = new SocketServer(port);
    }

    private static void usage() {
	logger.fatal("usage: java -jar cspoker-server-sockets.jar [portnumber]");
	System.exit(0);
    }

    private static Logger logger = Logger.getLogger(SocketServer.class);

    public final static int bufferSize = 1024;

    private final ServerSocketChannel server;

    private final Selector selector;

    private final ByteBuffer buffer;
    private final ByteBuffer filteredBuffer;

    private final ThreadPoolExecutor executor;

    private final Charset charset;
    private final CharsetDecoder decoder;

    private final Authenticator auth;

    public SocketServer(int port) throws IOException {

	// Create the server socket channel
	server = ServerSocketChannel.open();
	// nonblocking I/O
	server.configureBlocking(false);
	// host-port 8000
	server.socket().bind(new java.net.InetSocketAddress(port));
	logger.info("Server running on port "+port);
	// Create the selector
	selector = Selector.open();
	// Recording server to selector (type OP_ACCEPT)
	server.register(selector,SelectionKey.OP_ACCEPT);
	// Create a direct buffer to get bytes from socket.
	// Direct buffers should be long-lived and be reused as much as possible.
	buffer = ByteBuffer.allocateDirect(bufferSize);
	filteredBuffer = ByteBuffer.allocateDirect(bufferSize);

	charset=Charset.forName("UTF-8");
	decoder = charset.newDecoder();

	this.auth = new Authenticator(new XmlFileAuthenticator());

	executor = new LoggingThreadPool(
		1,
		Runtime.getRuntime().availableProcessors()+1, 
		1, TimeUnit.SECONDS,
		new PriorityBlockingQueue<Runnable>(1000, new SocketRunnableComparator()),
		"TestServer"
	);
	executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

	// Infinite server loop
	executor.submit(new WaitForIO());
    }

    private class WaitForIO implements Runnable, Prioritizable{

	public void run() {
	    waitForWork();
	    executor.submit(new WaitForIO());
	}

	public int getPriority() {
	    return -1;
	}
    }

    private class ProcessXML implements Runnable, Prioritizable{

	private String xml;
	private ClientContext context;

	public ProcessXML(String xml, ClientContext context) {
	    this.xml=xml;
	    this.context = context;
	}

	public void run() {
	    System.out.println("got xml:"+xml);
	    if(!context.isAuthenticated()){
		if(!auth.authenticate(context, xml)){
		    try {
			context.closeConnection();
		    } catch (IOException e) {
			logger.error("can't close socket: "+e.getMessage());
		    }
		}else{
		    context.send("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<login/>");
		}
	    }else{
		context.send("recieved: "+xml);
	    }
	}

	public int getPriority() {
	    return 1;
	}

    }

    private void waitForWork(){
	// Waiting for events
	try {
	    selector.select();
	    // Get keys
	    Set<SelectionKey> keys = selector.selectedKeys();
	    Iterator<SelectionKey> i = keys.iterator();

	    // For each keys...
	    while(i.hasNext()) {
		SelectionKey key = (SelectionKey)i.next();

		int kro = key.readyOps();


		if((kro & SelectionKey.OP_READ) == SelectionKey.OP_READ){
		    readSocket(key);
		    logger.trace("read from socket");
		}
		if((kro & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE){
		    getContext(key, (SocketChannel) key.channel()).writeBufferToClient();
		    logger.trace("wrote data to socket");
		}
		if((kro & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
		    acceptConnection();
		    logger.trace("accepted new connection");
		}
		i.remove();			// remove the key

	    }
	} catch (IOException e) {
	    //no op
	    logger.error(e.getMessage());
	    e.printStackTrace();
	}


    }

    private void readSocket(SelectionKey key) throws IOException {
	SocketChannel client = (SocketChannel) key.channel();

	// Clear the buffer and read bytes from socket
	buffer.clear();
	int numBytesRead = client.read(buffer);

	if (numBytesRead == -1) {
	    // No more bytes can be read from the channel
	    client.close();
	} else {
	    // To read the bytes, flip the buffer
	    buffer.flip();
	    ClientContext context = getContext(key, client);
	    StringBuilder stringBuilder = context.getBuffer();

	    while (buffer.hasRemaining()) {
		boolean hasEnded = filterUntilEndNode();

		CharBuffer decoded = decoder.decode(filteredBuffer);
		stringBuilder.append(decoded);
		if(hasEnded){
		    endNode(stringBuilder, context);
		}
	    }
	}

    }

    private ClientContext getContext(SelectionKey key, SocketChannel client) {
	ClientContext context = (ClientContext)(key.attachment());
	if(context==null){
	    context = new ClientContext(client, selector);
	    key.attach(context);
	}
	return context;

    }

    private boolean filterUntilEndNode(){
	filteredBuffer.clear();
	while (buffer.hasRemaining()) {
	    byte b = buffer.get();
	    if(b==0){
		filteredBuffer.flip();
		return true;
	    }else{
		filteredBuffer.put(b);
	    }

	}
	filteredBuffer.flip();
	return false;
    }

    private void acceptConnection() throws IOException{
	// get client socket channel
	SocketChannel client = server.accept();
	// Non Blocking I/O
	client.configureBlocking(false);
	// recording to the selector (reading)
	client.register(selector, SelectionKey.OP_READ);
    }

    private void endNode(StringBuilder stringBuilder, ClientContext context){
	executor.submit(new ProcessXML(stringBuilder.toString(), context));
	stringBuilder.setLength(0);
	logger.debug("ended an xml node");
    }

}