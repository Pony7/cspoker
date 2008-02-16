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
package org.cspoker.server.xml.sockets.runnables;

import java.io.StringReader;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;
import org.cspoker.server.common.threading.Prioritizable;
import org.cspoker.server.xml.sockets.ClientContext;
import org.cspoker.server.xml.sockets.security.PolicyFile;
import org.cspoker.server.xml.sockets.security.SocketsAuthenticator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProcessXML implements Runnable, Prioritizable{

    private final static Logger logger = Logger.getLogger(ProcessXML.class);
    public final static char DELIM = (char)0x00;

    private final String xml;
    private final ClientContext context;

    private final static SocketsAuthenticator auth = new SocketsAuthenticator(new XmlFileAuthenticator());

    public ProcessXML(String xml, ClientContext context) {
	this.xml=xml;
	this.context = context;
    }

    public void run() {
	logger.trace("recieved xml:\n"+xml);

	if(!context.isAuthenticated()){
	    //Flash client request for authorization to connect from a different host
	    if(xml.startsWith(PolicyFile.request)){
		logger.info("handling flash security manager request.");
		context.send(PolicyFile.POLICY+DELIM);
	    }else{
		//Check the credentials
		auth.authenticate(context, xml);
	    }
	}
	else{
	    //Perform the other requests
	    try {
		context.getXmlPlayerCommunication().handle(new InputSource(new StringReader(xml)));
	    } catch (SAXException e) {
		logger.error("Error parsing xml request. Closing connection.", e.getCause());
		context.closeConnection();
	    }
	}
    }

    public int getPriority() {
	return 1;
    }

}
