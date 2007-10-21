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
package org.cspoker.client.sockets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.cspoker.client.sockets.exceptions.ConnectionLostException;
import org.cspoker.client.sockets.exceptions.LoginFailedException;
import org.cspoker.common.xmlcommunication.XmlEventCollector;

public class XmlSocketsChannel {

    private final static Logger logger = Logger.getLogger(XmlSocketsChannel.class);

    public final static byte DELIMITER = 0x00;
    public final static char[] DELIMITER_ARRAY = new char[]{(char)DELIMITER};

    private final Socket s;
    private final Writer w;

    private XmlEventCollector collector;
    private final ExecutorService executor;

    private CharsetDecoder decoder;


    public XmlSocketsChannel(String server, int port, String username, String password
	    , XmlEventCollector collector) throws UnknownHostException, IOException, LoginFailedException {
	this.s=new Socket(server, port);
	this.w=new OutputStreamWriter(s.getOutputStream());
	this.collector = collector;

	Charset charset=Charset.forName("UTF-8");
	decoder = charset.newDecoder();

	if(!login(username, password))
	    throw new LoginFailedException();
	executor = Executors.newSingleThreadExecutor();
	executor.execute(new WaitForEvents());
    }

    private boolean login(String username, String password) throws IOException{
	w.write("<login username='"+username+"' password='"+password+"' useragent='sockets client'/>");
	try {
	    return readUntilDelimiter().contains("<login");
	} catch (ConnectionLostException e) {
	    logger.error("Connection lost during login.",e);
	} catch (InterruptedException e) {
	    //no op
	}
	return false;
    }

    public void sendXML(final String xml) throws IOException{
	w.write(xml);
	w.write(DELIMITER_ARRAY);
    }

    private String readUntilDelimiter() throws IOException, ConnectionLostException, InterruptedException{
	StringBuilder sb = new StringBuilder();
	ByteBuffer singleByteBuffer = ByteBuffer.allocateDirect(1);
	while(true){
	    int b = s.getInputStream().read();
	    if(Thread.interrupted())
		throw new InterruptedException();
	    if(b<0)
		throw new ConnectionLostException();
	    if(b==DELIMITER)
		return sb.toString();
	    else{
		singleByteBuffer.put((byte)b);
		CharBuffer decoded = decoder.decode(singleByteBuffer);
		sb.append(decoded);
		singleByteBuffer.clear();
	    }
	}
    }

    private AtomicBoolean closed = new AtomicBoolean(false);

    public void close(){
	if(!closed.getAndSet(true)){
	    executor.shutdownNow();
	    try {
		w.close();
	    } catch (IOException e) {
		logger.error(e);
	    }
	    try {
		s.close();
	    } catch (IOException e) {
		logger.error(e); 
	    }
	}
    }

    public class WaitForEvents implements Runnable {

	public void run() {
	    try {
		String s = readUntilDelimiter();
		collector.collect(s);
	    } catch (IOException e) {
		close();
	    } catch (ConnectionLostException e) {
		close();
	    } catch (InterruptedException e) {
		Thread.currentThread().interrupt();
	    }
	}

    }


}
