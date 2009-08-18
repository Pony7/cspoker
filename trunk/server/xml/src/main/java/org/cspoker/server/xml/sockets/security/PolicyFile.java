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
package org.cspoker.server.xml.sockets.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class PolicyFile {

	private static Logger logger = Logger.getLogger(PolicyFile.class);
	public static final String POLICY;
	public final static String request = "<policy-file-request";

	static {
		StringBuilder sb = new StringBuilder();
		;
		try {
			InputStream is = ClassLoader
					.getSystemClassLoader()
					.getResourceAsStream(
							"org/cspoker/server/xml/sockets/security/crossdomain.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			br.close();
			logger.info("Loaded policy file.");
		} catch (IOException e) {
			logger.error("Can't load policy file: " + e.getMessage(), e);
			e.printStackTrace();
			sb.setLength(0);
			sb.append("<cross-domain-policy/>");
		}
		POLICY = sb.toString();
	}

}
