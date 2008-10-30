import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.rmi.RemoteRMIServer;

/**
 * @author stephans
 */
public class TestSWTwithRMI
		extends TestSWTClient {
	
	/**
	 * @see TestSWTClient#setServer()
	 */
	@Override
	protected void setServer() {
		server = new RemoteRMIServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_RMI);
		
	}
	
}
