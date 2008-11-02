package models.connection
{
	import flash.events.*;
	import flash.net.*;
	import flash.system.*;
	
	import models.*;
	import models.utils.MD5;
	
	public class ServerConnectionNew extends Object
	{
		protected var connectionPort:int = 0;
		protected var hostName:String = "";
		protected var socket:XMLSocket = null;
		
		
		public var messageCenter:MessageCenterNew = null;
		
		private var idAction:int=0;
		private var loginActionId:int=0;
		
		public function ServerConnectionNew(passedMessageCenter:MessageCenterNew)
		{
			messageCenter = passedMessageCenter;	
			super();
			
		}
		
		/**
		 * 
		 * @param hostName
		 * @param port
		 * 
		 */
		public function connect(hostName:String, port:int):void{
			
			
			try{
				Security.loadPolicyFile("xmlSocket://" + hostName +":" + port ); 
			}catch(e:Error){
				trace("Can't load policy file :: " + hostName +":"+port);
				return;
			}
            if (socket != null && socket.connected)
            {
                closeConnection();
            }// end if
            socket = new XMLSocket();
            configureListeners(socket);
            try
            {
                socket.connect(hostName, port);
                trace("Attempting primary connection to: " + hostName + ":" + port);
                messageCenter.setSocket(socket);
            }// end try
            catch (e:Error)
            {
                trace("Table_Connection error: " + e.message);
            }// end catch	
		}
		
		
		/**
		 * 
		 * @param hostName
		 * @param port
		 * 
		 */
		private function secondaryConnect(hostName:String, port:int=8080):void{
			
		}
		
		
		/**
		 * destroy listeners and close connection completely.
		 * 
		 */
		public function closeConnection() : Boolean{
            try{
                socket.removeEventListener(Event.CLOSE, Main.disconnectHandler);
                socket.removeEventListener(Event.CONNECT, Main.connectHandler);
                socket.removeEventListener(DataEvent.DATA, dataHandler);
                socket.removeEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
                socket.removeEventListener(ProgressEvent.PROGRESS, progressHandler);
                socket.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
                socket.close();
                return true;
            }// end try
            catch (e:Error)
            {
                trace("closeConnection error: " + e.message);
            }// end catch
            return false;
        }// end function

		public function isConnected():Boolean{
			return socket.connected;
		}
		
		
		/**
		 *	HANDLES ALL DATA IN, AND SENDS TO MESSAGE CENTER FOR DISPATCH TO VARIOUS COMPONENTS (LOBBY/TABLE) 
		 * @param event
		 * 
		 */		
		private function dataHandler(event:DataEvent):void {
        	
        	trace("dataHandler: " + event);
            
    		messageCenter.parseDataIn(event.data);
    	}
        
        private function securityErrorHandler(event:SecurityErrorEvent):void{
        	trace("Security error event..." + event.text);
        	return;
        }
        
        private function ioErrorHandler(param1:IOErrorEvent) : void
        {
            trace("Table_Connection ioErrorHandler: " + param1);
            messageCenter.handleDisconnect();
            return;
        }// end function
        
        private function progressHandler(event:ProgressEvent) : void
        {
            trace("Table_Connection progressHandler loaded:" + event.bytesLoaded + " total: " + event.bytesTotal);
            return;
        }
		
		private function configureListeners(socket:IEventDispatcher) : void
        {
            socket.addEventListener(Event.CLOSE, Main.disconnectHandler);
            socket.addEventListener(Event.CONNECT, Main.connectHandler);
            socket.addEventListener(DataEvent.DATA, dataHandler);
            socket.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            socket.addEventListener(ProgressEvent.PROGRESS, progressHandler);
            socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
            return;
        }
        
        private function csSendData(xmlOut:XML):void{
			trace("sending... " + xmlOut.toXMLString());
			
			socket.send(xmlOut.toXMLString());
			
		}
		
		// Login to CS Server		
		//public function csSendLogin(userName:String,userPassword:String,userAgent:String):void
		public function csSendLogin(userName:String,userPassword:String):void
		{	
			idAction++;
			messageCenter.loginActionId = idAction;
			var userAgent:String = "Sockets Client";
			var passwordHash:String = MD5.hash(userPassword);
			var xml:XML =
			
			<login username={userName} password={userPassword}  />;
			csSendData(xml);
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

		
	}
}