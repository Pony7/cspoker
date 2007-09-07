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


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class Log4JPropertiesLoader {
    
    private static Logger logger = Logger.getLogger(Log4JPropertiesLoader.class);
    
    public static void load(String path){
	Properties properties = new Properties();
	try {
	    InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
	    properties.load(is);
	} catch (final IOException e) {
	    logger.error("Could not load log4j properties. "+e.getMessage(), e);
	    e.printStackTrace();
	}catch (final NullPointerException e) {
	    logger.error("Could not load log4j properties. "+e.getMessage(), e);
	    e.printStackTrace();
	}
	PropertyConfigurator.configure(properties);
    }
}
