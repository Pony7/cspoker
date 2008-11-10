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
		public var currentRound:String = "WAITING";
		
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
			if(pots.isPrototypeOf(Object)){
				for each(var pot:int in pots){
					potsTotal += pot;
				}
			}else{
				potsTotal = int(pots);
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
		
		public function receiveFlopCardsEvent(cards:Object):void{
			if(this.currentRound == "FLOP"){
				// deal 3 flop cards...
				table.dealFlopCards(cards.card);
				return;	
			}else if(this.currentRound == "TURN"){
				// deal turn card...
				table.dealTurnCard(cards.card);
				return;
			}else if(this.currentRound == "RIVER"){
				// deal river card...
				table.dealRiverCard(cards.card);
				return;
			}
		}
		
		public function receiveWinnersEvent(eventObj:Object):void{
			/* TODO */
		}
		
		public function receiveNewRoundEvent(eventObj:Object):void{
			trace("receiveNewRoundEvent...");
			this.currentRound = eventObj.round;
			
			if(this.currentRound == "PREFLOP"){
				return;
						
			}
			table.chipsToPot();
			if(this.potSize > 0){
				table.pot.calculateGraphics(this.potSize);
				table.pot.visible = true;
			}
		}
		
		public function receivePocketCardsEvent(pocketCards:Object):void{
			table.dealPocketCards(playerMap, pocketCards.card);
			
		}
		
		
		
	}
}