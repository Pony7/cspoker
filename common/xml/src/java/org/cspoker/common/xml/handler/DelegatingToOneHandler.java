package org.cspoker.common.xml.handler;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class DelegatingToOneHandler extends DelegatingHandler {

	private ContentHandler delegate;

	public DelegatingToOneHandler(ContentHandler delegate) {
		this.delegate = delegate;
	}

	@Override
	public ContentHandler getHandler(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		return delegate;
	}

}
