package models.lobby
{
	import views.*;
	
	public class LobbyModel extends Object
	{
		protected var lobby:Lobby = null;
		
		public function LobbyModel(passedLobby:Lobby)
		{
			lobby = passedLobby;
		}
		
	}
}