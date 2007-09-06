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
import org.cspoker.server.sockets.threading.LoggingThreadPool;
import org.cspoker.server.sockets.threading.Prioritizable;
import org.cspoker.server.sockets.threading.SocketRunnableComparator;
import org.xml.sax.SAXException;

public class SocketServer 
{

    public static void main(String args[]) throws IOException, SAXException{
	new SocketServer();
    }
    
    private static Logger logger = Logger.getLogger(SocketServer.class);

    public final static int bufferSize = 1024;

    private final ServerSocketChannel server;

    private final Selector selector;

    private final ByteBuffer buffer;
    private final ByteBuffer filteredBuffer;

    private final ThreadPoolExecutor executor;

    public SocketServer() throws IOException {
	// Create the server socket channel
	server = ServerSocketChannel.open();
	// nonblocking I/O
	server.configureBlocking(false);
	// host-port 8000
	server.socket().bind(new java.net.InetSocketAddress(8000));
	logger.info("Server running on port 8000");
	// Create the selector
	selector = Selector.open();
	// Recording server to selector (type OP_ACCEPT)
	server.register(selector,SelectionKey.OP_ACCEPT);
	// Create a direct buffer to get bytes from socket.
	// Direct buffers should be long-lived and be reused as much as possible.
	buffer = ByteBuffer.allocateDirect(bufferSize);
	filteredBuffer = ByteBuffer.allocateDirect(bufferSize);

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
	
	public ProcessXML(String xml) {
	    this.xml=xml;
	}

	public void run() {
	    System.out.println("got xml:"+xml);
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
		SelectionKey key = i.next();

		// Remove the current key
		i.remove();

		if (key.isAcceptable()) {
		    acceptConnection();
		}
		else if (key.isReadable()) {
		    readSocket(key);
		}

	    }
	} catch (IOException e) {
	    //no op
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
	    //client.close();
	} else {
	    // To read the bytes, flip the buffer
	    buffer.flip();
	    ClientContext context = (ClientContext)(key.attachment());
	    if(context==null){
		context = new ClientContext();
		key.attach(context);
	    }
	    StringBuilder stringBuilder = context.getBuffer();

	    while (buffer.hasRemaining()) {
		boolean hasEnded = filterUntilEndNode();

		Charset charset=Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer decoded = decoder.decode(filteredBuffer);
		stringBuilder.append(decoded);
		if(hasEnded){
		    endNode(stringBuilder);
		}
	    }
	}

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
	logger.debug("accepted new connection");
    }

    private void endNode(StringBuilder stringBuilder){
	executor.submit(new ProcessXML(stringBuilder.toString()));
	stringBuilder.setLength(0);
	logger.debug("ended an xml node");
    }

}