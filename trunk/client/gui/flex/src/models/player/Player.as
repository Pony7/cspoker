package models.player
{
	import models.*;
	
	import views.*;
	
	public class Player extends Object
	{
		protected var playerSeat:Seat;
		protected var cards:Array;
		protected var playerInformation:PlayerValueObject;
		
		public var id:int = -1;
		
		
		public function Player(passedSeat:Seat)
		{
			playerSeat = passedSeat;
		}
		
		public function sitInPlayer(playerObj:Object):void{
			this.id = playerObj.id;	
			playerInformation = new PlayerValueObject(null, playerObj.name, playerObj.stackValue, playerObj.betChipsValue, playerObj.seatId, null);
			Main.table.tableModel.mapPlayerToId(this.id, this);
		}
		
		public function paySmallBlind(amount:int):void{
			playerSeat.chipsToBet(amount);
			playerInformation.stackSize -= amount;
			playerInformation.betSize += amount;
			playerSeat.refresh();
		}
		
		public function payBigBlind(amount:int):void{
			playerSeat.chipsToBet(amount);
			playerInformation.stackSize -= amount;
			playerInformation.betSize += amount;
			
			playerSeat.refresh();
		}
		
		
		public function getPlayerName():String{
			return playerInformation.name;
		}
		
		public function getBetChips():int{
			return playerInformation.betSize;
		}
		
		public function getStackSize():int{
			return playerInformation.stackSize;
		}
		
		public function getSeatId():int{
			return this.id;
		}
	}
}