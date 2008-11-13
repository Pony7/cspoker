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
		
		public static var clientSeatId:int = -1;
		public static var clientPlayerId:int = -1;
		public static var clientSeat:Seat = null;
		public static var clientTurn:Boolean = false;
		public static var maxBet:int = 0;
			
		//public static var newServerConnection:ServerConnectionNew = null;
		//public static var newMessageCenter:MessageCenterNew = null;
		
		public function Main(passedContainerApp:CSPoker, passedLobby:Lobby, passedTable:Table, passedConnectionBox:ConnectionBox, passedRegisterBox:RegisterBox):void{
			trace("Launching CS Poker...");
			containerApp = passedContainerApp;
			
			containerApp.addEventListener(Event.CLOSING,closeHandler)
  

			table = passedTable;
			lobby = passedLobby;
			connectionBox = passedConnectionBox;
			registerBox = passedRegisterBox;
			messageCenter = new MessageCenter();
			serverConnection = new ServerConnection(messageCenter);
				
			
			
			connectionBox.endInit();
			lobby.endInit();
			//table.endInit();
			
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
		
		public static function getTables():void{
			serverConnection.csGetTablesAction();
		}
		
		public static function createNewTable(tableName:String):void{
			var delay:int = 12;
			serverConnection.csCreateTableAction(tableName);
			lobby.gamesDG.refreshGames();
		}
		
		public static function connectHandler(event:Event):void{
        	if(Config.SHOW_CONNECT_DIALOG) Main.showServerMessage("connected!!!");
        	connectionBox.showDisconnectButton();
        	return;
        }
        
        
        
        public static function sendSitIn(seatId:int, buyInAmount:int):void{
        	var theTableId:int = table.tableItem.getTableId();
        	clientSeatId = seatId;
        	serverConnection.csSitInAction(theTableId, seatId, buyInAmount);
        }
        
        public static function receiveClientPlayerId(passedClientPlayerId:int):void{
        	clientPlayerId = passedClientPlayerId;
        }
        
        
        /**
         * takes a whole list of tables and sends out requests for each table's info 
         * @param tablesList
         * 
         */        
        public static function receiveTablesList(tablesList:XML):void{
        	
        }
        
        public static function disconnectHandler(event:Event):void{
        	if(Config.SHOW_DISCONNECT_DIALOG) Main.showServerMessage("disconnected...");
        	connectionBox.showConnectButton();
        }
		
		
		
		public static function joinGame(selectedTable:DataGridRow):void{
			
			serverConnection.csJoinTableAction(selectedTable.tableId);
			table.setTableId(selectedTable.tableId);
			table.loadTableInfo(selectedTable);
			trace("join game called: " + selectedTable);
			
		}
		
		public function closeHandler(evt:Event):void{
		
     		if(serverConnection.isConnected()) serverConnection.csLeaveTableAction(table.tableId);
         	trace("closeHandler called, exiting...");
         	containerApp.exit();
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