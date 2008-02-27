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
package org.cspoker.common.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class DelegatingHandler extends DefaultHandler {
	private ContentHandler handler = null;
	private int layers = 0;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if (handler == null) {
			handler = getHandler(uri, localName, qName, atts);
		} else {
			handler.startElement(uri, localName, qName, atts);
		}
		++layers;
	}

	public abstract ContentHandler getHandler(String uri, String localName,
			String qName, Attributes atts) throws SAXException;

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		--layers;
		if (layers == 0) {
			handler = null;
		} else {
			handler.endElement(uri, localName, qName);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (handler != null) {
			characters(ch, start, length);
		}
	}
}
