package models.lobby
{
	import models.*;
	
	import views.*;
	
	public class LobbyModel extends Object
	{
		protected var lobby:Lobby = null;
		
		public function LobbyModel(passedLobby:Lobby)
		{
			lobby = passedLobby;
		}
		
		public function requestTables(result:Object):void{

			if(!result.hasOwnProperty("tables") || result.tables == null) return;
			/* TODO FIX BUG WHERE 1 TABLE, OR 0 TABLES, OR 2+ TABLES...  */
			
			
			var targetUsed:Object;
			if(!result.hasOwnProperty("tables")){
				return;
			}
			
			if(result.tables.table.hasOwnProperty("name")){
				trace("Requesting table..." + result.tables.table.id);
				Main.serverConnection.csGetTableAction(result.tables.table.id);
				return;
			}
			
			for each(var table:Object in result.tables.table){
				
				trace("requesting table..." + table["id"]);
				if(table["id"] != null) Main.serverConnection.csGetTableAction(table["id"]);			

				
			}
		}
		
	}
}