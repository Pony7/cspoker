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
package api.httphandler.abstracts;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import api.httphandler.util.Base64;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class HttpHandlerImpl implements HttpHandler {

    public HttpHandlerImpl() {
	super();
    }
    
    protected void throwException(HttpExchange http, Exception e, int status){
	//local error msg
	System.out.println("Exception occured:");
	e.printStackTrace();
	//remote error msg
	try {
	    http.sendResponseHeaders(status, 0);
	} catch (IOException e1) {}
	e.printStackTrace(new PrintStream(http.getResponseBody()));
        try {
	    http.getResponseBody().close();
	} catch (IOException e1) {}
	http.close();
    }
    
    protected void throwException(HttpExchange http, Exception e){
	throwException(http, e, 500);
    }

    public static String toPlayerName(Headers requestHeaders) {
	List<String> auth=requestHeaders.get("Authorization");
	if(auth==null||auth.size()!=1)
	    throw new IllegalStateException("Incorrect Authorization");
	String base64=auth.get(0);
	try {
	    String decoded=new String(Base64.decode(base64.split(" ")[1]));
	    return decoded.split(":")[0];
	} catch (IOException e) {
	    throw new IllegalStateException(e);
	}
    }
    
    protected abstract int getDefaultStatusCode();

}