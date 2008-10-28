// ActionScript file
import cs.CSCards;
import cs.CSGameEvent;
import cs.CSPlayer;
import cs.CSTable;
import cs.CSTableList;

import flash.events.KeyboardEvent;
import flash.ui.Keyboard;
import flash.xml.*;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.events.FlexEvent;

private var csComm:csCommunicator= new csCommunicator();
private var userName:String;

private var cards:CSCards=new CSCards;

private var _running:Boolean;

private var player0:CSPlayer;
private var player1:CSPlayer;



private var SeatedPlayer:CSPlayer;

public function Initialize():void{
 
 	
 		
 		
	 	txtMessage.addEventListener(KeyboardEvent.KEY_UP, OnKeyUp);
		OutputTextArea.addEventListener(FlexEvent.UPDATE_COMPLETE, OnOutputScroll);
		
		
//		imgFlop1.source=cards.getCard("CLUBS","DEUCE");
		
		
		vsMain.selectedChild=cnvLogin;
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
	 
	 csComm.addEventListener(csEventActions.OnConnectionsStatus,OnConnectionStatus);
	 
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
	
	var input:String=StripString( txtMessage.text);		
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
			csComm.csCreateTableAction( inputArray[1],30000);
			//csComm.csCreateTableAction( inputArray[1],120000);																
		}
	
		break;
	
	case "startgame":
	
		csComm.csStartGameAction();
		AppendData(input);
		AppendData("------------------------");
		break;		
	
	
	
	case "bet":
				
		AppendData(input);
		AppendData("------------------------");
				
		if (inputArray.length<2) {		
			input="Error"
			AppendData(input);
			AppendData("------------------------");
		}
		else
		{										
			csComm.csBetAction( inputArray[2]);														
		}
	
		break;
	
	case "raise":
				
		AppendData(input);
		AppendData("------------------------");
				
		if (inputArray.length<2) {		
			input="Error"
			AppendData(input);
			AppendData("------------------------");
		}
		else
		{										
			csComm.csRaiseAction( inputArray[2]);														
		}
	
		break;
		
	case "call":
	
		csComm.csStartGameAction();
		AppendData(input);
		AppendData("------------------------");
		break;
		
	case "fold":
	
		csComm.csStartGameAction();
		AppendData(input);
		AppendData("------------------------");
		break;
	
	default:
		
		AppendData(input);
	
}
	
	txtMessage.text="";	
		
}
//-------------------------------------------------------------------------------
// Game Actions
//-------------------------------------------------------------------------------

public function DoStartGameAction():void{
	
		
		
		btnAllIn.visible=true;
		btnAllIn.enabled=true;
		
		btnBet.visible=true;
		btnBet.enabled=true;
		
		btnCall.visible=true;
		btnCall.enabled=true;
		
		btnCheck.visible=true;
		btnCheck.enabled=true;
		
		btnFold.visible=true;
		btnFold.enabled=true;
		
		btnRaise.visible=true;
		btnRaise.enabled=true;
		
					
		csComm.csStartGameAction();
		
		
		
}

private function DoActionAllIn():void{
	
	//csComm.cs
}

private function DoActionRaise():void{
	
	csComm.csRaiseAction(Number( txtChips.text));
}

private function DoActionFold():void{
	
	csComm.csFoldAction();
	
}

private function DoActionCall():void{
	
	csComm.csCallAction();
}

private function DoActionCheck():void{
	
	csComm.csCheckAction();
}

private function DoActionBet():void{
	
	csComm.csBetAction( Number( txtChips.text));
}





//-------------------------------------------------------------------------------
// Game Actions
//-------------------------------------------------------------------------------
        

//-----------------------------------------------------

private function SetupListeners():void
{
	
	csComm.addEventListener(csEventActions.OnLogin,OnLogin);
	csComm.addEventListener(csEventActions.OnGetTablesAction,OnGetTablesAction);
	csComm.addEventListener(csEventActions.OnGetTableAction,OnGetTableAction);
	
	
	csComm.addEventListener(csEventActions.OnJoinTableAction,OnJoinTableAction);
	csComm.addEventListener(csEventActions.OnLeaveTableAction,OnLeaveTableAction);
	csComm.addEventListener(csEventActions.OnCreateTableAction,OnCreateTableAction);
	csComm.addEventListener(csEventActions.OnTableRemovedEvent,OnTableRemovedEvent);
	csComm.addEventListener(csEventActions.OnTableChangedEvent,OnTableChangedEvent);
	
	csComm.addEventListener(csEventActions.OnPlayerJoinedTableEvent,OnPlayerAction);
	csComm.addEventListener(csEventActions.OnPlayerLeftTableEvent,OnPlayerAction);
		
	csComm.addEventListener(csEventActions.OnIllegalActionEvent,OnIllegalActionEvent);
	
	csComm.addEventListener(csEventActions.OnNewDealEvent,OnNewDealEvent);
	csComm.addEventListener(csEventActions.OnNewRoundEvent,OnNewRoundEvent);
	csComm.addEventListener(csEventActions.OnSmallBlindEvent,OnSmallBlindEvent);
	csComm.addEventListener(csEventActions.OnBigBlindEvent,OnBigBlindEvent);
	csComm.addEventListener(csEventActions.OnNewPocketCardsEvent,OnNewPocketCardsEvent);
	
	
}

