package models.player
{
	import models.*;
	
	import views.*;
	
	public class Player extends Object
	{
		public var playerSeat:Seat;
		protected var cards:Array;
		protected var playerInformation:PlayerValueObject;
		
		public var id:int = -1;
		public var chipsToCall:int = 0;
		
		public function Player(passedSeat:Seat)
		{
			playerSeat = passedSeat;
		}
		
		public function sitInPlayer(playerObj:Object):void{
			this.id = playerObj.id;	
			this.playerInformation = new PlayerValueObject(null, playerObj.name, playerObj.stackValue, playerObj.betChipsValue, playerObj.seatId, null);
			Main.table.tableModel.mapPlayerToId(this.id, this);
			Main.serverConnection.csGetAvatarAction(this.id);
		}
		
		public function paySmallBlind(amount:int):void{
			playerSeat.chipsToBet(amount);
			playerInformation.stackSize -= amount;
			playerInformation.betSize += amount;
			Main.maxBet = amount;
			playerSeat.refresh();
			return;
		}
		
		public function payBigBlind(amount:int):void{
			playerSeat.chipsToBet(amount);
			playerInformation.stackSize -= amount;
			playerInformation.betSize += amount;
			Main.maxBet = amount;
			playerSeat.refresh();
			return;
		}
		
		//public function 
		
		
		public function receiveNextPlayerEvent(playerId:int):void{
			// move spotlight;
			Main.table.spotLight.moveSpotLight(this.playerSeat.x + (this.playerSeat.width / 2), this.playerSeat.y + (this.playerSeat.height / 2));
			
			// determine if client's turn or opponent's turn.
			if(playerId == Main.clientPlayerId){
				// client's turn:
				this.clientTurn();
			}else{
				// opponent's turn:
				this.opponentTurn();
			}
		}
		
		private function clientTurn():void{
			Main.clientTurn = true;
			Main.table.gamePanel.showOnTurn();
			chipsToCall = (Main.maxBet - this.getBetChips());
			var maxBet:int = this.getStackSize();
			
			Main.table.gamePanel.gamePanelOnTurn.setConstraints(chipsToCall, maxBet);
			
		}
		
		private function opponentTurn():void{
			chipsToCall = (Main.maxBet - this.getBetChips());
			Main.clientTurn = false;
			Main.table.gamePanel.showOffTurn();
		}
		
		public function checkEvent():void{
			trace("check event received...");
			Main.table.dealerBox.dealerMessage(this.getPlayerName() + " checks...");
			return;
		}
		
		public function callEvent():void{
			trace("call event received...");
			playerSeat.chipsToBet(chipsToCall);
			Main.table.dealerBox.dealerMessage(this.getPlayerName() + " calls " + chipsToCall);
			incrementBetChips(chipsToCall);
			return;
		}
		
		public function betEvent(amount:int):void{
			trace("bet event received...");
			Main.table.dealerBox.dealerMessage(this.getPlayerName() + " bets " + amount);
			playerSeat.chipsToBet(amount);
			incrementBetChips(amount);
			return;
		}
		
		public function foldEvent():void{
			trace("fold event received...");
			Main.table.dealerBox.dealerMessage(this.getPlayerName() + " folds...");
			playerSeat.showFold();
			return;
		}
		
		public function allInEvent():void{
			trace("all in event received...");
			Main.table.dealerBox.dealerMessage(this.getPlayerName() + "goes all in!");
			playerSeat.showAllIn();
		}
		
		public function getPlayerName():String{
			return playerInformation.name;
		}
		
		public function getBetChips():int{
			if(playerInformation.betSize == -1) return 0;
			else return playerInformation.betSize;
		}
		
		public function incrementBetChips(amount:int):void{
			playerInformation.betSize += amount;
			return;
		}
		
		public function getStackSize():int{
			return this.playerInformation.stackSize;
		}
		
		public function getSeatId():int{
			return this.playerSeat.seatNumber;
		}
	}
}