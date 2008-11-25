package models.connection
{
	import flash.events.*;
	import flash.net.*;
	import flash.system.*;
	
	import models.*;
	import models.utils.MD5;
	
	public class ServerConnection extends Object
	{
		protected var connectionPort:int = 0;
		protected var hostName:String = "";
		protected var socket:XMLSocket = null;
		
		
		public var messageCenter:MessageCenter = null;
		
		private var idAction:int=0;
		private var loginActionId:int=0;
		
		public function ServerConnection(passedMessageCenter:MessageCenter)
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
			
			<ns5:loginAction id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/"><username>{userName}</username><passwordHash>{userPassword}</passwordHash></ns5:loginAction>;

			csSendData(xml);
		}
	
		public function csGetTablesAction():void
		{
			idAction++;
			var xml:XML =
			<ns5:getTableListAction id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
			csSendData(xml);
			return;
		}
		
		public function csGetTableAction(tableID:int):void
		{
			idAction++;					
			var xml:XML =
			<ns5:getHoldemTableInformationAction tableId={tableID} id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;

			csSendData(xml);
			return;
		}
		
		public function csJoinTableAction(tableId:int,buyin:int=1000):void
		{
						
			idAction++;
			messageCenter.joinTableActionId = idAction;	
			Main.table.setTableId(tableId);
								
			var xml:XML = <ns5:joinHoldemTableAction tableId={tableId} 
			id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
    
    		csSendData(xml);
    				
		}
		
		
		public function csSitInAction(tableId:int, seatId:int, buyInAmount:int = 100):void{
			idAction++;
			var xml:XML = <ns5:sitInAction seatId={seatId} tableId={tableId}
			id={idAction} buyIn={buyInAmount} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
    
    		csSendData(xml);
    				
		}
		
		
		public function csGetMoneyAmountAction():void{
			idAction++;
			
			var xml:XML = <ns5:requestMoneyAction
			id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
    
    		csSendData(xml);
		}
		
		public function csLeaveTableAction(tableId:int):void
		{
						
			idAction++;
											
			var xml:XML =
			
			<ns5:leaveTableAction id={idAction} tableId={tableId} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
					    
    		csSendData(xml);
    				
		}
		
		public function csCreateTableAction(tableName:String,delay:int=0,bigBet:int=20,smallBet:int=10,bigBlind:int=10,smallBlind:int=5,autoDeal:Boolean=false,autoBlinds:Boolean=false, maxNbPlayers:int=10):void
		{
						
			idAction++;
											
			var xml:XML =
			<ns5:createHoldemTableAction id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/">
			<name>{tableName}</name>
    		<configuration autoDeal={autoDeal} maxNbPlayers={maxNbPlayers} autoBlinds={autoBlinds} delay={delay} bigBet={bigBet} smallBet={smallBet} bigBlind={bigBlind} smallBlind={smallBlind}/>
    		</ns5:createHoldemTableAction>;

    
    		csSendData(xml);
    				
		}
		
		public function csGetAvatarAction(playerId:int):void{
			idAction++;
			var xml:XML =
			
			<ns5:getAvatarAction id={idAction} playerId={playerId} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
					    
    		csSendData(xml);
		}
		
		public function csGetPlayerIdAction():void{
			idAction++;
			var xml:XML =
			
			<ns5:getPlayerIDAction id={idAction} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
					    
    		csSendData(xml);
		}
		
		public function csSendTableMessageAction(tableId:int, message:String):void{
			
			idAction++;
			var xml:XML =
			
			<ns5:sendTableMessageAction id={idAction} tableID={tableId} xmlns:ns5="http://www.cspoker.org/api/2008-11/">
			<message>{message}</message>
			</ns5:sendTableMessageAction>;
					    
    		csSendData(xml);
			
		}
		
		public function csCheckOrCallAction(tableId:int):void{
			idAction++;
			var xml:XML =
			<ns5:checkOrCallAction id={idAction} tableId={tableId} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;   
    		csSendData(xml);
		}
		
		public function csBetOrRaiseAction(tableId:int, amount:int):void{
			idAction++;
			var xml:XML =
			<ns5:betOrRaiseAction id={idAction} amount={amount} tableId={tableId} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;    
    		csSendData(xml);
		}
		
		public function csFoldAction(tableId:int):void{
			idAction++;
			var xml:XML =
			<ns5:foldAction id={idAction} tableId={tableId} xmlns:ns5="http://www.cspoker.org/api/2008-11/"/>;
    		csSendData(xml);
		}
		
		
		
		
		public function csStartGameAction():void
		{
						
			idAction++;
											
			var xml:XML =						
			<startGameAction id={idAction}/>;
    		
			
					    
    		csSendData(xml);
    				
		}
		
		
		
		
		
			
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------

		
	}
}