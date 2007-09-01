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
package org.cspoker.client.exceptions;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExceptionParser extends DefaultHandler {

    private HttpException exception;

    private StringBuilder sb=new StringBuilder();
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch,start,length);
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
	if(name.equalsIgnoreCase("msg")){
	    exception=new HttpException(sb.toString());
	}if(name.equalsIgnoreCase("stacktrace")){
	    exception.setStackTraceString(sb.toString());
	}
	sb.setLength(0);
    }
    
    public Exception getException(){
	return exception;
    }
    
}