private function OnConnectionStatus(event:csEventActions):void
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

private function OnIllegalActionEvent(event:csEventActions):void{
	
		var objMsg:Object=event.dataResult;
	
		AppendData(objMsg.toString());
		AppendData("------------------------");
}
private function OnLogin(event:csEventActions):void{
	
		userName=txtLoginUser.text;		
		Alert.show("Logged in");
		
}
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnGetTablesAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnGetTablesAction(event:csEventActions):void{
	
			
	var objTables:CSTableList=event.dataResult;
	var objTable:CSTable;
	var tableinfo:String;
	
	
	if(objTables.TablesCount>0){
		
		
		
		for each (objTable in objTables.tables)
	
			{
				
				
							
				tableinfo=  objTable.id.toString()+ " | " + objTable.name + " | " + objTable.PlayersCount;
				//mTables.push(tableinfo);
			//	mTables.addItem(objTable);
				//mTables.addItem(tableinfo);
				
				
				AppendData(tableinfo);						
				
				AppendData("------------------------");
		 			
			
		
			}
	}
	else
			// We are here, if no tables 
		{
			
			AppendData("No tables");
			
			AppendData("------------------------");
		
		}
		
	//vsMain.selectedChild=cnvTables;
	//lstTables.dataProvider=mTables;
	
	
	
}

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnGetTableAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnGetTableAction(event:csEventActions):void{
	
	var objTable:CSTable=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	
		
	var tableinfo:String;
	var playerinfo:String;
	
		
			AppendData(objDataAction.toString());
		
			
			if (objTable!=null){
			
			tableinfo="name :" + objTable.name + " players:" + objTable.PlayersCount + " playing :" + objTable.playing; 
			
			AppendData(tableinfo);
	
			}
			else
			{
				AppendData("No table");
			}
			
			AppendData("------------------------");
		 			
	
}

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnJoinTableAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnJoinTableAction(event:csEventActions):void{
		
	var objTable:CSTable=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server

	AppendData(objDataAction.toString());	
	
		
	var tableinfo:String;
	
	
		
		AppendData(objDataAction.toString());
		
			
			if (objTable!=null){
			
			tableinfo="name :" + objTable.name + " players:" + objTable.PlayersCount + " playing :" + objTable.playing; 
			
			AppendData(tableinfo);
	
			}
			else
			{
				AppendData("No table");
			}
			
		AppendData("------------------------");
			
	
	
	
}

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnCreateTableAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnCreateTableAction(event:csEventActions):void{
		
	var objTable:CSTable=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server

	AppendData(objDataAction.toString());	
	
		
	var tableinfo:String;
	
	
		
		AppendData(objDataAction.toString());
		
			
			if (objTable!=null){
			
			
			
					
			
			//var objSeatedPlayer:CSPlayer=new CSPlayer(objTable.players[0]);
			
			for each( var tempPlayer:CSPlayer in objTable.players)
			{
				if (tempPlayer.Name==userName){
					
					var objSeatedPlayer:CSPlayer=new CSPlayer(tempPlayer);					
				}
			}
			
			var playername:String="player"+objSeatedPlayer.SeatId;
			
			
			//player0=new CSPlayer(objTable.players[0]); // seat 0
			
			this[playername]= new CSPlayer(objSeatedPlayer); 
			
			parsePlayer(this[playername]);
			
			tableinfo="ID :"+ objTable.id +" name :" + objTable.name + " players:" + objTable.PlayersCount + " playing :" + objTable.playing; 
			
			AppendData(tableinfo);
	
			}
			else
			{
				AppendData("No table");
			}
			
		AppendData("------------------------");
			
	
	btnStartGame.visible=true;
	btnStartGame.enabled=true;
	
	
}

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnLeaveTableAction
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnLeaveTableAction(event:csEventActions):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	AppendData(objDataAction.toString());
	AppendData("------------------------");
	
}

//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnTableRemovedEvent
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnTableRemovedEvent(event:csEventActions):void{
	
	
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	var objTableID:int; 
	var tableinfo:String;
		
	
	
		
			AppendData(objDataAction.toString());
		
			
			if (event.dataResult!=null){
				
					objTableID=event.dataResult;			
			
			tableinfo="tableID :" + objTableID; 
			
			AppendData(tableinfo);
	
			}
			else
			{
				AppendData("No table");
			}
			
			AppendData("------------------------");
}


