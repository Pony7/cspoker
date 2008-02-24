/*
 * TableSelection.fx
 *
 * Created on 21-feb-2008, 23:34:58
 */

package org.client;
import javafx.ui.*;
import java.lang.*;
import org.client.elements.TableInterface;
import org.client.elements.TableItem;

/**
 * @author Cedric
 */

class TableSelection {
            attribute screen:Frame;
            attribute client:JavaFxClient;
            attribute prog:Main;
            attribute tables: TableItem*;
            attribute selection: Integer;
            operation init(c:JavaFxClient,m:Main);
            operation initScreen();
            operation select_table();
            operation refresh();
        }
        operation TableSelection.init(c:JavaFxClient,m:Main){
            client=c;
            refresh();
            initScreen();
        }
        operation TableSelection.initScreen(){
            screen=Frame{
            title: "Select your table"
            width: 300
            height: 400
            visible: true
            menubar: MenuBar {
                 menus: Menu {
                     text: "Options"
                     mnemonic: F
                     items:[MenuItem {
                     	sizeToFitRow: true
                         text: "Exit"
                         mnemonic: C
                         action: operation() {
                             System.exit(0);
                         }
                         },
                         MenuItem{
                           sizeToFitRow: true
                         text: "Re-Login"
                         mnemonic: L
                         action: operation() {
                             screen.hide();
                             Login.screen.show();
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
                cells: bind foreach (t in tables)
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
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Click this button to join the selected table"
                    action: operation() {
                         select_table();
                        }
                    },
                        Button {
                    text: "Refresh Table List"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Click this button to refresh the table list from the server"
                    action: operation() {
                         refresh();
                        }
                    }
                    ]
                }
               }
        };
        }

        operation TableSelection.select_table(){
            System.out.println("Attempt to join table {selection}");
            client.joinTable(selection);
        }
        operation TableSelection.refresh(){          
            var t=client.getTableList();
            delete this.tables;
            for(table in t){
                var ta=new TableItem{Id:table.getId(),
                nb:table.getNbPlayers(),
                sB:table.getSmallBlind(),
                bB:table.getBigBlind()};
                insert ta into tables;
            } 
        }