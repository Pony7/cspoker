import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.xml.http.RemoteHTTPServer;

/**
 * @author stephans
 */
public class TestSWTwithHTTP
		extends TestSWTClient {
	
	protected TestSWTwithHTTP() throws Exception {
		super();
	}

	public static void main(String[] args) throws Exception {
		(new TestSWTwithHTTP()).testPlay();
	}
	
	/**
	 * @see TestSWTClient#setServer()
	 */
	@Override
	protected void setServer() {
		server = new RemoteHTTPServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_HTTP);
		
	}
	
}
