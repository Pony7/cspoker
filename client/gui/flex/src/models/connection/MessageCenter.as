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
		
		public var loginActionId:int = 0;
		public var joinTableActionId:int = 0;
		
		private var loggedIn:Boolean = false;

		
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
			
			
			var xmlDoc:XMLDocument = new XMLDocument(strIn);
			
			var decoder:SimpleXMLDecoder=new SimpleXMLDecoder(true);
					
			try{
				contentObj=decoder.decodeXML(xmlDoc);			
			}catch(e:Error){
				trace("error decoding xml: " + strIn);
				return;
			}
			var traceContentObj:Object = contentObj;
			var objAction:Object;
			var objResult:Object;
			
	
			if (contentObj.hasOwnProperty("actionPerformedEvent") && contentObj.actionPerformedEvent.id == loginActionId){
				trace("LOGIN SUCCESSFUL!!!");
				Main.lobby.receiveLoginSucess();
				return;
			}
			
			if (contentObj.hasOwnProperty("actionPerformedEvent") && contentObj.actionPerformedEvent.id == joinTableActionId){
				trace("JOIN TABLE SUCCESSFUL!!!");
				//var tableId:int = contentObj.actionPerformedEvent.id;
				Main.table.receiveJoinTableSuccess();
				return;
			}
			
			if (contentObj.hasOwnProperty("actionPerformedEvent") && contentObj.actionPerformedEvent.hasOwnProperty("result")){
			 	trace("result returned");
			 	var resultType:String = contentObj.actionPerformedEvent.result["xsi:type"];
			 	var result:Object = contentObj.actionPerformedEvent.result;
		 		switch (resultType){	
			 		case "ns2:tableList":
						
						//mTables = new CSTableList(result.tables);
						
				  		//dispatchEvent(new csEventActions( csEventActions.OnGetTablesAction,"getTablesAction",mTables));
				  		Main.lobby.receiveTablesList(result);
				  		break;
				
					case "ns2:detailedHoldemTable":
				  		
						//mTable = new CSTable(contentObj.successfulInvocationEvent.result);
				  		//dispatchEvent(new csEventActions( csEventActions.OnGetTableAction,"getTableAction",mTable));
				  		Main.lobby.receiveDetailedTableInfo(result);
				  		
				  		break;
				
					case "joinTableAction":
						
						
						
						//mTable = new CSTable(contentObj.successfulInvocationEvent.result);
						//dispatchEvent(new csEventActions( csEventActions.OnJoinTableAction,"joinTableAction",mTable));
					break;
					
					case "leaveTableAction":
						objAction=contentObj.successfulInvocationEvent.action;
						objResult=contentObj.successfulInvocationEvent.result;
						//dispatchEvent(new csEventActions( csEventActions.OnLeaveTableAction,"leaveTableAction",objResult));
						break;
					
					case "ns2:createTableAction":
						Main.showServerMessage("TABLE CREATED!!!!");
						break;
				}	 
				return;
			}
			
			if(contentObj.hasOwnProperty("holdemTableTreeEventWrapper")){
				// AFTER JOIN TABLE, RECEIVE THESE....
				var event:Object = contentObj.holdemTableTreeEventWrapper.event;
				var eventType:String = event["xsi:type"];
				
				var seatId:int;
				// SWITCH STATEMENT FOR HOLDEM TABLE EVENTS...
				switch(eventType){
					case "ns2:sitInEvent":
						seatId = event.player.seatId;
						trace("sitInEvent received!!! Seat:" + String(seatId));
						Main.table.getSeatById(seatId).receiveSitIn(event.player);
						break;
					case "ns2:newDealEvent":
						trace("newDealEvent received!!!");
						break;
					
					
					
					
				}
				
			}
			 
			
			
			if (contentObj.hasOwnProperty("tableChangedEvent")){
				/* TODO:  ADD IN TABLECHANGEDEVENT"
				*/
				
				//mTable = new CSTable(contentObj.tableChangedEvent.table);
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