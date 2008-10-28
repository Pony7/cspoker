package models
{
	import components.ConnectionBox;
	import components.Lobby;
	import components.Table;
	
	import flash.display.*;
	import flash.events.*;
	import flash.system.*;
	import flash.utils.*;
	
	import models.connection.*;
	
	import mx.core.UIComponent;
	import mx.managers.*;
	
	public class Main extends Object
	{
		public static var lobby:Lobby = null;
		public static var table:Table = null;
		public static var containerApp:CSPoker = null;
		public static var connectionBox:ConnectionBox = null;
		
		public var container:UIComponent;
		
		
		public function Main(passedContainerApp:CSPoker, passedLobby:Lobby, passedTable:Table, passedConnectionBox:ConnectionBox):void{
			containerApp = passedContainerApp;
			table = passedTable;
			lobby = passedLobby;
			connectionBox = passedConnectionBox;
			
			Security.allowDomain("*");
			
			
		}
		{
			trace("Launching CS Poker...");
			
			//super();
		}
		
	}
}