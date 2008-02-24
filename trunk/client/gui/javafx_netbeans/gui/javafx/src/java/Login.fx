import javafx.ui.*;
import java.lang.*;
import org.client.ClientCore; 

        class Login {
            attribute name: String;
            attribute passw: String;
            attribute url: String;
            attribute port: String;
            attribute screen:Frame;
            attribute client:ClientCore;
        }
        operation Login.init(){
        	name= "Test";
            passw= "test";
            url= "localhost";
            port= "8080";
        }
        operation Login.registerClient(c:ClientCore){
        	client=c;
        }
        trigger on new Login{
              init();
              screen.hide();
        }
        
        operation Login.login():ClientCore{
        	client.login(url,port,name,passw);
        	return client;
        }
        attribute Login.screen=Frame{
        	centerOnScreen:true
            title: "Login Screen"
            width: 300
            height: 400
            visible: true
            menubar: MenuBar {
                 menus: Menu {
                     text: "Options"
                     mnemonic: F
                     items:MenuItem {
                     	sizeToFitRow: true
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
            		horizontalAlignment: LEADING
            	},
            	TextField{
            		row: usernameRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		columns: 25
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
            		horizontalAlignment: CENTER
            		columns: 25
            		value: bind passw
            	},
            	SimpleLabel{
            		row: urlRow
            		column: labelsColumn
            		text: "Server URL:"
            		horizontalAlignment: LEADING
            	},
            	TextField{
            		row: urlRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		columns: 25
            		value: bind url
            	},
            	SimpleLabel{
            		row: portRow
            		column: labelsColumn
            		text: "Server port:"
            		horizontalAlignment: LEADING
            	},
            	TextField{
            		row: portRow
            		column: fieldsColumn
            		horizontalAlignment: CENTER
            		
            		columns: 25
            		value: bind port
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
                         login();
                        }
                }
            }
            }
        };