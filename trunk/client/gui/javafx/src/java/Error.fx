import javafx.ui.*;
import java.lang.*;

//Error Window part
		class Error {
            attribute cause: String;
            attribute showing: Boolean;
        }
        var lastError= Error{
        	cause: "-----"
        	showing: false
        };
        var errorFrame=Frame{
        	background: Color{
        		red: 1
        		}
        	resizable: false
        	centerOnScreen:true
        	title: "Error"
        	width: 400
            height: 100
            visible: bind lastError.showing
            onClose: operation(){
            	lastError.showing=false;
            }
            disposeOnClose: false
        	content: GroupPanel{
        		halign: CENTER
        		valign: CENTER
        		var causeRow= Row{alignment: BASELINE}
        		var buttonRow= Row{alignment: CENTER}
        		
        		var column= Column{ alignment:TRAILING}
            	
            	rows: [causeRow,buttonRow]
            	columns: [column]
            	
            	content:[
            	SimpleLabel{
            		row: causeRow
            		column: column
            		text:bind lastError.cause
        			horizontalAlignment: CENTER
            	},
            	Button {
            		row:buttonRow
            		column: column
                    text: "CONTINUE"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Click this button to return to the previous screen"
                    action: operation() {
                         System.out.println("error reported");
                         lastError.showing=false;
                        }
                }
            	]	
        	}
        };