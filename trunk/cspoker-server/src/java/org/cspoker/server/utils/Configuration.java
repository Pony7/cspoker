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
package org.cspoker.server.utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Craig Motlin
 * 
 */
public final class Configuration {
	private static String propertiesFile = "cfg/cspoker.properties";
	private static Properties properties = new Properties();
	private static Logger logger = Logger.getLogger(Configuration.class);

	static {
		try {
			Configuration.properties.load(new FileInputStream(Configuration.propertiesFile));
		} catch (final FileNotFoundException e) {
			Configuration.logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (final IOException e) {
			Configuration.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private Configuration() {
		// Utility class, should not be instantiated
	}

	public static Properties getProperties() {
		return Configuration.properties;
	}
}
