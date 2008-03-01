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
import org.cspoker.client.gui.javafx.elements.TableInterface;
import org.cspoker.client.gui.javafx.elements.TableItem;
import java.rmi.RemoteException;
import org.cspoker.common.exceptions.IllegalActionException;

class TableSelection {
    attribute client:JavaFxClient;
    attribute main:Main inverse Main.table_selection;
    
    attribute screen:Frame;
    
    attribute tables: TableItem*;
    attribute selection: Integer;
    attribute active: Boolean;
    
    operation join_table();
    operation create_table();
    operation refresh();
    operation relogin();
}

trigger on new TableSelection{
    active = false;
    screen=Frame{
        title: "Tables"
        width: 400
        height: 400
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
                    text: "Logout"
                    mnemonic: L
                    action: operation() {
                        relogin();
                    }
                }]
            }
        }
        content: BorderPanel{
            center:Table{
                selection:bind this.selection
                columns:
                    [TableColumn {
                    text: "TableId"
                },
                TableColumn {
                    text: "# Players"
                },
                TableColumn {
                    text: "Small/Big Blind"
                    width: 100
                }]
                cells: bind foreach(t in tables)
                [TableCell {
                    text:bind t.Id.toString()
                },
                TableCell{
                    text:bind t.nb.toString()
                },
                TableCell{
                    text:bind "{t.sB}/{t.bB}"
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
                        create_table();
                    }
                    enabled: bind active
                },
                Button {
                    text: "Refresh Table List"
                    toolTipText: "Refresh the table list from the server"
                    action: operation() {
                        refresh();
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
        client.joinTable(selection);
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

operation TableSelection.create_table(){
    active = false;
    try{
        client.createTable();
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

operation TableSelection.refresh(){
    active = false;
    var t=client.getTableList();
    delete this.tables;
    for(table in t){
        var ta=new TableItem{
            Id:table.getId(),
            nb:table.getNbPlayers(),
            sB:table.getSmallBlind(),
            bB:table.getBigBlind()
        };
        insert ta into tables;
    }
    active = true;
}