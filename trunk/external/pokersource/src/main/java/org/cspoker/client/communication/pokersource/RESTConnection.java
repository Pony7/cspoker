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
package org.cspoker.client.communication.pokersource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class RESTConnection {

	private final static Logger logger = Logger.getLogger(PokersourceConnection.class);
	
	static{
		if(CookieHandler.getDefault() == null){
			logger.info("Setting default cookie handler.");
			CookieManager manager = new CookieManager();
			CookieHandler.setDefault(manager);
			//ORIGINAL_DOMAIN doesn't work for some reason.
			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		}
	}

	private final URL url;

	public RESTConnection(String server) throws MalformedURLException {
		url = new URL(server);
	}

	protected String put(String content) throws IOException {
		HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
		urlc.setRequestMethod("PUT");
		urlc.setDoOutput(true);
		write(content, urlc.getOutputStream());
		return response(urlc);
	}


	private String response(HttpURLConnection urlc) throws IOException {
		try {
			InputStream is = urlc.getInputStream();
			return read(is);
		} catch (IOException e) {
			try {
				//int respCode = urlc.getResponseCode();
				InputStream es = urlc.getErrorStream();
				// read the response body
				throw new IOException(read(es), e);
			} catch(IOException ex) {
				// deal with the exception
				throw ex;
			}
		}
	}
	
	private void write(String content, OutputStream outputStream)
			throws IOException {
		Writer writer = new OutputStreamWriter(outputStream);
		try{
			writer.write(content);
		}finally{
			writer.close();
		}
	}

	private String read(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} finally {
			is.close();
		}
		return sb.toString();
	}

	public void close() throws IOException {
		// no op
	}
}

