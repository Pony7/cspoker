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
import java.rmi.RemoteException;
import org.cspoker.common.exceptions.IllegalActionException;

class TableSelection {
    attribute main: Main inverse Main.table_selection;
    
    attribute screen: Frame;
    
    attribute selection: Integer;
    attribute active: Boolean;
    
    operation join_table();
    operation create_table(name:String);
    operation refresh();
    operation relogin();
}

trigger on new TableSelection{
    active = false;
    screen=Frame{
        title: "Tables"
        width: 450
        height: 450
        visible: true
        centerOnScreen: true
        onClose: operation() {System.exit(0);}
        menubar: MenuBar {
            menus: Menu {
                text: "Options"
                mnemonic: F
                items:[
                MenuItem{
                    sizeToFitRow: true
                    text: bind "Logout {main.state.me.name}"
                    mnemonic: L
                    action: operation() {
                        relogin();
                    }
                    enabled: bind active
                }]
            }
        }
        content: BorderPanel{
            center: Table{
                onMousePressed: operation(e){
                    if(e.clickCount>1){
                        join_table();
                    }
                }
                selection:bind this.selection
                columns: [TableColumn {
                    text: "Id"
                    width: 15
                },
                TableColumn {
                    text: "Name"
                },
                TableColumn {
                    text: "# Players"
                    width: 10
                },
                TableColumn {
                    text: "Blinds"
                    width: 15
                }]
                cells: bind foreach(t in main.state.tables)
                [TableCell {
                    value: bind t.id
                    text: bind t.id.getID().toString()
                },
                TableCell {
                    text: bind t.name
                },
                TableCell{
                    text: bind t.nbPlayers.toString()
                },
                TableCell{
                    text: bind "{t.smallBlind}/{t.bigBlind}"
                }]
            }
            top:FlowPanel{
                content:
                    [Button {
                    text: "Join Table"
                    toolTipText: "Join the selected table"
                    action: operation() {
                        join_table();
                    }
                    enabled: bind active
                },Button {
                    text: "Create Table"
                    toolTipText: "Create a new table"
                    action: operation() {
                        active = false;
                        CreateTable{
                            ts: this
                        }
                    }
                    enabled: bind active
                }
                ]
            }
        }
    };
}

operation TableSelection.relogin(){
    main.relogin();
}

operation TableSelection.join_table(){
    active = false;
    try{
        main.client.joinTable(main.state.tables[selection].id);
        main.state.tablename = main.state.tables[selection].name;
        main.table_selected();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to join table"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
    active = true;
}

operation TableSelection.create_table(name:String){
    active = false;
    try{
        main.client.createTable(name);
        main.state.tablename = name;
        main.table_selected();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to create table"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
    active = true;
}