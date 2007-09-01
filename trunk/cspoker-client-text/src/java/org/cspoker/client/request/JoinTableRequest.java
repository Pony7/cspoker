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

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.client.request.abstracts.OutputRequest;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class JoinTableRequest extends OutputRequest{

    public JoinTableRequest(String address) throws MalformedURLException {
	super(address);
    }

    @Override
    protected void doOutput(TransformerHandler request, String... args) throws SAXException {
	if(args.length<1)
	    throw new IllegalArgumentException("Not enough arguments.");
	
	this.id=args[0];
	request.startElement("", "id", "id", new AttributesImpl());
	String s=args[0];
	request.characters(s.toCharArray(), 0, s.length());
	request.endElement("", "id", "id");
    }

    @Override
    protected String getPath() {
	return "/table/join/";
    }

    private String id="";
    
    @Override
    protected String getResult() {
	return "Joined table "+id+"."+n;
    }
    
    @Override
    protected ContentHandler getContentHandler() {

	return new DefaultHandler(){

	};
    }

}
