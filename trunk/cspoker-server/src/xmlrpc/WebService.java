/** This program is free software; you can redistribute it and/or modify
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
package xmlrpc;

import game.PlayerListFullException;

/**
 * Provides the basic actions available at the server as a public webservice.
 */
public class WebService{

    private static ClientControl control = new ClientControl();
    
    
    public WebService() {
	
    }
    
    public long login(String username){
	return control.login(username);
    }

    public int joinTable(long id, String tablename) {
	try {
	    control.joinTable(id, tablename);
	    return 0;
	} catch (PlayerListFullException e) {
	    return 1;
	}
    }
    
}
