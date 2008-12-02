package models.table
{
	import flash.utils.Dictionary;
	
	import models.*;
	import models.chips.*;
	import models.connection.*;
	import models.player.Player;
	import models.utils.TweenLite;
	
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
		
		public function receiveAnnounceWinnersEvent(winners:Object):void{
			var count:int = 0;
			var potDescriptionText:String = "";
			var player:Object;
			var winningHandDescription:String = "";
			
			var endX:int;
			var endY:int
			var chipStackTemp:ChipStack;	
			
			if(winners.player.hasOwnProperty("seatId")){
				
				count++;
				/* ONE WINNER */
				
				if(count == 1) potDescriptionText = "main pot";
				else potDescriptionText = "side pot";
				player = winners.player;
				var winningPlayer:Player = this.getPlayerByPlayerId(player.id);
				
				try{
					winningHandDescription = this.getPlayerByPlayerId(player.id).getHandDescription();
				}catch(e:Error){
					trace("Error getting hand description in announce winner: " + e.message);
				}
				
				
				this.table.tableModel.getPlayerByPlayerId(player.id).updatePlayer(player);
				this.table.dealerBox.dealerMessage("Player " + player.name + " has won the " 
					+ potDescriptionText + " of " + winners.gainedAmount + " with a " + winningHandDescription);
				
				endX = this.table.tableModel.getPlayerByPlayerId(player.id).playerSeat.chipStack.x;
				endY = this.table.tableModel.getPlayerByPlayerId(player.id).playerSeat.chipStack.y;
				chipStackTemp = new ChipStack(this.table);
				chipStackTemp.calculateGraphics(winners.gainedAmount);
				
				TweenLite.to(chipStackTemp, 1, {alpha:1, x:endX, y:endY, delay:.5, visible:false, overwrite:true});
				
				/* MOVE THE POT CHIPS TO THE PLAYER ANIMATION */
				
			}else{
				for each(var winner:Object in winners){
					count++;
				/* MULTIPLE POTS */
					if(count == 1) potDescriptionText = "main pot";
					else potDescriptionText = "side pot";
					player = winner.player;
					try{
						winningHandDescription = this.getPlayerByPlayerId(player.id).getHandDescription();
					}catch(e:Error){
						trace("Error getting hand description in announce winner: " + e.message);
					}
					
					this.getPlayerByPlayerId(player.id).updatePlayer(player);
					this.table.dealerBox.dealerMessage("Player " + player.name + " has won the " 
					+ potDescriptionText + " of " + winner.gainedAmount + " with a " + winningHandDescription);  
					
				
					endX = this.table.tableModel.getPlayerByPlayerId(player.id).playerSeat.chipStack.x;
					endY = this.table.tableModel.getPlayerByPlayerId(player.id).playerSeat.chipStack.y;
					chipStackTemp = new ChipStack(this.table);
					chipStackTemp.calculateGraphics(winners.gainedAmount);
				
					TweenLite.to(chipStackTemp, 1, {alpha:1, x:endX, y:endY, delay:.5, visible:false, overwrite:true});
				
					
				}
			}
			this.table.cleanUpTable();
		}
		
		public function mapPlayerToId(playerId:int, player:Player):void{
			playerMap[playerId] = player;
		}
		
		public function getSeatBySeatId(seatId:int):Seat{
			if(this.seatLocations[seatId] != null) return this.seatLocations[seatId];
			else return null;
		}
		
		public function removedMappedPlayer(playerId:int):void{
			if(playerMap[playerId] != null) delete playerMap[playerId];
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
					//removedMappedPlayer(player.id);
				}catch(e:Error){
					trace("Error cleaning up table data: " + e.message);					
				}
			}
		}
		
		
		
		public function receiveNewRoundEvent(eventObj:Object):void{
			trace("receiveNewRoundEvent...");
			this.currentRound = eventObj.round;
			
			var seatCount:int = 0;
			while(seatCount < seatLocations.length)
            {
                seatLocations[seatCount].resetSeatForNewRound();
                seatCount++;
            }// end while
			
			
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
		
		public function loadGameStateFromNewDeal(result:Object):void{
			for each(var player:Player in this.playerMap){
				player.sitOutPlayer();
			}
			var seatId:int = -1;
			
			for each(var sitInPlayer:Object in result.players){
						seatId = sitInPlayer.seatId;
						this.getSeatBySeatId(seatId).receiveSitIn(sitInPlayer);
						if(sitInPlayer.betChipsValue != 0) this.getSeatBySeatId(seatId).chipsToBet(sitInPlayer.betChipsValue);
			}
			
			
			
		}
		
		
		public function loadGameStateFromDetailedTable(result:Object):void{
			trace("load current game state called!!!");
			for each(var player:Player in this.playerMap){
				player.sitOutPlayer();
			}
			
			this.table.cleanUpTable();
			
			
			var players:Object = result.players;
			var seatId:int = -1;
			
			if(players != null){
				
				if(players.player.hasOwnProperty("name")){
					
					seatId = players.player.seatId;
					this.getSeatBySeatId(seatId).receiveSitIn(players.player);
					if(players.player.betChipsValue != 0) this.getSeatBySeatId(seatId).chipsToBet(players.player.betChipsValue);
				}else{
					for each(var sitInPlayer:Object in players.player){
						seatId = sitInPlayer.seatId;
						this.getSeatBySeatId(seatId).receiveSitIn(sitInPlayer);
						if(sitInPlayer.betChipsValue != 0) this.getSeatBySeatId(seatId).chipsToBet(sitInPlayer.betChipsValue);
					}
				}
				
			}
			
			var round:String = result.round;
			var isPlaying:Boolean = result.playing;
			
			
			if(isPlaying == true && round != "WAITING"){
				// show the pot:
				this.receivePotsChangedEvent(result.pots);
			
				// new round event:
				this.receiveNewRoundEvent(result);
			
				// dealer button:
				var dealerId:int = result.dealer.id;
				this.receiveNewDealEvent(dealerId);
				
				// flop cards:
				if(result.communityCards != null) this.receiveFlopCardsEvent(result.communityCards);
			}
			
			
			
		}
		
		
	}
}