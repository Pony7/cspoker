import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.xml.sockets.RemoteSocketServer;

/**
 * @author stephans
 */
public class TestSWTwithSockets
		extends TestSWTClient {
	
	/**
	 * @see TestSWTClient#setServer()
	 */
	@Override
	protected void setServer() {
		server = new RemoteSocketServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_SOCKET);
		
	}
	
}
