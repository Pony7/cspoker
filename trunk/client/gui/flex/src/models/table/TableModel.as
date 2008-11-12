package models.table
{
	import flash.utils.Dictionary;
	
	import models.*;
	import models.chips.*;
	import models.connection.*;
	import models.player.Player;
	
	import mx.containers.Canvas;
	
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
			//var potsTotal:int = 0;
			this.potSize = pots.totalValue;
		}
		
		public function receiveNewDealEvent(dealer:int):void{
			try{
				var seatNumber:int = this.getPlayerByPlayerId(dealer).playerSeat.seatNumber;
				var dealerButtonLocation:Canvas = table.dealerButtonLocations[seatNumber];
				table.dealerButton.moveDealerButton(dealerButtonLocation.x, dealerButtonLocation.y);
			}catch(e:Error){
				trace("error on newDealEvent: " + e.message);
			}
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
			}else if(this.currentRound == "FINAL"){
				// deal river card...
				table.dealRiverCard(cards.card);
				return;
			}
		}
		
		public function cleanUpTableData():void{
			/* cleans up data elements in table */
			
			for each(var player:Player in this.playerMap){
				try{
					player.playerSeat.resetForNewGame();
					removedMappedPlayer(player.id);
				}catch(e:Error){
					trace("Error cleaning up table data: " + e.message);					
				}
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
		
		public function receiveShowHandEvent(player:Object):void{
			var playerId:int = int(player.id);
			var playerCards:Object = player.handCards;
			if(Main.clientPlayerId != playerId){
				
				try{
					this.getPlayerByPlayerId(playerId).setHandDescription(player.description);
				}catch(e:Error){
					trace("Error in setting hand description: " + e.message);
				}
				
				/* show opponent's hand to client */
				try{
					this.getPlayerByPlayerId(playerId).playerSeat.showOpponentCards(playerCards);	
				}catch(e:Error){
					trace("Error in receiveShowCardsEvent: " + e.message);
				}
				
				
				
			}	
		}
		
		
	}
}