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
package org.cspoker.client.bots;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.simple.CallBot;
import org.cspoker.client.bots.bot.simple.RuleBasedBot;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.bots.listener.ReSitInBotListener;
import org.cspoker.client.bots.listener.SpeedTestBotListener;
import org.cspoker.client.common.SmartClientContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.DefaultLobbyListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Log4JPropertiesLoader;

public class BotRunner
		implements LobbyListener {
	
	static {
		Log4JPropertiesLoader.load("org/cspoker/client/bots/logging/log4j.properties");
	}
	
	private final static Logger logger = Logger.getLogger(BotRunner.class);

	private final BotFactory[] bots;

	private final SmartLobbyContext[] botLobbies;
	private final PlayerId[] botIDs;

	protected final SmartLobbyContext directorLobby;

	private final ExecutorService executor;
	
	private volatile Bot bot1 = null;
	private volatile Bot bot2 = null;
	
	private int botIndex1 = 0;
	private int botIndex2 = 0;

	private final BotListener speedMinitor = new SpeedTestBotListener();
	
	public BotRunner(RemoteCSPokerServer cspokerServer){
		this(cspokerServer, new BotFactory[]{CallBot.getBotFactory(), RuleBasedBot.getBotFactory()});
	}
	
	public BotRunner(RemoteCSPokerServer cspokerServer, BotFactory[] bots) {
		try {
			this.bots = bots;
			botLobbies = new SmartLobbyContext[bots.length];
			botIDs = new PlayerId[bots.length];
			
			SmartClientContext director = new SmartClientContext(cspokerServer.login("director", "test"));
			directorLobby = director.getLobbyContext(BotRunner.this);
			
			for(int i=0;i<bots.length;i++){
				SmartClientContext clientContext = new SmartClientContext(cspokerServer.login(bots[i].toString(), "test"));
				botLobbies[i] = clientContext.getLobbyContext(new DefaultLobbyListener());
				botIDs[i] = clientContext.getAccountContext().getPlayerID();
			}
			
			executor = Executors.newSingleThreadExecutor();
		
			iterateBots();
			
		} catch (LoginException e) {
			throw new IllegalStateException("Login Failed",e);
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		}
		
	}
	
	private void iterateBots() {
		if(botIndex2<bots.length-1){
			botIndex2++;
			playOnNewtable();
		}else if(botIndex2<bots.length-1){
			botIndex1++;
			botIndex2 = botIndex1+1;
			playOnNewtable();
		}else{
			shutdown();
		}
	}
	

	private void playOnNewtable() {
		try {
			TableId tableId = directorLobby.createHoldemTable(bots[botIndex1].toString()+" vs "+bots[botIndex2].toString(), 
					new TableConfiguration(AbstractBot.bigBlind)).getId();
			bot1 = bots[botIndex1].createBot(botIDs[botIndex1], tableId, botLobbies[botIndex1], executor, 
					new ReSitInBotListener(this), speedMinitor );
			bot1.start();
			bot2 = bots[botIndex2].createBot(botIDs[botIndex2], tableId, botLobbies[botIndex2], executor);
			bot2.start();
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalStateException("Server setup failed.", e);
		}
	}

	private void shutdown() {
		executor.shutdown();
	}

	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
		logger.info(tableCreatedEvent.getTable().getName()+" table created.");
	}
	
	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
		logger.info("Table " + tableRemovedEvent.getTableId()+" removed.");
	}

	public void respawnBots() {
		bot1.stop();
		bot2.stop();
		playOnNewtable();
	}
	
}
