import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableConfiguration;
import org.eclipse.swt.widgets.Display;

/**
 * A visual test for checking layout, drawing etc in the {@link GameWindow}
 */
public class GameWindowTest
		extends TestCase {
	
	/** Standard delay between actions */
	public final static int MS_ACTION_DELAY = 0;
	
	/**
	 * A visual test for checking layout, drawing etc in the {@link GameWindow}
	 */
	public void testGameWindow() {
		User user = new User("TestPlayer 0", "");
		ClientCore core = new ClientCore(user);
		List<SeatedPlayer> players = new ArrayList<SeatedPlayer>();
		TableConfiguration tconfig = new TableConfiguration(20, 2000);
		final SeatedPlayer player1 = new SeatedPlayer(0, 0, "TestPlayer 0", 10000, 0);
		final SeatedPlayer player2 = new SeatedPlayer(1, 1, "TestPlayer 1", 20000, 0);
		final SeatedPlayer player3 = new SeatedPlayer(2, 2, "TestPlayer 2", 55550, 0);
		final SeatedPlayer player4 = new SeatedPlayer(3, 3, "TestPlayer 3", 56000, 0);
		final SeatedPlayer player5 = new SeatedPlayer(4, 4, "TestPlayer 4", 569000, 0);
		final SeatedPlayer player6 = new SeatedPlayer(5, 5, "TestPlayer 5", 70003, 0);
		players.addAll(Arrays.asList(player1, player2, player3, player4, player5, player6));
		final List<SeatedPlayer> seatedPlayers = Collections.unmodifiableList(players);
		final GameWindow w = new GameWindow(new LobbyWindow(core), new DetailedHoldemTable(0, "wurst", Arrays.asList(
				player1, player3, player5), false, tconfig));
		// Fire some events for w with a delay
		w.getDisplay().timerExec(1000, new Runnable() {
			
			public void run() {
				try {
					w.getUserInputComposite().getGameInfoText().append(
							"Button actions are not enabled in this demo due to missing contexts");
					w.onSitIn(new SitInEvent(player2));
					Thread.sleep(MS_ACTION_DELAY);
					w.onSitIn(new SitInEvent(player4));
					// w.onSitIn(new SitInEvent(player6));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNewDeal(new NewDealEvent(seatedPlayers, player1));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNewPocketCards(new NewPocketCardsEvent(new HashSet<Card>(Arrays.asList(new Card(Rank.QUEEN,
							Suit.HEARTS), new Card(Rank.ACE, Suit.CLUBS)))));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNewCommunityCards(new NewCommunityCardsEvent(new HashSet<Card>(Arrays.asList(new Card(
							Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.ACE, Suit.HEARTS),
							new Card(Rank.SIX, Suit.HEARTS)))));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNextPlayer(new NextPlayerEvent(player1));
					Thread.sleep(MS_ACTION_DELAY);
					w.onBet(new BetEvent(player1, 50, new Pots(15)));
					w.onNextPlayer(new NextPlayerEvent(player2));
					Thread.sleep(MS_ACTION_DELAY);
					w.onRaise(new RaiseEvent(player2, 100, new Pots(45)));
					w.onNextPlayer(new NextPlayerEvent(player3));
					Thread.sleep(MS_ACTION_DELAY);
					w.onRaise(new RaiseEvent(player3, 320, new Pots(499)));
					w.onNextPlayer(new NextPlayerEvent(player4));
					Thread.sleep(MS_ACTION_DELAY);
					w.onCall(new CallEvent(player4, new Pots(499)));
					w.onNextPlayer(new NextPlayerEvent(player1));
					Thread.sleep(MS_ACTION_DELAY);
					w.onFold(new FoldEvent(player1));
					w.onNextPlayer(new NextPlayerEvent(player2));
					Thread.sleep(MS_ACTION_DELAY);
					w.onRaise(new RaiseEvent(player2, 1200, new Pots(2323)));
					w.onNextPlayer(new NextPlayerEvent(player3));
					Thread.sleep(MS_ACTION_DELAY);
					w.onFold(new FoldEvent(player3));
					w.onNextPlayer(new NextPlayerEvent(player4));
					Thread.sleep(MS_ACTION_DELAY);
					w.onCall(new CallEvent(player4, new Pots(3323)));
					Thread.sleep(MS_ACTION_DELAY);
					w.onShowHand(new ShowHandEvent(new ShowdownPlayer(player4,
							new HashSet<Card>(Arrays.asList(new Card(Rank.SIX, Suit.CLUBS), new Card(Rank.SIX,
									Suit.SPADES), new Card(Rank.SIX, Suit.HEARTS), new Card(Rank.QUEEN, Suit.DIAMONDS),
									new Card(Rank.ACE, Suit.HEARTS))), new HashSet<Card>(Arrays.asList(new Card(
									Rank.SIX, Suit.CLUBS), new Card(Rank.SIX, Suit.SPADES))), " good hand")));
					Thread.sleep(MS_ACTION_DELAY);
					w.onWinner(new WinnerEvent(new TreeSet<Winner>(Arrays.asList(new Winner(player4, 3323)))));
					Thread.sleep(MS_ACTION_DELAY);
					w.getUserInputComposite().onMessage(new MessageEvent(player2, "Doh"));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNewDeal(new NewDealEvent(seatedPlayers, player2));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNextPlayer(new NextPlayerEvent(player3));
				} catch (IllegalArgumentException e) {
					System.err.println(e);
					e.printStackTrace();
					fail();
				} catch (InterruptedException e) {
					fail();
				}
			}
		});
		// Show w in a blocking thread
		Display.getDefault().syncExec(new Runnable() {
			
			public void run() {
				w.show();
			}
		});
		
	}
}
