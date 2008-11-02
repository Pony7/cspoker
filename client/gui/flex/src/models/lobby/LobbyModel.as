package models.lobby
{
	import views.*;
	import models.*;
	
	public class LobbyModel extends Object
	{
		protected var lobby:Lobby = null;
		
		public function LobbyModel(passedLobby:Lobby)
		{
			lobby = passedLobby;
		}
		
		public function requestTables(result:Object):void{
			if(!result.hasOwnProperty("tables")) return;
			for each(var table:Object in result.tables){
				trace("requesting table..." + table["id"]);
				if(table["id"] != null) Main.serverConnection.csGetTableAction(table["id"]);			
				
			}
		}
		
	}
}