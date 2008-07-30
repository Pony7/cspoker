// ActionScript file
import flash.events.KeyboardEvent;
import flash.ui.Keyboard;
import flash.xml.*;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.FlexEvent;


private var csComm:csCommunicator= new csCommunicator();

 

public function Initialize():void{
 
 	
 	// txtMessage.addEventListener(FlexEvent.CREATION_COMPLETE,controlCreated);
 	
	 
			
}

private function controlCreated(event:FlexEvent):void
{
		//txtMessage.addEventListener(KeyboardEvent.KEY_UP, OnKeyUp);
		//OutputTextArea.addEventListener(FlexEvent.UPDATE_COMPLETE, OnOutputScroll);
}

public function OnKeyUp(event:KeyboardEvent):void
{
	// If the user hits ENTER, the message should be sent.
	if (event.keyCode == Keyboard.ENTER)
	{
		OnSendClick();
	}
}


public function OnOutputScroll(event:Event):void
{
	// Update output window to scroll to last line.
	OutputTextArea.verticalScrollPosition = OutputTextArea.maxVerticalScrollPosition;
}



public function DoConnect():void
{
	
	
	
	var dataServer:Array =  txtServer.text.split(':');
	
	if	(btnConnect.label=="Connect"){
	 
	 btnConnect.label="Disconnect"
	 
	 csComm.addEventListener(csEvent.OnConnectionsStatus,OnConnectionStatus);
	 
	 csComm.csConnect(dataServer[0],dataServer[1]);
	 
	 SetupListeners();
	 
	 }
	 else
	 {
	 
	 btnConnect.label="Connect"
	 
	 csComm.csDisconnect();
	 
	 }
	 
}

public function DoLogin():void{
	
		vsMain.selectedChild=cnvMain;
					
		csComm.csSendLogin(txtLoginUser.text,txtLoginPass.text);
		
		
}


private function AppendData(strIn:String):void
{
	OutputTextArea.text += strIn + '\n';    
}



public function OnSendClick():void
{
	
	var input:String=txtMessage.text;		
	var inputArray:Array;
	
	inputArray=input.split(" "); 
	
	
	switch (inputArray[0]){
	
	case "gettables":
	
		csComm.csGetTablesAction();	
		AppendData(input);
		AppendData("------------------------");
		break;
	
	case "gettable":
		
		
		AppendData(input);
		AppendData("------------------------");
		
		if (inputArray.length<2) {
		
			input="Error"
			AppendData(input);
			AppendData("------------------------");
		}
		else
		{	
									
			csComm.csGetTableAction(inputArray[1]);
		
		}
	
		break;
	
	case "jointable":
				
		AppendData(input);
		AppendData("------------------------");
				
		if (inputArray.length<2) {		
			input="Error"
			AppendData(input);
			AppendData("------------------------");
		}
		else
		{										
			csComm.csJoinTableAction(inputArray[1]);														
		}
	
		break;			
		
	case "leavetable":
				
		AppendData(input);
		AppendData("------------------------");
				
		if (inputArray.length<2) {		
			input="Error"
			AppendData(input);
			AppendData("------------------------");
		}
		else
		{										
			csComm.csLeaveTableAction (inputArray[1]);														
		}
	
		break;				
	
	case "createtable":
				
		AppendData(input);
		AppendData("------------------------");
				
		if (inputArray.length<2) {		
			input="Error"
			AppendData(input);
			AppendData("------------------------");
		}
		else
		{										
			csComm.csCreateTableAction( inputArray[1]);														
		}
	
		break;		
	
	}
	
	txtMessage.text="";	
		
}
        
private function StripString(input:String):String
{
	 	
 	input = input.replace(/\r*$/g, "");	
 	return input;
}
//-----------------------------------------------------

private function SetupListeners():void
{
	
	csComm.addEventListener(csEvent.OnLogin,OnLogin);
	csComm.addEventListener(csEvent.OnGetTablesAction,OnGetTablesAction);
	csComm.addEventListener(csEvent.OnGetTableAction,OnGetTableAction);
	
	//csComm.addEventListener(csEvent.OnJoinTableAction,OnGetTableAction);
	csComm.addEventListener(csEvent.OnJoinTableAction,OnTableAction);
	csComm.addEventListener(csEvent.OnLeaveTableAction,OnTableAction);
	csComm.addEventListener(csEvent.OnCreateTableAction,OnTableAction);

	
	
	
	csComm.addEventListener(csEvent.OnIllegalActionEvent,OnIllegalActionEvent);
	
}

