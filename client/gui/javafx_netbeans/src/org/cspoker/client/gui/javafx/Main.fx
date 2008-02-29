/*
 * Main.fx
 *
 * Created on 19-feb-2008, 18:02:50
 */
/**
 * @author Cedric
 */
package org.cspoker.client.gui.javafx;

import javafx.ui.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.*;
import org.cspoker.client.gui.javafx.game.Game;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameevents.ShowHandEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.events.serverevents.PlayerJoinedEvent;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;

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
