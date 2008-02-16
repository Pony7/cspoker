import javafx.ui.*;
import java.lang.System;
import org.client.ClientCore;
		
        class Login {
            attribute name: String;
            attribute passw: String;
            attribute url: String;
            attribute port: String;
        }

        var model = Login {
            name: "Test"
            passw: "test"
            url: "localhost"
            port: "8080" 
        };
       	var client= new ClientCore();

        Frame {
            title: "Login Screen"
            width: 300
            height: 400
            visible: true
            menubar: MenuBar {
                 menus: Menu {
                     text: "Options"
                     mnemonic: F
                     items:MenuItem {
                         text: "Exit"
                         mnemonic: C
                         action: operation() {
                             System.exit(0);
                         }
                         }
                     }
            }
            content: BorderPanel{
            	center:GroupPanel{
            	var usernameRow= Row{alignment: BASELINE}
            	var passwRow= Row{alignment: BASELINE}
            	var urlRow= Row{alignment: BASELINE}
            	var portRow= Row{alignment: BASELINE}
            	
            	var labelsColumn= Column{ alignment:TRAILING}
            	var fieldsColumn= Column {alignment:LEADING resizable:true }
            	
            	rows: [usernameRow,passwRow,urlRow,portRow]
            	columns: [labelsColumn,fieldsColumn]
            	
            	content:
            	[SimpleLabel{
            		row: usernameRow
            		column: labelsColumn
            		text: "User name:"
            		horizontalAlignment: CENTER
            	},
            	TextField{
            		row: usernameRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		columns: 25
            		value: bind model.name
            	},
            	SimpleLabel{
            		row: passwRow
            		column: labelsColumn
            		text: "Password:"
            		horizontalAlignment: CENTER
            	},
            	PasswordField{
            		row: passwRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		columns: 25
            		value: bind model.passw
            	},
            	SimpleLabel{
            		row: urlRow
            		column: labelsColumn
            		text: "Server URL:"
            		horizontalAlignment: CENTER
            	},
            	TextField{
            		row: urlRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		columns: 25
            		value: bind model.url
            	},
            	SimpleLabel{
            		row: portRow
            		column: labelsColumn
            		text: "Server port:"
            		horizontalAlignment: CENTER
            	},
            	TextField{
            		row: portRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		
            		columns: 25
            		value: bind model.port
            	}
            	]
            }
            top:SimpleLabel {
            					horizontalAlignment: CENTER
                                icon: Image {url:"./images/cspoker8.jpg"}
            }

            bottom:FlowPanel{
            	content:
            		Button {
                    text: "Login"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Click this button to login"
                    action: operation() {
                         System.out.println("login");
                         client.login(model.url,model.port,model.name,model.passw);
                    }
                }
            }
            }
        };