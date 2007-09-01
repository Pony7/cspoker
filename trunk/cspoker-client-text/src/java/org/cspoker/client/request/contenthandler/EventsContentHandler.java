package org.cspoker.client.request.contenthandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cspoker.client.savedstate.Cards;
import org.cspoker.client.savedstate.Pot;
import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.common.game.elements.cards.CardImpl;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EventsContentHandler extends DefaultHandler {

    private List<String> events;
    
    private volatile String lastID = "0";
    
    private StringBuilder sb=new StringBuilder();

    private Cards cards;
    
    private List<Card> newCards = new ArrayList<Card>(5);
    private boolean privateCards = false;
    private Suit lastSuit;

    private Pot pot;
    
    private String lastMsg;
    private boolean showLastMsg = true;
    
    public EventsContentHandler(Cards cards, Pot pot) {
	this.cards = cards;
	this.pot = pot;
    }
    
    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public String getLastID() {
        return lastID;
    }

    @Override
    public void startDocument() throws SAXException {
        events=new ArrayList<String>();
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch, start, length);
    }
    
    @Override
    public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
        
        if(name.equalsIgnoreCase("events")){
            lastID=attributes.getValue("lastEventNumber");
        }else if(name.equalsIgnoreCase("cards")){
            newCards.removeAll(newCards);
            if(attributes.getValue("type").equalsIgnoreCase("private")){
        	privateCards=true;
            }else{
        	privateCards=false;
            }
        }else if(name.equalsIgnoreCase("card")){
            lastSuit = Suit.getSuit(attributes.getValue("suit"));
        }else if( name.equalsIgnoreCase("event")){
            showLastMsg = true;
        }
        sb.setLength(0);
    }
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if(name.equalsIgnoreCase("msg")){
            lastMsg = sb.toString();
        }else if(name.equalsIgnoreCase("card")){
            Rank rank = Rank.getRank(sb.toString());
            if(rank==null || lastSuit==null)
        	throw new SAXException("Problem parsing XML response");
            newCards.add(new CardImpl(lastSuit, rank));
        }else if(name.equalsIgnoreCase("cards")){
            if(privateCards)
        	cards.setPrivateCards(newCards);
            else
        	cards.addRiverCards(newCards);
        }else if(name.equalsIgnoreCase("deal")){
            cards.resetCards();
            pot.resetPot();
        }else if (name.equalsIgnoreCase("pot")){
            pot.setAmount(Integer.parseInt(sb.toString()));
            showLastMsg = false;
        }else if (name.equalsIgnoreCase("event")){
            if(showLastMsg){
        	events.add(lastMsg);
            }
        }
        sb.setLength(0);
    }
    
}
