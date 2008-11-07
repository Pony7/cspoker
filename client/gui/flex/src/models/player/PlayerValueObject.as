package models.player
{
	import models.player.User;
	
	public class PlayerValueObject extends Object
	{
		public var stackSize:int = -1;
		public var user:User = null;
		public var betSize:int = -1;
		public var playerState:String = null;
		public var tablePosition:int = -1;
		public var state:String = null;
		
		public var name:String = null;
		
		public var avatar:String = null;
		
		public function PlayerValueObject(passedUser:User = null, passedName:String = null, passedStackSize:int = 0, passedBetSize:int = 0, passedTablePosition:int = 0, passedPlayerState:String = null)
		{
			user = passedUser;
			name = passedName;
			stackSize = passedStackSize;
			betSize = passedBetSize;
			tablePosition = passedTablePosition;
			playerState = passedPlayerState;
			
			
			
		}
		
	}
}