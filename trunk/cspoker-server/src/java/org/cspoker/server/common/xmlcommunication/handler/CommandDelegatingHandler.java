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
package org.cspoker.server.common.xmlcommunication.handler;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.cspoker.common.xmlcommunication.XmlEventCollector;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.cspoker.server.game.playerCommunication.PlayerCommunication;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class CommandDelegatingHandler extends DelegatingHandler implements
ContentHandler {
    private final static Logger logger = Logger.getLogger(CommandDelegatingHandler.class);

    private final PlayerCommunication playerComm;
    private final XmlEventCollector collector;

    public CommandDelegatingHandler(PlayerCommunication playerComm,
	    XmlEventCollector collector) {
	this.playerComm = playerComm;
	this.collector = collector;
    }

    @Override
    public ContentHandler getHandler(String uri, String localName, String name,
	    Attributes attrs) throws SAXException {
	try {
	    if("call".equalsIgnoreCase(localName)){
		return new CallHandler(attrs);
	    }else if ("bet".equalsIgnoreCase(localName)){
		return new BetHandler(attrs);
	    }else if ("fold".equalsIgnoreCase(localName)){
		return new FoldHandler(attrs);
	    }else if ("check".equalsIgnoreCase(localName)){
		return new CheckHandler(attrs);
	    }else if ("raise".equalsIgnoreCase(localName)){
		return new RaiseHandler(attrs);
	    }else if ("deal".equalsIgnoreCase(localName)){
		return new DealHandler(attrs);
	    }else if ("allin".equalsIgnoreCase(localName)){
		return new AllinHandler(attrs);
	    }else if ("jointable".equalsIgnoreCase(localName)){
		return new JointableHandler(attrs);
	    }else if ("leavetable".equalsIgnoreCase(localName)){
		return new LeavetableHandler(attrs);
	    }else if ("createable".equalsIgnoreCase(localName)){
		return new CreatetableHandler(attrs);
	    }else if ("startgame".equalsIgnoreCase(localName)){
		return new StartgameHandler(attrs);
	    }
	} catch (IllegalActionException e) {
	    try {
		logger.error("Illegal action performed.", e);
		notifyOfIllegalAction(e);
	    } catch (TransformerConfigurationException e1) {
		logger.fatal("Can't transform illegal action to xml.", e1);
		throw new IllegalStateException(e1);
	    }
	}
	throw new SAXException(new IllegalArgumentException("Illegal tag name."));
    }


    private void notifyOfIllegalAction(IllegalActionException e) throws TransformerConfigurationException, SAXException {
	StringWriter xml = new StringWriter();

	SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
	TransformerHandler response = tf.newTransformerHandler();
	response.setResult(new StreamResult(xml));
	response.startDocument();
	response.startElement("", "exception", "exception", new AttributesImpl());

	response.startElement("", "message", "message", new AttributesImpl());
	String msg=(e.getMessage()==null?"unknown error":e.getMessage());
	response.characters(msg.toCharArray(), 0, msg.length());
	response.endElement("", "message", "message");

	response.startElement("", "stacktrace", "stacktrace", new AttributesImpl());
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw, true);
	e.printStackTrace(pw);
	pw.flush();
	sw.flush();
	String trace=sw.toString();
	response.characters(trace.toCharArray(), 0, trace.length());
	response.endElement("", "stacktrace", "stacktrace");

	response.endElement("", "exception", "exception");
	response.endDocument();
	collector.collect(xml.toString());

    }


    private class StartgameHandler extends DefaultHandler {
	public StartgameHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.startGame();
	}
    }

    private class CreatetableHandler extends DefaultHandler {

	public CreatetableHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.createTable();
	}

    }

    private class LeavetableHandler extends DefaultHandler {

	public LeavetableHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.leaveTable();
	}

    }

    private class JointableHandler extends DefaultHandler {

	public JointableHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.joinTable(new TableId(Integer.parseInt(attrs.getValue("amount"))));
	}

    }

    private class AllinHandler extends DefaultHandler {

	public AllinHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.allIn();
	}

    }

    private class DealHandler extends DefaultHandler {

	public DealHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.deal();
	}

    }

    private class RaiseHandler extends DefaultHandler {

	public RaiseHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.raise(Integer.parseInt(attrs.getValue("amount")));
	}

    }

    private class CheckHandler extends DefaultHandler {

	public CheckHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.check();
	}

    }

    private class FoldHandler extends DefaultHandler {

	public FoldHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.fold();
	}

    }

    private class BetHandler extends DefaultHandler {

	public BetHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.bet(Integer.parseInt(attrs.getValue("amount")));
	}

    }

    private class CallHandler extends DefaultHandler {

	public CallHandler(Attributes attrs) throws IllegalActionException {
	    playerComm.call();
	}

    }

}
