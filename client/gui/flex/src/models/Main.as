package models
{
	import flash.display.*;
	import flash.events.*;
	import flash.system.*;
	import flash.utils.*;
	
	import models.connection.*;
	import models.lobby.DataGridRow;
	
	import mx.core.UIComponent;
	import mx.managers.*;
	
	import views.*;
	
	public class Main extends Object
	{
		public static var lobby:Lobby = null;
		public static var table:Table = null;
		public static var containerApp:CSPoker = null;
		public static var connectionBox:ConnectionBox = null;
		public static var registerBox:RegisterBox = null;
		
		public var container:UIComponent;
		public static var messageCenter:MessageCenter = null;
		public static var serverConnection:ServerConnection = null;	
		
		public function Main(passedContainerApp:CSPoker, passedLobby:Lobby, passedTable:Table, passedConnectionBox:ConnectionBox, passedRegisterBox:RegisterBox):void{
			trace("Launching CS Poker...");
			containerApp = passedContainerApp;
			table = passedTable;
			lobby = passedLobby;
			connectionBox = passedConnectionBox;
			registerBox = passedRegisterBox;
			messageCenter = new MessageCenter();
			serverConnection = new ServerConnection(messageCenter);
			
			connectionBox.endInit();
			
			//Security.allowDomain("*");
		}
		
		
		public static function sendConnect(hostName:String, port:int):void{
			serverConnection.connect(hostName, port);	
		}
		
		public static function sendDisconnect():void{
			trace("sending disconnect...");
			var successfullyDisconnected:Boolean = serverConnection.closeConnection();
			if(successfullyDisconnected){
				Main.showServerMessage("disconnected...");
				connectionBox.showConnectButton();
			}
		}
		
		public static function sendLogin(userName:String, userPass:String):void{
			serverConnection.csSendLogin(userName, userPass);
		}
		
		public static function connectHandler(event:Event):void{
        	Main.showServerMessage("connected!!!");
        	connectionBox.showDisconnectButton();
        	return;
        }
        
        public static function disconnectHandler(event:Event):void{
        	Main.showServerMessage("disconnected...");
        	connectionBox.showConnectButton();
        }
		
		
		
		public static function joinGame(selectedTable:DataGridRow):void{
			trace("join game called: " + selectedTable);
		}
		
		public static function showServerMessage(message:String) : void
        {
            var dialogBox:* = new ServerMessageDialog();
            PopUpManager.addPopUp(dialogBox, containerApp, true);
            dialogBox.setText(message);
            PopUpManager.centerPopUp(dialogBox);
            return;
        }// end function
		
		
		public static function showTable():void{
			containerApp.mainAppViewStack.selectedChild=table;
		}		
		public static function showLobby():void{
			containerApp.mainAppViewStack.selectedChild=lobby;
		}
		public static function showConnectionBox():void{
			containerApp.mainAppViewStack.selectedChild=connectionBox;
		}
		public static function showRegisterBox():void{
			containerApp.mainAppViewStack.selectedChild=registerBox;
		}
		
		
		
	}
}