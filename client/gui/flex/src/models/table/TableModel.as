package models.table
{
	import flash.utils.Dictionary;
	
	import models.*;
	import models.chips.*;
	import models.connection.*;
	import models.player.Player;
	
	import views.*;
	
	
	public class TableModel extends Object
	{	
		public var table:Table;
		public var seatLocations:Array;
		public var pot:ChipStack;
		public var playerMap:Dictionary = new Dictionary();
		public var potSize:int = 0;
		
		public function TableModel(table:Table, seatsArray:Array, pot:ChipStack)
		{
			this.table = table;
			this.seatLocations = seatsArray;
			this.pot = pot;
		}
		
		public function mapPlayerToId(playerId:int, player:Player):void{
			playerMap[playerId] = player;
		}
		
		public function receivePotsChangedEvent(pots:Object):void{
			var potsTotal:int = 0;
			for each(var pot:int in pots){
				potsTotal += pot;
			}
			this.potSize = potsTotal;
		}
		
		public function removedMappedPlayer(playerId:int):void{
			if(playerMap[playerId] != null) delete playerMap[playerId];
		}
		
		public function getPlayerByPlayerId(playerId:int):Player{
			if(playerMap[playerId] != null) return playerMap[playerId];
			else return null;
		}
		
		public function receiveNewRoundEvent(eventObj:Object):void{
			trace("receiveNewRoundEvent...");
			table.chipsToPot();
			if(this.potSize > 0) table.pot.calculateGraphics(this.potSize);
		}
		
		public function receivePocketCardsEvent(pocketCards:Object):void{
			table.dealPocketCards(playerMap, pocketCards.card);
			
		}
		
		
		
	}
}