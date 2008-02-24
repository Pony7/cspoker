/*
 * Main.fx
 *
 * Created on 19-feb-2008, 18:02:50
 */
/**
 * @author Cedric
 */
package org.client;

import javafx.ui.*;
import java.lang.*;
import org.client.*;
import org.client.game.Game;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameEvents.GameMessageEvent;
import org.cspoker.common.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameEvents.NewDealEvent;
import org.cspoker.common.events.gameEvents.NewRoundEvent;
import org.cspoker.common.events.gameEvents.NextPlayerEvent;
import org.cspoker.common.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameEvents.ShowHandEvent;
import org.cspoker.common.events.gameEvents.WinnerEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.common.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.common.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.common.events.serverEvents.PlayerLeftEvent;
import org.cspoker.common.events.serverEvents.ServerMessageEvent;
import org.cspoker.common.events.serverEvents.TableCreatedEvent;

class Main{
		attribute client: JavaFxClient;
		attribute login: Login;
                attribute table_selection: TableSelection;
                attribute game: Game;
                attribute listener:RemoteAllEventsListener;
                operation init();
                operation logged_in();
                operation table_selected();
}
var prog= new Main{
    login: Login,
    table_selection: TableSelection,
    client: new JavaFxClient()
};
prog.init();
operation Main.init(){
    login.init(client,this);
    listener=new RemoteAllEventsListener {
   operation onAllInEvent(e:AllInEvent){
       
   }
   operation onBetEvent(e:BetEvent){
       
   }
   operation onBigBlindEvent(e:BigBlindEvent){
       
   }
   operation onCallEvent(e:CallEvent){
       
   }
   operation onCheckEvent(e:CheckEvent){
       
   }
   operation onFoldEvent(e:FoldEvent){
       
   }
   operation onRaiseEvent(e:RaiseEvent){
       
   }
   operation onSmallBlindEvent(e:SmallBlindEvent){
       
   }
   operation onNewPocketCardsEvent(e:NewPocketCardsEvent){
       
   }
   operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent){
       
   }
   operation onNewDealEvent(e:NewDealEvent){
       
   }
   operation onNewRoundEvent(e:NewRoundEvent){
       
   }
   operation onNextPlayerEvent(e:NextPlayerEvent){
       
   }
   operation onPlayerJoinedGameEvent(e:PlayerJoinedGameEvent){
       
   }
   operation onPlayerLeftTableEvent(e:PlayerLeftTableEvent){
       
   }
   operation onShowHandEvent(e:ShowHandEvent){
       
   }
   operation onWinnerEvent(e:WinnerEvent){
       
   }
   operation onGameMessageEvent(e:GameMessageEvent){
       
   }
   operation onPlayerJoinedEvent(e:PlayerJoinedEvent){
       
   }
   operation onPlayerLeftEvent(e:PlayerLeftEvent){
       
   }
   operation onTableCreatedEvent(e:TableCreatedEvent){
       
   }
   operation onServerMessageEvent(e:ServerMessageEvent){
       
   }
};
    client.subscribeAllEvents(listener);
}
operation Main.logged_in(){
    login.screen.hide();
    System.out.println("Succesfull login");
    table_selection.init(client,this);
}
operation Main.table_selected(){
    
}
