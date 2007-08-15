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
package org.cspoker.client.request;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.cspoker.client.request.abstracts.NoOutputRequest;
import org.xml.sax.SAXException;

public class ListTablesRequest extends NoOutputRequest {

    private List<String> tables;
    private StringBuilder sb=new StringBuilder();
    
    public ListTablesRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected String getResult() {
	if(tables.size()==0)
	    return "No tables found."+n;
	String r="Table ID's:"+n;
	for(String s:tables){
	    r+=s+n;
	}
	return r;
    }

    @Override
    protected String getPath() {
	return "/table/list/";
    }
    
    @Override
    public void startDocument() throws SAXException {
        tables=new ArrayList<String>();
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch, start, length);
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if(name.equalsIgnoreCase("table")){
            tables.add(sb.toString());
            sb.setLength(0);
        }
    }

}
