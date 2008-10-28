package models.connection
{
	import flash.events.*;
	import flash.net.*;
	import flash.system.*;
	
	import models.*;
	
	public class ServerConnection extends Object
	{
		protected var connectionPort:int = 0;
		protected var hostName:String = "";
		protected var socket:XMLSocket = null;
		
		
		
		public function ServerConnection()
		{
			super();
		}
		
		/**
		 * 
		 * @param hostName
		 * @param port
		 * 
		 */
		public function connect(hostName:String, port:int):void{
			
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
		private function closeConnection():void{
			
		}
		
		public function isConnected():Boolean{
			return socket.connected;
		}
		
		private function configureListeners(socket:IEventDispatcher) : void
        {
            socket.addEventListener(Event.CLOSE, closeHandler);
            socket.addEventListener(Event.CONNECT, connectHandler);
            socket.addEventListener(DataEvent.DATA, dataHandler);
            socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, securityErrorHandler);
            return;
        }
		
	}
}