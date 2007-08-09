package api;

import com.sun.net.httpserver.BasicAuthenticator;


public class HardCodedBasicAuthentication extends BasicAuthenticator {

    public HardCodedBasicAuthentication() {
	super("/");
    }

    @Override
    public boolean checkCredentials(String user, String pass) {
	System.out.println("login "+(user.equalsIgnoreCase("guy") && pass.equalsIgnoreCase("test")));
	return user.equalsIgnoreCase("guy") && pass.equalsIgnoreCase("test");
    }


}
