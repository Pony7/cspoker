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

//package org.cspoker.client.gui.javafx;
import javafx.ui.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.*;
import org.cspoker.client.gui.javafx.views.*;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.client.gui.javafx.eventlisteners.EventListener;

class Main{
    attribute client: JavaFxClient;
    attribute login: Login inverse Login.main;
    attribute table_selection: TableSelection inverse TableSelection.main;
    attribute gametable: GameTable inverse GameTable.main;
    attribute listener: EventListener inverse EventListener.main;
    attribute state: GameView;
    
    operation init();
    operation logged_in();
    operation relogin();
    operation table_selected();
    operation resetState();
    operation leftTable();
}
operation Main.logged_in(){
    listener = EventListener{};
    this.client.subscribeAllEvents(listener.listener);
    login.screen.visible = false;
    state.tables = TableView{}.toTableViews(client.getTableList());
    table_selection = TableSelection{};
}

operation Main.leftTable(){
    gametable.screen.visible = false;
    state.tables = TableView{}.toTableViews(client.getTableList());
    table_selection.screen.visible = true;
}

operation Main.table_selected(){
    table_selection.screen.visible = false;
    gametable = GameTable{};
}

operation Main.relogin(){
    table_selection.screen.visible = false;
    gametable.screen.visible = false;
    resetState();
    login.screen.visible = true;
}

Main{
    var: me
    client: new JavaFxClient()
    login: Login{
        // inverse doesn't seem to work in this case ...
        main: me
    }
}

trigger on new Main{
    resetState();
}

operation Main.resetState(){
    state = GameView{
        events: "Welcome to CSPoker!<br/>"
        myname: "guy"
        tables: []
        mytable: bind state.tables[t | t.id.equals(this.state.mytableid)][0]
        me: bind state.mytable.players[p | p.name.equals(state.myname)][0]
    };
}