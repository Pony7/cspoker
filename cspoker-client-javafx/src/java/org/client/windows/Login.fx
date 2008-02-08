import javafx.ui.*;
		
        class Login {
            attribute name: String;
            attribute passw: String;
            attribute url: String;
            attribute port: String;
        }

        var login = Login {
            name: "Test"
            passw: "test"
            url: "localhost"
            port: "8080"
        };

        Frame {
            title: "Login Screen"
            width: 200
            height: 300
            visible: true
            content: GroupPanel{
            	var usernameRow= Row{alignment: BASELINE}
            	var passwRow= Row{alignment: BASELINE}
            	var urlRow= Row{alignment: BASELINE}
            	var portRow= Row{alignment: BASELINE}
            	
            	var labelsColumn= Column{ alignment:TRAILING}
            	var fieldsColumn= Column {alignment:LEADING resizable:true }
            	
            	rows: [usernameRow,passwRow]
            	columns: [labelsColumn,fieldsColumn]
            	
            	content:
            	[SimpleLabel{
            		row: usernameRow
            		column: labelsColumn
            		text: "User name:"
            	},
            	TextField{
            		row: usernameRow
            		column: fieldsColumn
            		
            		columns: 25
            		value: bind login.name
            	},
            	SimpleLabel{
            		row: passwRow
            		column: labelsColumn
            		text: "Password:"
            	},
            	PasswordField{
            		row: passwRow
            		column: fieldsColumn
            		
            		columns: 25
            		value: bind login.passw
            	},
            	SimpleLabel{
            		row: urlRow
            		column: labelsColumn
            		text: "Server URL:"
            	},
            	TextField{
            		row: urlRow
            		column: fieldsColumn
            		
            		columns: 25
            		value: bind login.url
            	}
            	]
            }
        };