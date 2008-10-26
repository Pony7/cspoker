package org.cspoker.server.common.gamecontrol;

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
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.account.ExtendedAccountContext;
import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.elements.id.TableId;
import org.cspoker.server.common.elements.table.PlayerListFullException;
import org.cspoker.server.common.elements.table.ServerTable;
import org.cspoker.server.common.player.GameSeatedPlayer;
import org.cspoker.server.common.player.ServerPlayer;

public class GameFlowTest extends TestCase {
	
	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/common/logging/log4j.properties");
	}
	
	private static Logger logger = Logger.getLogger(GameFlowTest.class);
	
	private ServerTable table;
	
	private DummyPlayerFactory factory = new DummyPlayerFactory();
	
	private GameSeatedPlayer kenzo;
	
	private GameSeatedPlayer cedric;
	
	private GameSeatedPlayer guy;
	
	private PlayingTableState gameControl;
	
	private PokerTable pokerTable;
	
	public void setUp(){
		table = new ServerTable(8);
		pokerTable = new PokerTable(new TableId(0), "table", new TableConfiguration(), new ExtendedAccountContext(){

			public ServerPlayer getPlayer() {
				return null;
			}

			public void changePassword(String passwordHash) {
				
			}

			public void createAccount(String username, String passwordHash) {
				
			}

			public byte[] getAvatar(long playerId) {
				return null;
			}

			public boolean hasPassword(String passwordHash) {
				return false;
			}

			public void setAvatar(byte[] avatar) {
				
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
			
		});
		try {
			kenzo = new GameSeatedPlayer(factory.createNewPlayer("kenzo", 100), 50);
			cedric = new GameSeatedPlayer(factory.createNewPlayer("cedric", 100), 50);
			guy = new GameSeatedPlayer(factory.createNewPlayer("guy",100), 50);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			gameControl = new PlayingTableState(pokerTable, table);
			
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
	}
	
	public void testTwoPlayersCase(){
		table = new ServerTable(8);
		try {
			kenzo = new GameSeatedPlayer(factory.createNewPlayer("kenzo", 100), 50);
			cedric = new GameSeatedPlayer(factory.createNewPlayer("cedric", 100), 50);
			guy = new GameSeatedPlayer(factory.createNewPlayer("guy",100), 50);
			table.addPlayer(kenzo);
			table.addPlayer(cedric);
			gameControl = new PlayingTableState(pokerTable, table, kenzo);

			
		} catch (IllegalValueException e) {
			fail(e.getMessage());
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		try {
			gameControl.deal(kenzo);
			gameControl.call(kenzo);
			gameControl.check(cedric);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}
	

}
