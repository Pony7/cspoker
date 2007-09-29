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
package org.cspoker.server.http.httphandler.exception;

import org.xml.sax.SAXException;

/**
 * A wrapper exception for exceptions thrown inside a SAX ContentHandler with 
 * support for Http status codes.
 */
public class HttpSaxException extends SAXException implements HttpException{

    private static final long serialVersionUID = 1886168467073180945L;
    
    private final int status;
    
    public HttpSaxException(Exception e, int status) {
	super(e);
	this.status=status;
    }
    
    public int getStatus(){
	return status;
    }
    
}
