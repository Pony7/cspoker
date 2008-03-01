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
import org.cspoker.common.exceptions.IllegalActionException;

class Login {
    operation init(c:JavaFxClient,m:Main);
    operation login();
    attribute name: String;
    attribute passw: String;
    attribute connection: String;
    attribute screen:Frame;
    attribute client:JavaFxClient;
    attribute prog:Main;
    attribute loginable:Boolean;
}
operation Login.init(c:JavaFxClient,m:Main){
    
    var provider = CommunicationProvider.global_provider;
    new LoadProvidersFromXml(provider);
    var providers:String* = foreach(prov in provider.getProviders().toArray()) prov.toString();
    
    name= "";
    passw= "";
    client=c;
    prog=m;
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
                    value: bind passw
                }
                ]
            }
            top:SimpleLabel {
                horizontalAlignment: CENTER
                icon: Image {url:"./org/cspoker/client/gui/javafx/images/cspoker10.jpg"
                
                }
            }
            bottom:FlowPanel{
                content:
                    Button {
                    text: "Login"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Click this button to login"
                    defaultButton: true
                    enabled: bind loginable
                    action: operation() {
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
        client.login(connection,name,passw);
        prog.logged_in();
        loginable = true;
    }catch(e:IllegalArgumentException){
        System.out.println(e);
        MessageDialog{
            title: "Login failed"
            visible: true
            message: e.getMessage()
            messageType: ERROR
            onClose: operation(){
                loginable = true;
            }
        }
    }catch(e:RuntimeException){
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