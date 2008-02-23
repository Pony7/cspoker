package org.cspoker.common.events;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;

public class EventJAXBContext {

	private final static Logger logger = Logger.getLogger(EventJAXBContext.class);

	public final static JAXBContext context = initContext();

    private static JAXBContext initContext() {
        try {
			return JAXBContext.newInstance(
					//playeractionevents
					AllInEvent.class, BetEvent.class, BigBlindEvent.class
					, CallEvent.class, CheckEvent.class, FoldEvent.class, RaiseEvent.class, SmallBlindEvent.class
					//privatevents
					,NewPocketCardsEvent.class);
		} catch (JAXBException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		}
    }
	
}
