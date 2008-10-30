import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.xml.http.RemoteHTTPServer;

/**
 * @author stephans
 */
public class TestSWTwithHTTP
		extends TestSWTClient {
	
	/**
	 * @see TestSWTClient#setServer()
	 */
	@Override
	protected void setServer() {
		server = new RemoteHTTPServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_HTTP);
		
	}
	
}
