package
{
	import cs.*;
	
	import flash.events.*;
	import flash.net.*;
	import flash.security.*;
	import flash.system.Security;
	import flash.xml.*;
	
	import mx.controls.Alert;
	import mx.rpc.xml.SimpleXMLDecoder;
	
	
	public class csCommunicator extends EventDispatcher
	{
		
		private var csSocket:XMLSocket;
		private var isConnected:Boolean=false;
		private var idAction:int=0;
		private var contentObj:Object;
		
		public function csCommunicator()
		{
			csSocket= new XMLSocket();
			configureListeners(csSocket);
			
		}

		
		
		public function csConnect(csServerAddress:String,csServPort:int):void
		{
		
		
		
		Security.loadPolicyFile("xmlSocket://" + csServerAddress +":" + csServPort ); 
		
		
		
		
		csSocket.connect(csServerAddress, csServPort);
		
		
		}
		
		public function csDisconnect():void{
			
			csSocket.close();
			
		}
		
		
		private function csSendData(xmlOut:XML):void{
		
			csSocket.send(xmlOut.toXMLString());
		
		}
		
		// Login to CS Server
				
		//public function csSendLogin(userName:String,userPassword:String,userAgent:String):void
		public function csSendLogin(userName:String,userPassword:String):void
		{
		
		
		
		
		//var userAgent:String = "Flex Sockets Client";
		var userAgent:String = "Sockets Client";
		
		
		
		
	/*	var xml:XML = 
		
		<login username={userName} password={userPassword} useragent={userAgent}  />;
	*/
		
		
		var str:String="<login username=\'"+userName+"\' password=\'"+ userPassword + "\'  />";
		
		csSocket.send(str);
		
		//var xml:XML = new XML(str);		
		//csSendData(xml);
		
		}
	
		public function csGetTablesAction():void
		{
			
			
			idAction++;
											
			var xml:XML =
			
			<getTablesAction type="GetTablesAction" id={idAction} />;
			
			csSendData(xml);
			
		
		}
		
		public function csGetTableAction(tableID:int):void
		{
			
			
			idAction++;
											
			var xml:XML =
			
			<getTableAction type="getTableAction" tableid={tableID} id={idAction} />;
			
			csSendData(xml);
			
		
		}
		
		public function csJoinTableAction(tableID:int,buyin:int=1000):void
		{
						
			idAction++;
											
			var xml:XML =

			<joinTableAction id={idAction}>
			<tableId>{tableID}</tableId>
			<buyin>{buyin}</buyin>
			</joinTableAction>;
    
    		csSendData(xml);
    				
		}
		
		public function csLeaveTableAction(tableID:int):void
		{
						
			idAction++;
											
			var xml:XML =
			
			<leaveTableAction id={idAction}>
    		<tableId>{tableID}</tableId>
			</leaveTableAction>;
					    
    		csSendData(xml);
    				
		}
		
		public function csCreateTableAction(tableName:String,delay:int=0,bigBet:int=20,smallBet:int=10,bigBlind:int=10,smallBlind:int=5):void
		{
						
			idAction++;
											
			var xml:XML =
			
			<createTableAction id={idAction}>
		    <name>{tableName}</name>
    		<settings delay={delay} bigBet={bigBet} smallBet={smallBet} bigBlind={bigBlind} smallBlind={smallBlind}/>
    		</createTableAction>;

    
    		csSendData(xml);
    				
		}
		
		
		
		
		public function csStartGameAction():void
		{
						
			idAction++;
											
			var xml:XML =						
			<startGameAction id={idAction}/>;
    		
			
					    
    		csSendData(xml);
    				
		}
		
		public function csCallAction():void
		{						
			idAction++;
											
			var xml:XML =						
			<callAction id={idAction}/>;
    									    
    		csSendData(xml);    				
		}		
		
		public function csBetAction(amount:int ):void
		{						
			idAction++;
											
			var xml:XML =						
			
			<betAction amount={amount} id={idAction}/>;
			    									    
    		csSendData(xml);    				
		}
		
		public function csFoldAction():void
		{						
			idAction++;
											
			var xml:XML =						
			
			<foldAction id={idAction}/>;
						    									    
    		csSendData(xml);    				
		}
		
		public function csRaiseAction(amount:int ):void
		{						
			idAction++;
											
			var xml:XML =						
			
			<raiseAction amount={amount} id={idAction}/>;
									    									   
    		csSendData(xml);    				
		}
		
		public function csCheckAction():void
		{						
			idAction++;
											
			var xml:XML =						
			<checkAction id={idAction}/>;
    									    
    		csSendData(xml);    				
		}		
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------

	private  function ParseDataIn(strIn:String):void{
	
	
		//if (strIn == undefined){
		if (strIn == null){
	
			return;
		}
		
		
		
		//var strXML:String=strIn.replace("xsi:type","xsitype");
		//var xmlDoc:XMLDocument = new XMLDocument(strXML);
			
		var xmlDoc:XMLDocument = new XMLDocument(strIn);
	
		var decoder:SimpleXMLDecoder=new SimpleXMLDecoder(true);
				
		

		contentObj=decoder.decodeXML(xmlDoc);
		
				
		
		var objAction:Object;
		var objResult:Object;

		
		
		
		
		
		if (contentObj.hasOwnProperty("login")){
		
		
		
			objResult=	contentObj.login;
			
			dispatchEvent(new csEventActions( csEventActions.OnLogin,objAction,objResult));
			
			return;
		
			
		}
		
	
		
	
		if (contentObj.hasOwnProperty("successfulInvocationEvent")){
		 
	 	//switch (contentObj.successfulInvocationEvent.action.xsitype){
	 	switch (contentObj.successfulInvocationEvent.action["xsi:type"]){	
	 
	 		case "getTablesAction":
		  			  	
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result.tables;
			
			var mTables:CSTableList= new CSTableList(contentObj.successfulInvocationEvent.result.tables);
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnGetTablesAction,"getTablesAction",mTables));
		  	
		  	
			
			break;
		
			case "getTableAction":
		  			  	
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
			
			var mTable:CSTable= new CSTable(contentObj.successfulInvocationEvent.result);
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnGetTableAction,"getTableAction",mTable));
		  	
		  	break;
		
			case "joinTableAction":
		
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
			
			var mTable:CSTable= new CSTable(contentObj.successfulInvocationEvent.result);
		
			dispatchEvent(new csEventActions( csEventActions.OnJoinTableAction,"joinTableAction",mTable));
		
			break;
			
			case "leaveTableAction":
		
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
		
			dispatchEvent(new csEventActions( csEventActions.OnLeaveTableAction,"leaveTableAction",objResult));
		
			break;
			
			case "createTableAction":
		
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
			
			
		  
		  	var mTable:CSTable= new CSTable(contentObj.successfulInvocationEvent.result);
		
			dispatchEvent(new csEventActions( csEventActions.OnCreateTableAction,"createTableAction",mTable));
		
			break;
			
		
		}	 
		 
		
		return;
		 
		 		
		}
		 
		if (contentObj.hasOwnProperty("tableCreatedEvent")){
			
			
			objResult=contentObj.tableCreatedEvent.table;
		  
		  	var mTable:CSTable= new CSTable(contentObj.tableCreatedEvent.table);
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnGetTableAction,"tableCreatedEvent",mTable));
		  	
		  	return;
			
		}
		
		if (contentObj.hasOwnProperty("tableChangedEvent")){
			
			
			
			var mTable:CSTable= new CSTable(contentObj.tableChangedEvent.table);
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnTableChangedEvent,"tableChangedEvent",mTable));
		  	
		  	return;
			
		}
		
		if (contentObj.hasOwnProperty("tableRemovedEvent")){
			
			
		//	var mTable:CSTable= new CSTable(contentObj.tableRemovedEvent);
			
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnTableRemovedEvent,"tableRemovedEvent",contentObj.tableRemovedEvent.id));
		  	
		  	return;
			
		}
		
		if (contentObj.hasOwnProperty("playerJoinedTableEvent")){
			
			
			
			var objPlayer:CSPlayer= new CSPlayer(contentObj.playerJoinedTableEvent.player);
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnPlayerJoinedTableEvent,"playerJoinedTableEvent",objPlayer));
		  	
		  	return;
			
		}
		
		if (contentObj.hasOwnProperty("playerLeftTableEvent")){
			
			
					  
		  	var objPlayer:CSPlayer= new CSPlayer(contentObj.playerLeftTableEvent.player);
		  
		  	dispatchEvent(new csEventActions( csEventActions.OnPlayerLeftTableEvent,"playerLeftTableEvent",objPlayer));
		  	
		  	return;
			
		}
		
		 
		if (contentObj.hasOwnProperty("illegalActionEvent")){
			
			objAction=contentObj.illegalActionEvent.action;
			objResult=contentObj.illegalActionEvent.msg;
			
			dispatchEvent(new csEventActions( csEventActions.OnIllegalActionEvent,objAction,objResult));
			
			
		}
		
		// game logic
		
		if (contentObj.hasOwnProperty("newDealEvent")){
			
			
			objResult=contentObj.newDealEvent;
			
			var objEvent:CSGameEvent=new CSGameEvent("newDealEvent",objResult);
			
			
			dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"newDealEvent",objEvent));
			//dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"newDealEvent",objResult));
			
			
		}
		
		if (contentObj.hasOwnProperty("newRoundEvent")){
			
			
			objResult=contentObj.newRoundEvent;
			
			var objEvent:CSGameEvent=new CSGameEvent("newRoundEvent",objResult);
			dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"newRoundEvent",objEvent));
			
			//dispatchEvent(new csEventActions( csEventActions.OnNewRoundEvent,"newRoundEvent",objResult));
			
			
		}
		
		if (contentObj.hasOwnProperty("smallBlindEvent")){
			
			
			objResult=contentObj.smallBlindEvent;
			
			var objEvent:CSGameEvent=new CSGameEvent("smallBlindEvent",objResult);
			
			dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"smallBlindEvent",objEvent));
			//dispatchEvent(new csEventActions( csEventActions.OnSmallBlindEvent,"smallBlindEvent",objResult));
			
			
		}
		
		if (contentObj.hasOwnProperty("bigBlindEvent")){
			
			
			objResult=contentObj.bigBlindEvent;
			
			var objEvent:CSGameEvent=new CSGameEvent("bigBlindEvent",objResult);
			
			dispatchEvent(new csEventActions( csEventActions.OnNewDealEvent,"bigBlindEvent",objEvent));
			
			//dispatchEvent(new csEventActions( csEventActions.OnBigBlindEvent,"bigBlindEvent",objResult));
			
			
		}
		
		if (contentObj.hasOwnProperty("newPocketCardsEvent")){
			
			
			objResult=contentObj.newPocketCardsEvent;
			
			var objEvent:CSGameEvent=new CSGameEvent("newPocketCardsEvent",objResult);
			
			dispatchEvent(new csEventActions( csEventActions.OnNewPocketCardsEvent,"newPocketCardsEvent",objEvent));
			
			
			
			//dispatchEvent(new csEventActions( csEventActions.OnNewPocketCardsEvent,"newPocketCardsEvent",objResult));
			
			
		}
		
	
	
	}



