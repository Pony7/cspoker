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
package org.cspoker.server.data;

import java.io.File;

public class DataDirectory {

	/**
	 * Find and create if necessairy the user's application directory, according
	 * to operating system standards. The resolved directory is then stored in
	 * the system property <code>cspoker.home</code>
	 * <p>
	 * If the system property <code>cspoker.home</code> already exists, the
	 * directory specified by that path will be used instead and created if
	 * needed.
	 * <p>
	 * If any exception occurs (such as out of diskspace), cspoker.home will be
	 * unset.
	 *
	 * <p>
	 * On Windows, this will typically be something like:
	 *
	 * <pre>
	 *      	C:\Document and settings\MyUsername\Application Data\MyApplication
	 * </pre>
	 *
	 * while on Mac OS X it will be something like:
	 *
	 * <pre>
	 *      	/Users/MyUsername/Library/Application Support/MyApplication
	 * </pre>
	 *
	 * All other OS'es are assumed to be UNIX-alike, returning something like:
	 *
	 * <pre>
	 *      	/user/myusername/.cspoker
	 * </pre>
	 *
	 * <p>
	 * If the directory does not already exist, it will be created. It will also
	 * create the 'conf' directory within it if it doesn't exist.
	 * </p>
	 *
	 * @post System property <code>cspoker.home</code> contains path of an
	 *         existing directory for cspoker user-centric files.
	 */
	public static void findUserDir() {
		File appHome;
		String application = "CSPoker";
		String tavHome = System.getProperty("cspoker.home");
		if (tavHome != null) {
			appHome = new File(tavHome);
		} else {
			File home = new File(System.getProperty("user.home"));
			if (!home.isDirectory()) {
				// logger.error("User home not a valid directory: " + home);
				return;
			}
			String os = System.getProperty("os.name");
			// logger.debug("OS is " + os);
			if (os.equals("Mac OS X")) {
				File libDir = new File(home, "Library/Application Support");
				libDir.mkdirs();
				appHome = new File(libDir, application);
			} else if (os.startsWith("Windows")) {
				String APPDATA = System.getenv("APPDATA");
				File appData = null;
				if (APPDATA != null) {
					appData = new File(APPDATA);
				}
				if (appData != null && appData.isDirectory()) {
					appHome = new File(appData, application);
				} else {
					// logger.warn("Could not find %APPDATA%: " + APPDATA);
					appHome = new File(home, application);
				}
			} else {
				// We'll assume UNIX style is OK
				appHome = new File(home, "." + application.toLowerCase());
			}
		}
		if (!appHome.exists()) {
			if (appHome.mkdir()) {
				// logger.info("Created " + appHome);
			} else {
				// logger.error("Could not create " + appHome);
				System.clearProperty("cspoker.home");
				return;
			}
		}
		if (!appHome.isDirectory()) {
			// logger.error(APPLICATION + " user home not a valid directory: "
			// + appHome);
			System.clearProperty("cspoker.home");
			return;
		}

		// create conf folder
		File conf = new File(appHome, "conf");
		if (!conf.exists())
			conf.mkdir();

		System.setProperty("cspoker.home", appHome.getAbsolutePath());
		return;
	}

	
}
