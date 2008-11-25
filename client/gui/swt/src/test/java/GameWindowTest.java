import java.util.*;

import junit.framework.TestCase;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
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
		final SeatedPlayer player1 = new SeatedPlayer(new PlayerId(0), new SeatId(0), "TestPlayer 0", 10000, 0, true,
				true);
		final SeatedPlayer player2 = new SeatedPlayer(new PlayerId(1), new SeatId(1), "TestPlayer 1", 20000, 0, true,
				true);
		final SeatedPlayer player3 = new SeatedPlayer(new PlayerId(2), new SeatId(2), "TestPlayer 2", 55550, 0, true,
				true);
		final SeatedPlayer player4 = new SeatedPlayer(new PlayerId(3), new SeatId(3), "TestPlayer 3", 56000, 0, true,
				true);
		final SeatedPlayer player5 = new SeatedPlayer(new PlayerId(4), new SeatId(4), "TestPlayer 4", 569000, 0, true,
				true);
		final SeatedPlayer player6 = new SeatedPlayer(new PlayerId(5), new SeatId(5), "TestPlayer 5", 70003, 0, true,
				true);
		players.addAll(Arrays.asList(player1, player2, player3, player4, player5, player6));
		final List<SeatedPlayer> seatedPlayers = Collections.unmodifiableList(players);
		final GameWindow w = new GameWindow(new LobbyWindow(core), new DetailedHoldemTable(new TableId(0), "wurst",
				Arrays.asList(player1, player3, player5), false, tconfig));
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
					w.onNewPocketCards(new NewPocketCardsEvent(EnumSet.of(Card.QUEEN_HEARTS, Card.ACE_CLUBS)));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNewCommunityCards(new NewCommunityCardsEvent(EnumSet.of(Card.QUEEN_DIAMONDS, Card.ACE_HEARTS,
							Card.SIX_HEARTS)));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNextPlayer(new NextPlayerEvent(player1.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onBet(new BetEvent(player1, 15));
					w.onNextPlayer(new NextPlayerEvent(player2.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onRaise(new RaiseEvent(player2.getId(), 30, 45));
					w.onNextPlayer(new NextPlayerEvent(player3.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onRaise(new RaiseEvent(player3.getId(), 100, 145));
					w.onNextPlayer(new NextPlayerEvent(player4.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onCall(new CallEvent(player4, false));
					w.onNextPlayer(new NextPlayerEvent(player1.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onFold(new FoldEvent(player1, false));
					w.onNextPlayer(new NextPlayerEvent(player2.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onRaise(new RaiseEvent(player2.getId(), 500, 545));
					w.onNextPlayer(new NextPlayerEvent(player3.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onFold(new FoldEvent(player3, false));
					w.onNextPlayer(new NextPlayerEvent(player4.getId()));
					Thread.sleep(MS_ACTION_DELAY);
					w.onCall(new CallEvent(player4, true));
					Thread.sleep(MS_ACTION_DELAY);
					w.onShowHand(new ShowHandEvent(new ShowdownPlayer(player4, EnumSet.of(Card.SIX_CLUBS,
							Card.SIX_SPADES, Card.SIX_HEARTS, Card.QUEEN_DIAMONDS, Card.ACE_HEARTS), EnumSet.of(
							Card.SIX_CLUBS, Card.SIX_SPADES), " good hand")));
					Thread.sleep(MS_ACTION_DELAY);
					w.onWinner(new WinnerEvent(new TreeSet<Winner>(Arrays.asList(new Winner(player4, 3323)))));
					Thread.sleep(MS_ACTION_DELAY);
					w.getUserInputComposite().onMessage(new MessageEvent(player2, "Doh"));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNewDeal(new NewDealEvent(seatedPlayers, player2));
					Thread.sleep(MS_ACTION_DELAY);
					w.onNextPlayer(new NextPlayerEvent(player3.getId()));
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
