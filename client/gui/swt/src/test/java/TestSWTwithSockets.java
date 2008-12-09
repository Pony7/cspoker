import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.xml.sockets.RemoteSocketServer;

/**
 * @author stephans
 */
public class TestSWTwithSockets
		extends TestSWTClient {
	
	protected TestSWTwithSockets() throws Exception {
		super();
	}

	public static void main(String[] args) throws Exception {
		(new TestSWTwithSockets()).testPlay();
	}

	/**
	 * @see TestSWTClient#setServer()
	 */
	@Override
	protected void setServer() {
		server = new RemoteSocketServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_SOCKET);
		
	}
	
}
