package models.connection
{
	
	import cs.*;
	
	import flash.events.*;
	import flash.net.*;
	import flash.security.*;
	import flash.xml.*;
	
	import models.*;
	
	import mx.rpc.xml.SimpleXMLDecoder;
		
	public class MessageCenter extends Object
	{
		private var contentObj:Object;
		private var idAction:int = 0;
		private var socket:XMLSocket = null;
		
		private var mTables:CSTableList = null;
		private var mTable:CSTable = null;

		
		public function MessageCenter()
		{
			return;
		}
	
	
		public function setSocket(socket:XMLSocket):void{
			this.socket = socket;
		}
	
		public function handleDisconnect() : void
        {
            Main.showServerMessage("You have been disconnected from the server.  Please reload the application and try again...");
            Main.showConnectionBox();
            return;
        }// end function
        	
		public function parseDataIn(strIn:String):void{
			trace("dataReceived: " );
			var xmlDoc:XMLDocument = new XMLDocument(strIn);
			var decoder:SimpleXMLDecoder=new SimpleXMLDecoder(true);
					
			try{
				contentObj=decoder.decodeXML(xmlDoc);			
			}catch(e:Error){
				trace("error decoding xml: " + strIn);
				return;
			}
			var objAction:Object;
			var objResult:Object;
			
	
			if (contentObj.hasOwnProperty("login")){
				objResult=	contentObj.login;
				
				//dispatchEvent(new csEventActions( csEventActions.OnLogin,objAction,objResult));
				
				Main.lobby.receiveLobbyMessage(objAction, objResult);
				return;
			}
			
			if (contentObj.hasOwnProperty("successfulInvocationEvent")){
			 	trace("successfulInvocationEvent");
			 	
		 		switch (contentObj.successfulInvocationEvent.action["xsi:type"]){	
			 		case "getTablesAction":
						objAction=contentObj.successfulInvocationEvent.action;
						objResult=contentObj.successfulInvocationEvent.result.tables;
						mTables = new CSTableList(contentObj.successfulInvocationEvent.result.tables);
				  		//dispatchEvent(new csEventActions( csEventActions.OnGetTablesAction,"getTablesAction",mTables));
				  		Main.lobby.receiveLobbyMessage(objAction, objResult);
				  		break;
				
					case "getTableAction":
				  		objAction=contentObj.successfulInvocationEvent.action;
						objResult=contentObj.successfulInvocationEvent.result;
						mTable = new CSTable(contentObj.successfulInvocationEvent.result);
				  		//dispatchEvent(new csEventActions( csEventActions.OnGetTableAction,"getTableAction",mTable));
				  		break;
				
					case "joinTableAction":
						objAction=contentObj.successfulInvocationEvent.action;
						objResult=contentObj.successfulInvocationEvent.result;
						mTable = new CSTable(contentObj.successfulInvocationEvent.result);
						//dispatchEvent(new csEventActions( csEventActions.OnJoinTableAction,"joinTableAction",mTable));
					break;
					
					case "leaveTableAction":
						objAction=contentObj.successfulInvocationEvent.action;
						objResult=contentObj.successfulInvocationEvent.result;
						//dispatchEvent(new csEventActions( csEventActions.OnLeaveTableAction,"leaveTableAction",objResult));
						break;
					
					case "createTableAction":
						objAction=contentObj.successfulInvocationEvent.action;
						objResult=contentObj.successfulInvocationEvent.result;
						mTable = new CSTable(contentObj.successfulInvocationEvent.result);
						//dispatchEvent(new csEventActions( csEventActions.OnCreateTableAction,"createTableAction",mTable));
						break;
				}	 
				return;
			}
			 
			if (contentObj.hasOwnProperty("tableCreatedEvent")){
				objResult=contentObj.tableCreatedEvent.table;
			  	mTable = new CSTable(contentObj.tableCreatedEvent.table);
			  	//dispatchEvent(new csEventActions( csEventActions.OnGetTableAction,"tableCreatedEvent",mTable));
			  	return;
			}
			
			if (contentObj.hasOwnProperty("tableChangedEvent")){
				mTable = new CSTable(contentObj.tableChangedEvent.table);
			  	//dispatchEvent(new csEventActions( csEventActions.OnTableChangedEvent,"tableChangedEvent",mTable));
			  	return;
			}
			
			if (contentObj.hasOwnProperty("tableRemovedEvent")){
				//	var mTable:CSTable= new CSTable(contentObj.tableRemovedEvent);
			  	//dispatchEvent(new csEventActions( csEventActions.OnTableRemovedEvent,"tableRemovedEvent",contentObj.tableRemovedEvent.id));
			  	return;
			}
			
			if (contentObj.hasOwnProperty("playerJoinedTableEvent")){
				var objPlayer:CSPlayer= new CSPlayer(contentObj.playerJoinedTableEvent.player);
			  	//dispatchEvent(new csEventActions( csEventActions.OnPlayerJoinedTableEvent,"playerJoinedTableEvent",objPlayer));
			  	return;
			}
			
			if (contentObj.hasOwnProperty("playerLeftTableEvent")){
				var objPlayer:CSPlayer= new CSPlayer(contentObj.playerLeftTableEvent.player);
			  	//dispatchEvent(new csEventActions( csEventActions.OnPlayerLeftTableEvent,"playerLeftTableEvent",objPlayer));
			  	return;
			}
			
			if (contentObj.hasOwnProperty("illegalActionEvent")){
				objAction=contentObj.illegalActionEvent.action;
				objResult=contentObj.illegalActionEvent.msg;
				//dispatchEvent(new csEventActions( csEventActions.OnIllegalActionEvent,objAction,objResult));
				return;
			}
			
			
			// game logic (goes to tableModel)		
			if (contentObj.hasOwnProperty("newDealEvent")){
				objResult=contentObj.newDealEvent;
				var objEvent:CSGameEvent=new CSGameEvent("newDealEvent",objResult);
				//dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"newDealEvent",objEvent));
			}
			
			if (contentObj.hasOwnProperty("newRoundEvent")){
				objResult=contentObj.newRoundEvent;
				var objEvent:CSGameEvent=new CSGameEvent("newRoundEvent",objResult);
				//dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"newRoundEvent",objEvent));
			}
			
			if (contentObj.hasOwnProperty("smallBlindEvent")){
				objResult=contentObj.smallBlindEvent;
				var objEvent:CSGameEvent=new CSGameEvent("smallBlindEvent",objResult);
				//dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"smallBlindEvent",objEvent));
			}
			
			if (contentObj.hasOwnProperty("bigBlindEvent")){
				objResult=contentObj.bigBlindEvent;
				var objEvent:CSGameEvent=new CSGameEvent("bigBlindEvent",objResult);
				//dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"bigBlindEvent",objEvent));
			}
			
			if (contentObj.hasOwnProperty("newPocketCardsEvent")){
				objResult=contentObj.newPocketCardsEvent;
				var objEvent:CSGameEvent=new CSGameEvent("newPocketCardsEvent",objResult);
				//dispatchEvent(new csEventActions( csEventActions.OnNewPocketCardsEvent,"newPocketCardsEvent",objEvent));
				
			}
		}// end function
		
		
		

		
	}//end class
}//end package