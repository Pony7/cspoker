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
package org.cspoker.server.xml.http.authentication;

import org.apache.log4j.Logger;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;

import com.sun.net.httpserver.BasicAuthenticator;

public class XmlFileBasicAuthentication extends BasicAuthenticator {

	private static Logger logger = Logger
			.getLogger(XmlFileBasicAuthentication.class);
	private XmlFileAuthenticator file;

	public XmlFileBasicAuthentication(XmlFileAuthenticator file) {
		super("cspoker");
		this.file = file;
	}

	@Override
	public boolean checkCredentials(String user, String pass) {
		boolean ok = false;
		if (file.hasPassword(user, pass)) {
			XmlFileBasicAuthentication.logger.trace("Authentication for " + user
					+ " succeeded.");
			ok = true;
		} else {
			XmlFileBasicAuthentication.logger.info("Authentication for " + user
					+ " failed.");
		}
		return ok;
	}

}
