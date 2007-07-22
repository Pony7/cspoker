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
package xmlrpc;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * Creates a new XMLRPC server and starts it. The webservice is the public interface
 * defined by the <code>WebService</code> class.
 */
public class Server {

    /**
     * Creates and starts the server.
     * @param args
     * 	      The arguments. Must be only the port number.
     * @throws NumberFormatException
     * 	       The port argument was not a number.
     * @throws XmlRpcException
     *         The XmlRpcServer could not be created.
     * @throws IOException
     *         There was a problem opening the given port.
     * 	       
     */
    public static void main(String[] args) throws NumberFormatException, XmlRpcException, IOException {
	if(args.length!=1){
	    System.out.println("usage: java -jar cspoker-server.jar [portnumber]");
	    System.exit(0);
	}
	Server server = new Server(Integer.parseInt(args[0]));
	
	server.start();
	
    }
    
    /**
     * Variable holding the webserver object.
     */
    private WebServer webServer;
    
    /**
     * Creates a new server at the given port.
     * @param port
     * 	      The port to listen at.
     * @throws XmlRpcException
     *         The XmlRpcServer could not be created.
     */
    public Server(int port) throws XmlRpcException {
	webServer = new WebServer(port);
        
        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
        
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
	phm.addHandler("CSPoker", xmlrpc.WebService.class);
        
	System.out.println(phm.getListMethods()[0]);
	System.out.println(phm.getListMethods()[1]);
	
        xmlRpcServer.setHandlerMapping(phm);
        
        XmlRpcServerConfigImpl serverConfig =
            (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(true);
        //enabling keepalive also enables some bug in xmlrpc!
        //but without it exceptions are not propagated correctly...
	serverConfig.setKeepAliveEnabled(true);
        System.out.println("XmlRpcServer created for port "+port+".");
    }
    
    /**
     * Starts this server.
     * @throws IOException
     *         There was a problem opening this server's port.
     */
    public void start() throws IOException {
	webServer.start();
	System.out.println("XmlRpcServer started.");
    }
    
}
