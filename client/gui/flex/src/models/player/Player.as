package models.player
{
	import models.*;
	
	import views.*;
	
	public class Player extends Object
	{
		protected var playerSeat:Seat;
		protected var cards:Array;
		protected var playerInformation:PlayerValueObject;
		public function Player(passedSeat:Seat)
		{
			playerSeat = passedSeat;
		}
		
	}
}