package models.player
{
	import models.*;
	
	import views.*;
	
	public class Player extends Object
	{
		protected var playerSeat:Seat;
		protected var cards:Array;
		protected var playerInformation:PlayerInfoItem;
		public function Player(passedSeat:Seat)
		{
			playerSeat = passedSeat;
		}
		
	}
}