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
package api;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {


    private LoginHandler loginHandler;
    private JoinTableHandler joinTableHandler;


    public RequestHandler() {
	    loginHandler = new LoginHandler();
	    joinTableHandler = new JoinTableHandler(loginHandler);
    }
    
    
    public void handle(HttpExchange http) throws IOException {
	System.out.println("Request recieved from " + http.getRemoteAddress());

	if (http.getRequestURI().getPath().equals("/cspoker/login")) {
	    http.sendResponseHeaders(200, 0);
	    loginHandler.handle(http.getRequestBody(), http.getResponseBody());
	}else if(http.getRequestURI().getPath().equals("/cspoker/jointable")) {
	    http.sendResponseHeaders(200, 0);
	    joinTableHandler.handle(http.getRequestBody(), http.getResponseBody());
	}else{	
	    //Send code for "Bad Request"
	    http.sendResponseHeaders(400, 0);
	}
	http.getResponseBody().flush();
	http.close();
    }

}
