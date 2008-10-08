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
package org.cspoker.server.xml.http.handler.abstracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.cspoker.common.util.Base64;
import org.cspoker.common.util.Pair;
import org.cspoker.server.xml.http.handler.exception.HttpException;
import org.cspoker.server.xml.http.handler.exception.HttpExceptionImpl;
import org.xml.sax.helpers.AttributesImpl;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * A HttpHandler that supports Exceptions over Http.
 * 
 */
public abstract class AbstractHttpHandler implements HttpHandler {

	private final static Logger logger = Logger
			.getLogger(AbstractHttpHandler.class);

	public AbstractHttpHandler() {
		super();
	}

	public void handle(HttpExchange http) throws IOException {
		try {
			http.getResponseHeaders().add("Cache-Control", "no-cache");
			byte[] response = getResponse(http);

			// send the default status code (no exception occured)
			http.sendResponseHeaders(getDefaultStatusCode(), response.length);
			http.getResponseBody().write(response);
			http.getResponseBody().close();
			http.close();
		} catch (Exception e) {
			throwException(http, e);
		}

	}

	protected abstract byte[] getResponse(HttpExchange http)
			throws HttpExceptionImpl;

	/**
	 * Throws an exception over the Http connection in XML format.
	 * 
	 * @param http
	 *            The http context.
	 * @param e
	 *            The throwable to throw.
	 * @param status
	 *            The http error code to send.
	 * @throws IOException
	 *             An exception occured while creating the error response.
	 */
	protected void throwException(HttpExchange http, Throwable e, int status)
			throws IOException {
		try {
			logger.error("Returning serialized exception", e);
			if (e instanceof HttpException) {
				// e has http status information
				status = ((HttpException) e).getStatus();
			}
			if (e.getCause() != null) {
				// e is a wrapper class.
				e = e.getCause();
			}
			// remote error msg
			ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
			TransformerHandler response = null;
			StreamResult requestResult = new StreamResult(responseBody);

			SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
					.newInstance();
			response = tf.newTransformerHandler();
			response.setResult(requestResult);
			response.startDocument();
			response.startElement("", "exception", "exception",
					new AttributesImpl());

			response.startElement("", "msg", "msg", new AttributesImpl());
			String msg = (e.getMessage() == null ? "unknown error" : e
					.getMessage());
			response.characters(msg.toCharArray(), 0, msg.length());
			response.endElement("", "msg", "msg");

			response.startElement("", "stacktrace", "stacktrace",
					new AttributesImpl());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			String trace = sw.toString();
			response.characters(trace.toCharArray(), 0, trace.length());
			response.endElement("", "stacktrace", "stacktrace");

			response.endElement("", "exception", "exception");
			response.endDocument();

			http.sendResponseHeaders(status, responseBody.size());
			responseBody.writeTo(http.getResponseBody());
			http.getResponseBody().close();
			http.close();
		} catch (Exception e1) {
			logger.error(e1);
			throw new IOException(e1.getMessage());
		}
	}

	protected void throwException(HttpExchange http, Throwable e)
			throws IOException {
		throwException(http, e, 500);
	}

	public static Pair<String,String> getCredentials(Headers requestHeaders)
			throws HttpExceptionImpl {
		List<String> auth = requestHeaders.get("Authorization");
		if (auth == null || auth.size() != 1) {
			throw new HttpExceptionImpl(new IllegalArgumentException(
					"Incorrect Authorization"), 401);
		}
		String base64 = auth.get(0);
		try {
			String decoded = new String(Base64.decode(base64.split(" ")[1]));
			String[] parts = decoded.split(":");
			return new Pair<String, String>(parts[0],parts[1]);
		} catch (IOException e) {
			throw new HttpExceptionImpl(e, 401);
		}
	}

	protected int getDefaultStatusCode() {
		return 201;
	}

}