//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    			OnTableChangedEvent
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnTableChangedEvent(event:csEventActions):void{
	
	var objTable:CSTable=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	
		
	var tableinfo:String;
	var playerinfo:String;
	
		
			AppendData(objDataAction.toString());
		
			
			if (objTable!=null){
			
			tableinfo="name :" + objTable.name + " players:" + objTable.PlayersCount + " playing :" + objTable.playing; 
			
			AppendData(tableinfo);
	
			}
			else
			{
				AppendData("No table");
			}
			
			AppendData("------------------------");
}



//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------
//    				OnTableAction
//				
//			Just generic event handler, to print message
//----------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------

private function OnTableAction(event:csEventActions):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	AppendData(objDataAction.toString());
	AppendData("------------------------");
	
}

//
// Just generic event handler for players
//
private function OnPlayerAction(event:csEventActions):void{
	
	
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	var objPlayer:CSPlayer= new CSPlayer(event.dataResult);
	
	var playerinfo:String;
	
		switch (event.dataAction){
			
			case "playerJoinedTableEvent":
				
				var playername:String="player"+objPlayer.SeatId;
				this[playername]= new CSPlayer(objPlayer);
				parsePlayer(this[playername]);
			
			break;
			
			case "playerLeftTableEvent":
				
				var playername:String="player"+objPlayer.SeatId;
				this[playername]= null;
			
			break;
			
			
			
		}	
	
	
	playerinfo="name:" + objPlayer.Name + " id:" + objPlayer.Id + " seatId:" + objPlayer.SeatId ;
	
	AppendData(objDataAction.toString());
	AppendData(playerinfo);
	AppendData("------------------------");
	
}

private function OnNewDealEvent(event:csEventActions):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	var objDealer:Object=objDataResult.dealer;
	
	AppendData(objDataAction.toString());
	
	//AppendData("Dealer :" + objDealer.name);
	AppendData("------------------------");
	
	
}

private function OnNewRoundEvent(event:csEventActions):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	//var objDealer:Object=objDataResult.dealer;
	
	AppendData(objDataAction.toString());
	
	//AppendData("Dealer :" + objDealer.name);
	AppendData("------------------------");
	
	
}

private function OnSmallBlindEvent(event:csEventActions):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
	//var objDealer:Object=objDataResult.dealer;
	
	AppendData(objDataAction.toString());
	
	//AppendData("Dealer :" + objDealer.name);
	AppendData("------------------------");
	
	
}

private function OnBigBlindEvent(event:csEventActions):void{
	
	var objDataResult:Object=event.dataResult; //Result Data In from Server
	var objDataAction:Object=event.dataAction; //Action Data In from Server
	
		
	AppendData(objDataAction.toString());	
	AppendData("------------------------");
	
	
}

private function OnNewPocketCardsEvent(event:csEventActions):void{
	

	var objDataAction:Object=event.dataAction; //Action Data In from Server
		
	//var objGameEvent:CSGameEvent= new CSGameEvent("newDealEvent",event.dataResult);
	var objGameEvent:CSGameEvent= event.dataResult;
	
	
	//imgFlop1.source=cards.getCard("CLUBS","DEUCE");
	
	var seatID:int=objGameEvent.player.SeatId;
	
	this[ "imgCard"+seatID+"0"].source=cards.getCard(objGameEvent.pocketCards.card[0].suit,objGameEvent.pocketCards.card[0].rank);
	this[ "imgCard"+seatID+"1"].source=cards.getCard(objGameEvent.pocketCards.card[1].suit,objGameEvent.pocketCards.card[1].rank);
	
	AppendData(objDataAction.toString());		
	AppendData("------------------------");
	
	
}

private function parsePlayer(player:CSPlayer):void
{

		var seatID:int=player.SeatId;
		var playerName:String;
		
	//	playerName=lblPlayer+seatID;
		
		 this[ "lblPlayer"+seatID].text=player.Name;
		 this[ "lblChips"+seatID].text=player.StackValue;
		 
		 
		
		
		

}

private function cnvsTablesComplited():void
{
		 var DGArray:Array = [
         {Artist:'Pavement', Album:'Slanted and Enchanted', Price:11.99},
         {Artist:'Pavement', Album:'Brighten the Corners', Price:11.99}];
        
        var initDG=new ArrayCollection(DGArray);
     //   var spectrumColors:ArrayCollection = ["red","orange","yellow","green","blue","indigo","violet"];
        
		//gridTables.dataProvider=spectrumColors; 
		
		
		//Alert.show("complited");
}

private function StripString(input:String):String
{
	 	
 	input = input.replace(/\r*$/g, "");	
 	return input;
}