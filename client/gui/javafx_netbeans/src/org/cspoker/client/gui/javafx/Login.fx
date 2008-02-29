/*
 * Login.fx
 *
 * Created on 19-feb-2008, 18:32:57
 */

package org.cspoker.client.gui.javafx;

/**
 * @author Cedric
 */

import javafx.ui.*;
import java.lang.*;
import org.cspoker.client.common.CommunicationProvider;
import org.cspoker.client.allcommunication.LoadProvidersFromXml;
import org.cspoker.client.gui.javafx.JavaFxClient;

class Login {
    operation init(c:JavaFxClient,m:Main);
    operation login();
    attribute name: String;
    attribute passw: String;
    attribute connection: String;
    attribute screen:Frame;
    attribute client:JavaFxClient;
    attribute prog:Main;
}
operation Login.init(c:JavaFxClient,m:Main){
    
    var provider = CommunicationProvider.global_provider;
    new LoadProvidersFromXml(provider);
    var providers:String* = foreach(prov in provider.getProviders().toArray()) prov.toString();
    
    name= "";
    passw= "";
    client=c;
    prog=m;
    screen=Frame{
        title: "Login"
        width: 300
        height: 350
        visible: true
        centerOnScreen: true
        onClose: operation() {System.exit(0);}
        content: BorderPanel{
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
                    horizontalAlignment: LEFT
                    value: bind name
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
                    horizontalAlignment: LEFT
                    value: bind passw
                }
                ]
            }
            top:SimpleLabel {
                horizontalAlignment: CENTER
                icon: Image {url:"./org/cspoker/client/gui/javafx/images/cspoker8.jpg"}
            }
            
            bottom:FlowPanel{
                content:
                    Button {
                    text: "Login"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Click this button to login"
                    action: operation() {
                        System.out.println("Log in");
                        login();
                    }
                }
            }
        }
    };
}
operation Login.login(){
    client.login(connection,name,passw);
    prog.logged_in();
}