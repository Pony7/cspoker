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
package org.cspoker.server.xml.http.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CrossDomain implements HttpHandler {

	private final static Logger logger = Logger.getLogger(CrossDomain.class);

	public void handle(HttpExchange http) throws IOException {

		logger.trace("Received root request");

		if (http.getRequestURI().getPath() != null
				&& http.getRequestURI().getPath().endsWith("crossdomain.xml")) {
			logger.trace("Received request for crossdomain.xml");
			byte[] result = "<?xml version=\"1.0\"?><!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\"><cross-domain-policy><allow-access-from domain=\"*\" /></cross-domain-policy>"
					.getBytes();
			http.sendResponseHeaders(200, result.length);
			http.getResponseBody().write(result);
			http.getResponseBody().flush();
			http.getResponseBody().close();
		} else {
			logger.trace("404: Page not found");
			byte[] result = "Page not found.".getBytes();
			http.sendResponseHeaders(404, result.length);
			http.getResponseBody().write(result);

			http.getResponseBody().flush();
			http.getResponseBody().close();
		}
	}

}