private	 function configureListeners(dispatcher:IEventDispatcher):void {
	 	
            dispatcher.addEventListener(Event.CLOSE, closeHandler);
            dispatcher.addEventListener(Event.CONNECT, connectHandler);
            dispatcher.addEventListener(DataEvent.DATA, dataHandler);
            dispatcher.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            dispatcher.addEventListener(ProgressEvent.PROGRESS, progressHandler);
            dispatcher.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
        }

private function closeHandler(event:Event):void {
	
			Alert.show("Connection dropped");
            trace("closeHandler: " + event);
        }
   
 private function connectHandler(event:Event):void {
            trace("connectHandler: " + event);
            dispatchEvent(new csEventActions( csEventActions.OnConnectionsStatus,null,"SUCCESS"));
        }
   
 private function dataHandler(event:DataEvent):void {
        	
    		ParseDataIn(event.data);
    		
            trace("dataHandler: " + event);
            //dispatchEvent(new csEvent( csEvent.ONCONNECTED,event.text));
        }
   
 private function ioErrorHandler(event:IOErrorEvent):void {
            trace("ioErrorHandler: " + event);
            //dispatchEvent(new Event("connectionStatus",));
            dispatchEvent(new csEventActions( csEventActions.OnConnectionsStatus,null,"ERROR"));
            
        }
   
 private function progressHandler(event:ProgressEvent):void {
            trace("progressHandler loaded:" + event.bytesLoaded + " total: " + event.bytesTotal);
        }
   
 private function securityErrorHandler(event:SecurityErrorEvent):void {
            trace("securityErrorHandler: " + event);
        }
        
	
}
}