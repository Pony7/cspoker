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