private function OnConnectionStatus(event:csEvent):void
{
	
	
	
	switch (event.dataResult){
	
	case "ERROR":
		Alert.show( "Connection Error");
		break;
	case "SUCCESS":
	
		Alert.show( "Connection Successful");
		break;
	}

}

private function OnIllegalActionEvent(event:csEvent):void{
	
		var objMsg:Object=event.dataResult;
	
		AppendData(objMsg.toString());
		AppendData("------------------------");
}
private function OnLogin(event:csEvent):void{
	
}
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnGetTablesAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnGetTablesAction(event:csEvent):void{
	
		var objDataResult:Object=event.dataResult; //Data In from Server
		
		var objTables:ArrayCollection;		
		var	objTable:Object;
		
		if (objDataResult !=null )
		{
		
		
		
		//!!!!!!!!!!!!!!!!!!!!VERY IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// That a confirmed bug in FLEX
		// ObjectProxy with 0 or 1 elements in collection
		// will trown error in casting to ArrayCollection
		//bellow is workout to resolve that problem
		
		
		
			objTables=objDataResult.table as ArrayCollection;
				
     			if (objTables == null)
     			{
          	 		objTables = new ArrayCollection([objDataResult.table]);
     			}
		
		// End of casting ObjectProxy to ArrayCollection		
		//--------------------------------------------------------------
		
		
				
		for each (objTable in objTables){
		
			var objProperty:Object=objTable.property;
			var objPlayers:ArrayCollection;	
		
			var tableinfo:String;
			var propertyinfo:String;
			var playerinfo:String;
		
		
			
			tableinfo ="id:" + objTable.id + " name:" + objTable.name + " playing:" + objTable.playing; 	
			propertyinfo ="bigBet:" + objProperty.bigBet + " bigBlind:" + objProperty.bigBlind + " smallBet:" + objProperty.smallBet + " smallBlind:" + objProperty.smallBlind + " delay:" + objProperty.delay; 
		
			AppendData(tableinfo);
			AppendData(propertyinfo);
			
			
			
			//!!!!!!!!!!!!!!!!!!!!VERY IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// !!! Same workout for casting ObjectProxy to ArrayCollection
		
			objPlayers=objTable.players.player as ArrayCollection;
		
			if (objPlayers == null)
     		{
          		 objPlayers = new ArrayCollection([objTable.players.player]);
          		 
     		}
		
		// End of casting ObjectProxy to ArrayCollection
		//--------------------------------------------------------------	
			
			var objPlayer:Object;
			
			for each (objPlayer in objPlayers)
			{
				playerinfo="name:" + objPlayer.name + " id:" + objPlayer.id + " seatId:" + objPlayer.seatId + " stackValue:" + objPlayer.stackValue + " betChipsValue:" + objPlayer.betChipsValue; 
				AppendData(playerinfo);
			}
			
			AppendData("------------------------");
		 			
			}	
		
		}
		else
			// We are here, if no tables 
		{
			
			AppendData("No tables");
			
			AppendData("------------------------");
		
		}
		
	
	
}

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnGetTableAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnGetTableAction(event:csEvent):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	var objPlayers:ArrayCollection;	
	var tableinfo:String;
	var playerinfo:String;
	
		
			AppendData(objDataAction.toString());
		
			
			
			tableinfo ="id:" + objDataResult.id + " name:" + objDataResult.name + " playing:" + objDataResult.playing;
			AppendData(tableinfo);
	
			if (objDataResult.players!=null){
			
				objPlayers=objDataResult.players.player as ArrayCollection;
				var objPlayer:Object;
			
				if (objPlayers == null)
     				{
          		 
          		 		objPlayers = new ArrayCollection([objDataResult.players.player]);
          		 
     				}	
	
				for each (objPlayer in objPlayers)
					{
					playerinfo="name:" + objPlayer.name + " id:" + objPlayer.id + " seatId:" + objPlayer.seatId + " stackValue:" + objPlayer.stackValue + " betChipsValue:" + objPlayer.betChipsValue; 
					AppendData(playerinfo);
				}
			
			}
			else
			// We are here, if no players
			{
				AppendData("No players");
			}
			
			AppendData("------------------------");
		 			
	
}
//
// Just generic event handler, to print message
//
private function OnTableAction(event:csEvent):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	AppendData(objDataAction.toString());
	AppendData("------------------------");
	
}
