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
import org.cspoker.client.gui.javafx.eventlisteners.EventListener;

class Main{
    attribute mainclient: JavaFxClient;
    attribute login: Login inverse Login.prog;
    attribute table_selection: TableSelection inverse TableSelection.main;
    attribute gametable: GameTable inverse GameTable.main;
    operation init();
    operation logged_in();
    operation relogin();
    operation table_selected();
}

operation Main.logged_in(){
    var temp = EventListener{
        mainstate: bind gametable.state 
        events: bind gametable.events
    };
    mainclient.subscribeAllEvents(temp.listener);
    login.screen.hide();
    table_selection = TableSelection{
        client: bind mainclient
    };
    table_selection.refresh();
}

operation Main.table_selected(){
    table_selection.screen.hide();
    gametable = GameTable{
        client: bind mainclient
    };
}

operation Main.relogin(){
    table_selection.screen.visible = false;
    gametable.screen.visible = false;
    login.screen.visible = true;
}

Main{
    var: me
    mainclient: new JavaFxClient()
    login: Login{
        client: bind me.mainclient
        main: me
    }
}