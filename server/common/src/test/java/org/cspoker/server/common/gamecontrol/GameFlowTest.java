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
package org.cspoker.server.common.gamecontrol;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.player.MutablePlayer;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.account.ExtendedAccountContext;
import org.cspoker.server.common.elements.table.PlayerListFullException;
import org.cspoker.server.common.elements.table.ServerTable;
import org.cspoker.server.common.gamecontrol.rounds.FinalRound;
import org.cspoker.server.common.gamecontrol.rounds.FlopRound;
import org.cspoker.server.common.gamecontrol.rounds.PreFlopRound;
import org.cspoker.server.common.gamecontrol.rounds.TurnRound;
import org.cspoker.server.common.gamecontrol.rounds.WaitingRound;

public class GameFlowTest extends TestCase {

	private static Logger logger = Logger.getLogger(GameFlowTest.class);

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/server/common/logging/log4j.properties");
	}

	private MutableSeatedPlayer cedric;

	private DummyPlayerFactory factory = new DummyPlayerFactory();

	private MutableSeatedPlayer guy;

	private MutableSeatedPlayer kenzo;

	private PokerTable pokerTable;

	private ServerTable table;

	@Override
	public void setUp(){
		table = new ServerTable(8);
		pokerTable = new PokerTable(new TableId(0), "table", new TableConfiguration(), new ExtendedAccountContext(){

			public void changePassword(String passwordHash) {

			}

			public void createAccount(String username, String passwordHash) {

			}

			public byte[] getAvatar(PlayerId playerId) {
				return null;
			}

			public MutablePlayer getPlayer() {
				return null;
			}

			public boolean hasPassword(String passwordHash) {
				return false;
			}

			public void setAvatar(byte[] avatar) {

			}

			public PlayerId getPlayerID() {
				return new PlayerId(0);
			}

		});
		pokerTable.subscribeHoldemTableListener(new HoldemTableListener(){

			public void onAllIn(AllInEvent allInEvent) {
				GameFlowTest.logger.info(allInEvent);
			}

			public void onBet(BetEvent betEvent) {
				GameFlowTest.logger.info(betEvent);

			}

			public void onBigBlind(BigBlindEvent bigBlindEvent) {
				GameFlowTest.logger.info(bigBlindEvent);
			}

			public void onCall(CallEvent callEvent) {
				GameFlowTest.logger.info(callEvent);					
			}

			public void onCheck(CheckEvent checkEvent) {
				GameFlowTest.logger.info(checkEvent);					
			}

			public void onFold(FoldEvent foldEvent) {
				GameFlowTest.logger.info(foldEvent);					
			}

			public void onJoinTable(JoinTableEvent joinTableEvent) {
				GameFlowTest.logger.info(joinTableEvent);					
			}

			public void onLeaveTable(LeaveTableEvent leaveTableEvent) {
				GameFlowTest.logger.info(leaveTableEvent);					
			}

			public void onNewCommunityCards(
					NewCommunityCardsEvent newCommunityCardsEvent) {
				GameFlowTest.logger.info(newCommunityCardsEvent);							
			}

			public void onNewDeal(NewDealEvent newDealEvent) {
				GameFlowTest.logger.info(newDealEvent);							
			}

			public void onNewRound(NewRoundEvent newRoundEvent) {
				GameFlowTest.logger.info(newRoundEvent);							
			}

			public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
				GameFlowTest.logger.info(nextPlayerEvent);							
			}

			public void onRaise(RaiseEvent raiseEvent) {
				GameFlowTest.logger.info(raiseEvent);							
			}

			public void onShowHand(ShowHandEvent showHandEvent) {
				GameFlowTest.logger.info(showHandEvent);							
			}

			public void onSitIn(SitInEvent sitInEvent) {
				GameFlowTest.logger.info(sitInEvent);
			}

			public void onSitOut(SitOutEvent sitOutEvent) {
				GameFlowTest.logger.info(sitOutEvent);					
			}

			public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
				GameFlowTest.logger.info(smallBlindEvent);					
			}

			public void onWinner(WinnerEvent winnerEvent) {
				GameFlowTest.logger.info(winnerEvent);					
			}

			public void onPotsChanged(PotsChangedEvent potsChangedEvent) {
				GameFlowTest.logger.info(potsChangedEvent);		
			}

		});
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("cedric", 100), 100);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("guy",100), 100);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);

		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}

	public void test2AllInOneActivePlayerCase() {
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),100);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 200),200);
			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		// Game continues automatically
	}

	public void testAllAllInCase() {
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 200), 200);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),100);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 150),150);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.allIn(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		GameFlowTest.logger.info("Common Cards: " + game.getCommunityCards());

		// New game
		MutableSeatedPlayer testPlayer;
		try {
			testPlayer = new MutableSeatedPlayer(factory.createNewPlayer("Test", 150),150);
			gameControl.sitIn(new SeatId(4), testPlayer);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		}

	}

	public void testAllInBigBlindCase() {
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),100);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 100),9);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	/*
	 * An all-in raise that is greater than half the previous raise opens up the
	 * round for more betting.
	 */
	public void testAllInRaiseCase() {
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 200), 200);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 220),220);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 100),100);

			table = new ServerTable(8);

			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Game Properties:");
		GameFlowTest.logger.info("Small Blind: "
				+ pokerTable.getTableConfiguration().getSmallBlind());
		GameFlowTest.logger.info("Big Blind: "
				+ pokerTable.getTableConfiguration().getBigBlind());

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		GameFlowTest.logger.info("Betting Rules: "
				+ gameControl.getGame().getBettingRules().toString());
		Game game = gameControl.getGame();

		GameFlowTest.logger.info("Dealer: " + game.getDealer());

		GameFlowTest.logger.info(game.getCurrentDealPlayers());
		GameFlowTest.logger.info("Kenzo's Cards: " + kenzo.getPocketCards());
		GameFlowTest.logger.info("Cedric's Cards: " + cedric.getPocketCards());
		GameFlowTest.logger.info("Guy's Cards: " + guy.getPocketCards());

		try {
			gameControl.raise(kenzo, 20);
			gameControl.call(cedric);
			gameControl.allIn(guy);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		try {
			gameControl.raise(kenzo, 75);
		} catch (IllegalActionException e1) {
			fail("Previous all in big enough to open up betting!");
		}


	}

	/**
	 * Test Settings: > 3 players > Big blind raises.
	 */
	public void testBigBlindRaisesCase() {
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		// Pre-flop Round
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			// Big Blind Raises.
			gameControl.raise(game.getCurrentPlayer(), 20);

			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testBothBlindsAllInCase() {
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),4);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 100),9);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		Game game = gameControl.getGame();
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		try {
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testCase1() {
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		Game game = gameControl.getGame();
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		// Pre-flop Round
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		assertTrue(game.getCurrentPlayer().equals(kenzo));

		try {
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

			// Big Blind Checks.
			gameControl.check(game.getCurrentPlayer());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		assertEquals(TurnRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		assertEquals(FinalRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// New Deal
		assertEquals(cedric, game.getDealer());
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		assertEquals(TurnRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		assertEquals(FinalRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// New Deal
		assertEquals(guy, game.getDealer());
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
	}

	public void testCase2() {
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, guy);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		// New Deal
		assertEquals(guy, game.getDealer());
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

		try {
			System.out.println(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.raise(game.getCurrentPlayer(), 10);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.raise(game.getCurrentPlayer(), 20);
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
			gameControl.call(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}


		// Flop Round
		assertEquals(FlopRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		assertEquals(TurnRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		assertEquals(FinalRound.class, gameControl.getRound().getClass());
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// New Deal
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
	}

	public void testMultiplePlayersAllInSmallBlindCase() {
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),4);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 100),100);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		try {
			gameControl.call(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Flop Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Turn Round
		try {
			gameControl.check(game.getCurrentPlayer());
			gameControl.check(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		// Final Round
		try {
			System.out.println("final round");
			gameControl.check(game.getCurrentPlayer());
			System.out.println("Check 1");
			gameControl.check(game.getCurrentPlayer());
			System.out.println("check 2");
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		GameFlowTest.logger.info("Next game...");

		GameFlowTest.logger.info("Current Player:" + game.getCurrentPlayer());
		GameFlowTest.logger.info("Next Dealer:" + game.getNextDealer());
		GameFlowTest.logger.info(game.getFirstToActPlayer());
		GameFlowTest.logger.info("Dealer: " + game.getDealer().getName());
		GameFlowTest.logger.info(game.getCurrentDealPlayers());
	}

	///*
	//* An all-in raise that is smaller than half the previous raise does not
	//* open up the round for more betting. Only calls are allowed.
	//*/
	//public void testAllInCallCase() {
	//      try {
	//              kenzo = playerFactory.createNewPlayer("Kenzo", 200);
	//              cedric = playerFactory.createNewPlayer("Cedric", 220);
	//              guy = playerFactory.createNewPlayer("Guy", 100);
	//
	//              TableId id = new TableId(0);
	//              gameMediator = new GameMediator(id);
	//              table = new GameTable(id, new GameProperty());
	//              table.addPlayer(kenzo);
	//              table.addPlayer(cedric);
	//              table.addPlayer(guy);
	//      } catch (IllegalValueException e) {
	//              fail(e.getMessage());
	//      } catch (PlayerListFullException e) {
	//              fail(e.getMessage());
	//      }
	//
	//      GameControl gameControl = new GameControl(gameMediator, table, kenzo);
	//
	//      try {
	//              gameControl.raise(kenzo, 75);
	//              gameControl.call(cedric);
	//              gameControl.allIn(guy);
	//      } catch (IllegalActionException e) {
	//              fail(e.getMessage());
	//      }
	//
	//      try {
	//              gameControl.raise(kenzo, 75);
	//              fail("Previous all in was not big enough to open up betting!");
	//      } catch (IllegalActionException e1) {
	//              // Should not reach here
	//      }
	//}

	public void testOnlyOneActivePlayer() {

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table);

		Game game = gameControl.getGame();
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		try {
			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		assertFalse(gameControl.getRound() instanceof WaitingRound);

	}

	public void testOnlyOneAllInPlayer() {

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testOnlyOneAllInPlayer2() {
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 200),200);
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 200),200);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			table.addPlayer(guy);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		Game game = gameControl.getGame();

		try {
			gameControl.allIn(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());
			gameControl.call(game.getCurrentPlayer());

			gameControl.fold(game.getCurrentPlayer());
			gameControl.fold(game.getCurrentPlayer());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testTwoPlayersDealerChecksCase(){
		table = new ServerTable(8);
		PlayingTableState gameControl;
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("kenzo", 100), 50);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("cedric", 100), 50);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			gameControl = new PlayingTableState(pokerTable, table, kenzo);
			try {
				gameControl.deal();
			} catch (IllegalActionException e1) {
				fail(e1.toString());
			}




			try {
				gameControl.check(kenzo);
				fail("Exception Expected.");
			} catch (IllegalActionException e) {
			}



			try {
				gameControl.call(kenzo);
				gameControl.check(cedric);
			} catch (IllegalActionException e) {
				fail(e.getMessage());
			}

		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}

	public void testTwoPlayersNormalCase(){
		table = new ServerTable(8);
		PlayingTableState gameControl;
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("cedric", 100), 100);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			gameControl = new PlayingTableState(pokerTable, table, kenzo);
			try {
				gameControl.deal();
			} catch (IllegalActionException e1) {
				fail(e1.toString());
			}
			try {
				gameControl.check(kenzo);
				fail("Exception Expected.");
			} catch (IllegalActionException e) {
			}
			try {
				gameControl.call(kenzo);
				gameControl.check(cedric);
			} catch (IllegalActionException e) {
				fail(e.getMessage());
			}

			assertEquals(FlopRound.class, gameControl.getRound().getClass());

			try {
				gameControl.check(cedric);
				gameControl.check(kenzo);
			} catch (IllegalActionException e) {
				fail(e.getMessage());
			}

			assertEquals(TurnRound.class, gameControl.getRound().getClass());

			try {
				gameControl.check(cedric);
				gameControl.check(kenzo);
			} catch (IllegalActionException e) {
				fail(e.getMessage());
			}

			assertEquals(FinalRound.class, gameControl.getRound().getClass());

			try {
				gameControl.check(cedric);
				gameControl.check(kenzo);
			} catch (IllegalActionException e) {
				fail(e.getMessage());
			}

			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}

	public void testTwoPlayersDealerRaisesCase(){
		table = new ServerTable(8);
		PlayingTableState gameControl;
		try {
			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("cedric", 100), 100);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			gameControl = new PlayingTableState(pokerTable, table, kenzo);
			try {
				gameControl.deal();
			} catch (IllegalActionException e1) {
				fail(e1.toString());
			}
			try {
				gameControl.raise(kenzo, 30);
				gameControl.call(cedric);
			} catch (IllegalActionException e) {
				fail(e.getMessage());
			}

		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}

	/*
	 * Test that the dealer gets to act first on the first round in a heads up
	 * game
	 */
	public void testTwoPlayersDealerSwitch() {
		MutableSeatedPlayer craig = null;
		try {
			guy = new MutableSeatedPlayer(factory.createNewPlayer("Guy", 100),100);
			craig = new MutableSeatedPlayer(factory.createNewPlayer("Craig", 100),100);
			table = new ServerTable(8);
			table.addPlayer(guy);
			table.addPlayer(craig);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, guy);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		try {
			gameControl.call(guy);
			gameControl.check(craig);

			gameControl.check(craig);
			gameControl.check(guy);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testTwoPlayersOneFold() {
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 500), 500);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 500),500);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

		try {
			gameControl.call(kenzo);
			gameControl.raise(cedric, 20);
			gameControl.call(kenzo);

			assertEquals(FlopRound.class, gameControl.getRound().getClass());

			gameControl.bet(cedric, 20);
			gameControl.call(kenzo);

			assertEquals(TurnRound.class, gameControl.getRound().getClass());

			gameControl.bet(cedric, 20);
			gameControl.call(kenzo);

			assertEquals(FinalRound.class, gameControl.getRound().getClass());

			gameControl.bet(cedric, 20);
			gameControl.raise(kenzo, 50);
			gameControl.fold(cedric);

			assertEquals(PreFlopRound.class, gameControl.getRound().getClass());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}


	/**
	 * 2 players: 5/10
	 * Cedric small blind with 4 chips.
	 * Kenzo calls and automatic show down.
	 * 
	 */
	public void testTwoPlayersSmallBlindAllInCase(){
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 100);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),4);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, cedric);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		try {
			performAction(gameControl, kenzo, Action.call, 5);
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		//Verify auto-showdown
		assertNotSame(FlopRound.class, gameControl.getRound().getClass());

	}

	public void testTwoPlayersSmallBlindRaises() {
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 500), 500);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 500),500);


			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, kenzo);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}
		try {
			gameControl.raise(kenzo, 20);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		try {
			gameControl.check(cedric);
			fail("Exception expected: can not check after raise");
		} catch (IllegalActionException e) {
		}

		try {
			gameControl.call(cedric);

			assertEquals(FlopRound.class, gameControl.getRound().getClass());
			gameControl.check(cedric);
			gameControl.check(kenzo);

			assertEquals(TurnRound.class, gameControl.getRound().getClass());
			gameControl.check(cedric);
			gameControl.check(kenzo);
			assertEquals(FinalRound.class, gameControl.getRound().getClass());
			gameControl.check(cedric);
			gameControl.check(kenzo);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertEquals(PreFlopRound.class, gameControl.getRound().getClass());
	}

	/**
	 * 2 players: 5/10
	 * Kenzo big blind with 8 chips (> big blind)
	 * Cedric calls and automatic show down.
	 * 
	 */
	public void testTwoPlayersBigBlindAllInCase1(){
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 8);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),100);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, cedric);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		try {
			performAction(gameControl, cedric, Action.call, 5);
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		//Verify auto-showdown
		assertNotSame(FlopRound.class, gameControl.getRound().getClass());

	}
	
	/**
	 * 2 players: 5/10
	 * Kenzo big blind with 4 chips (< small blind)
	 * automatic show down.
	 * 
	 */
	public void testTwoPlayersBigBlindAllInCase2(){
		try {

			kenzo = new MutableSeatedPlayer(factory.createNewPlayer("Kenzo", 100), 4);
			cedric = new MutableSeatedPlayer(factory.createNewPlayer("Cedric", 100),100);

			table = new ServerTable(8);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}

		PlayingTableState gameControl = new PlayingTableState(pokerTable, table, cedric);
		try {
			gameControl.deal();
		} catch (IllegalActionException e1) {
			fail(e1.toString());
		}

		//Verify auto-showdown
		assertNotSame(FlopRound.class, gameControl.getRound().getClass());

	}

	public void performAction(PlayingTableState gameControl, MutableSeatedPlayer player, Action action, int argument, Action... validAlternatives) throws IllegalActionException{
		List<Action> alternatives = Arrays.asList(validAlternatives);

		for (Action generalAction : Action.values()) {
			if(generalAction != action && generalAction!=Action.fold && !alternatives.contains(generalAction)){
				try {
					GameFlowTest.logger.info("++ "+player+ " tries to "+generalAction);
					generalAction.performAction(gameControl, player, argument);
					fail(player+" should not be able to "+generalAction);
				} catch (IllegalActionException e) {
					GameFlowTest.logger.info("++ "+player+ " action denied");
				}
			}
		}
		action.performAction(gameControl, player, argument);
	}

	private enum Action{
		check{
			@Override
			public void performAction(PlayingTableState gameControl,
					MutableSeatedPlayer player, int arguement) throws IllegalActionException {
				gameControl.check(player);
			}
		}
		, call{

			@Override
			public void performAction(PlayingTableState gameControl,
					MutableSeatedPlayer player, int argument) throws IllegalActionException {
				gameControl.call(player);
			}

		}, bet{

			@Override
			public void performAction(PlayingTableState gameControl,
					MutableSeatedPlayer player, int argument) throws IllegalActionException {
				gameControl.bet(player, argument);
			}

		}, raise{

			@Override
			public void performAction(PlayingTableState gameControl,
					MutableSeatedPlayer player, int argument) throws IllegalActionException {
				gameControl.raise(player, argument);
			}

		}, fold{

			@Override
			public void performAction(PlayingTableState gameControl,
					MutableSeatedPlayer player, int argument) throws IllegalActionException {
				gameControl.fold(player);
			}
		};

		public abstract void performAction(PlayingTableState gameControl, MutableSeatedPlayer player, int argument) throws IllegalActionException;

	}

}
