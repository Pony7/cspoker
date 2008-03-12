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
import org.cspoker.client.common.CommunicationProvider;
import org.cspoker.client.allcommunication.LoadProvidersFromXml;
import org.cspoker.client.gui.javafx.JavaFxClient;

class Login {
    attribute main:Main inverse Main.login;
    
    attribute screen:Frame;
    attribute passw: String;
    attribute connection: String;
    attribute loginable:Boolean;
    
    operation login();
}
trigger on new Login{
    var provider = CommunicationProvider.global_provider;
    new LoadProvidersFromXml(provider);
    var providers:String* = foreach(prov in provider.getProviders().toArray()) prov.toString();
    passw = "test";
    loginable = true;
    screen=Frame{
        title: "Login"
        width: 300
        height: 320
        visible: true
        centerOnScreen: true
        onClose: operation() {System.exit(0);}
        background: new Color(50/255,153/255,0,1)
        content: BorderPanel{
            top:SimpleLabel {
                horizontalAlignment: CENTER
                icon: Image {url:"./org/cspoker/client/gui/javafx/images/cspoker10.jpg"
                    
                }
            }
            center:GroupPanel{
                var dropdownRow= Row{alignment: BASELINE}
                var usernameRow= Row{alignment: BASELINE}
                var passwRow= Row{alignment: BASELINE}
                
                var labelsColumn= Column{ alignment:TRAILING}
                var fieldsColumn= Column {alignment:LEADING resizable:true }
                
                rows: [dropdownRow,usernameRow,passwRow]
                columns: [labelsColumn,fieldsColumn]
                
                content:[
                SimpleLabel{
                    row: dropdownRow
                    column: labelsColumn
                    text: "Connection:"
                    horizontalAlignment: LEADING
                },
                ComboBox {
                    row: dropdownRow
                    column: fieldsColumn
                    editable: true
                    value: bind connection
                    cells: foreach(str in providers)
                    ComboBoxCell{
                        text: str
                        value: str
                    }
                    
                },
                SimpleLabel{
                    row: usernameRow
                    column: labelsColumn
                    text: "User name:"
                    horizontalAlignment: LEADING
                },
                TextField{
                    row: usernameRow
                    column: fieldsColumn
                    value: bind main.state.myname
                },
                SimpleLabel{
                    row: passwRow
                    column: labelsColumn
                    text: "Password:"
                    horizontalAlignment: LEADING
                },
                PasswordField{
                    row: passwRow
                    column: fieldsColumn
                    value: bind passw
                }
                ]
            }
            bottom:FlowPanel{
                content:
                    Button {
                    text: "Login"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Log in to the server"
                    defaultButton: true
                    enabled: bind loginable
                    action: bind operation() {
                        login();
                    }
                }
            }
        }
    };
}
operation Login.login(){
    try{ 
        loginable = false;
        main.client.login(connection,main.state.myname,passw);
        main.logged_in();
        loginable = true;
    }catch(e:Exception){
        e.printStackTrace();
        MessageDialog{
            title: "Login failed"
            visible: true
            message: e.getMessage()
            messageType: ERROR
            onClose: operation(){
                loginable = true;
            }
        }
    }
}