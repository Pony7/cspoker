package org.cspoker.client.gui.text.eventlistener;

import org.cspoker.client.gui.text.Console;
import org.cspoker.client.gui.text.savedstate.Cards;
import org.cspoker.client.gui.text.savedstate.Pot;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;

public class StatefulConsoleListener extends ConsoleListener {

	private Cards cards;
	private Pot pot;

	public StatefulConsoleListener(Console console, Cards cards, Pot pot) {
		super(console);
		this.cards = cards;
		this.pot = pot;
	}

	public void onNewRoundEvent(NewRoundEvent event) {
		cards.resetCards();
		pot.resetPot();
		super.onNewRoundEvent(event);
	}

	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {
		cards.addCommonCards(event.getCommonCards());
		super.onNewCommunityCardsEvent(event);
	}

	public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
		cards.setPrivateCards(event.getPocketCards());
		super.onNewPocketCardsEvent(event);
	}

	public void onAllInEvent(AllInEvent event) {
		pot.setAmount(event.getPots().getTotalValue());
		super.onAllInEvent(event);
	}

	public void onBetEvent(BetEvent event) {
		pot.setAmount(event.getPots().getTotalValue());
		super.onBetEvent(event);
	}

	public void onBigBlindEvent(BigBlindEvent event) {
		pot.setAmount(event.getPots().getTotalValue());
		super.onBigBlindEvent(event);
	}

	public void onCallEvent(CallEvent event) {
		pot.setAmount(event.getPots().getTotalValue());
		super.onCallEvent(event);
	}

	public void onRaiseEvent(RaiseEvent event) {
		pot.setAmount(event.getPots().getTotalValue());
		super.onRaiseEvent(event);
	}

	public void onSmallBlindEvent(SmallBlindEvent event) {
		pot.setAmount(event.getPots().getTotalValue());
		super.onSmallBlindEvent(event);
	}

}
