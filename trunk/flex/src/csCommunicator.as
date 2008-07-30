package
{
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
		
	private  function ParseDataIn(strIn:String):void{
	
	
		//if (strIn == undefined){
		if (strIn == null){
	
			return;
		}
		
		
		
		var strXML:String=strIn.replace("xsi:type","xsitype");
		
		
		
		var xmlDoc:XMLDocument = new XMLDocument(strXML);	
	
		var decoder:SimpleXMLDecoder=new SimpleXMLDecoder(true);
				
		

		contentObj=decoder.decodeXML(xmlDoc);
		
				
		
		var objAction:Object;
		var objResult:Object;

		
		
		
		
		
		if (contentObj.hasOwnProperty("login")){
		
		
		
			objResult=	contentObj.login;
		
			
		}
		
	
		
	
		if (contentObj.hasOwnProperty("successfulInvocationEvent")){
		 
	 	switch (contentObj.successfulInvocationEvent.action.xsitype){
	 
	 		case "getTablesAction":
		  			  	
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result.tables;
		  
		  	dispatchEvent(new csEvent( csEvent.OnGetTablesAction,objAction,objResult));
			
			break;
		
			case "getTableAction":
		  			  	
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
		  
		  	dispatchEvent(new csEvent( csEvent.OnGetTableAction,"getTableAction",objResult));
		  	break;
		
			case "joinTableAction":
		
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
		
			dispatchEvent(new csEvent( csEvent.OnJoinTableAction,"joinTableAction",objResult));
		
			break;
			
			case "leaveTableAction":
		
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
		
			dispatchEvent(new csEvent( csEvent.OnLeaveTableAction,"leaveTableAction",objResult));
		
			break;
			
			case "createTableAction":
		
			objAction=contentObj.successfulInvocationEvent.action;
			objResult=contentObj.successfulInvocationEvent.result;
		
			dispatchEvent(new csEvent( csEvent.OnCreateTableAction,"createTableAction",objResult));
		
			break;
			
		
		}	 
		 
		
		 
		 		
		}
		 
		if (contentObj.hasOwnProperty("tableCreatedEvent")){
			
			
			objResult=contentObj.tableCreatedEvent.table;
		  
		  	dispatchEvent(new csEvent( csEvent.OnGetTableAction,"tableCreatedEvent",objResult));
		  	
		  	return;
			
		}
		
		if (contentObj.hasOwnProperty("tableChangedEvent")){
			
			
			objResult=contentObj.tableChangedEvent.table;
		  
		  	dispatchEvent(new csEvent( csEvent.OnGetTableAction,"tableChangedEvent",objResult));
		  	
		  	return;
			
		}
		
		if (contentObj.hasOwnProperty("tableRemovedEvent")){
			
			
			objResult=contentObj.tableRemovedEvent;
		  
		  	dispatchEvent(new csEvent( csEvent.OnGetTableAction,"tableRemovedEvent",objResult));
		  	
		  	return;
			
		}
		
		
		 
		if (contentObj.hasOwnProperty("illegalActionEvent")){
			
			objAction=contentObj.illegalActionEvent.action;
			objResult=contentObj.illegalActionEvent.msg;
			
			dispatchEvent(new csEvent( csEvent.OnIllegalActionEvent,objAction,objResult));
			
			
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
            dispatchEvent(new csEvent( csEvent.OnConnectionsStatus,null,"SUCCESS"));
        }
   
 private function dataHandler(event:DataEvent):void {
        	
    		ParseDataIn(event.data);
    		
            trace("dataHandler: " + event);
            //dispatchEvent(new csEvent( csEvent.ONCONNECTED,event.text));
        }
   
 private function ioErrorHandler(event:IOErrorEvent):void {
            trace("ioErrorHandler: " + event);
            //dispatchEvent(new Event("connectionStatus",));
            dispatchEvent(new csEvent( csEvent.OnConnectionsStatus,null,"ERROR"));
            
        }
   
 private function progressHandler(event:ProgressEvent):void {
            trace("progressHandler loaded:" + event.bytesLoaded + " total: " + event.bytesTotal);
        }
   
 private function securityErrorHandler(event:SecurityErrorEvent):void {
            trace("securityErrorHandler: " + event);
        }
        
	
}
}