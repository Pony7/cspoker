package models.lobby
{
	import mx.collections.ArrayCollection;
	
	public class DataGridRow extends Object
	{
		/* public var stakes:String = null;
        public var numSpotsLeft:String = null;
        public var tableAvailability:String = null;
        public var numPlayers:int = 0;
        
        public var bigBlindSize:int = -1;
        public var maxNumPlayers:int = 0;
        public var lastPot:int = 0;
        public var maximumBuyin:int = -1;
        public var smallBlindSize:int = -1;
        public var casinoId:int = -1;
        public var tableName:String = "";
        public var numViewers:int = 0;
        public var minimumBuyin:int = -1; */

		public var playing:Boolean;
		public var tableId:Number = -1;
		public var tableName:String = null;
		public var delay:int = 0;
		public var maxNbPlayers:int = 0;
		public var nbPlayers:int = 0;
		public var autoBlinds:Boolean = false;
		public var autoDeal:Boolean = false;
		public var bigBet:int = 0;
		public var smallBet:int = 0;
		public var bigBlind:int = 0;
		public var smallBlind:int = 0;
		
		public var gameType:String = "";
		
		public function DataGridRow(result:Object)
		{
			if(result["id"] != null) tableId = result["id"];
			if(result["name"] != null) tableName = result["name"];
			playing = Boolean(result.playing);
			var property:Object = result.property;
			var players:ArrayCollection = result.players;
			delay = property["delay"];
			maxNbPlayers = property["maxNbPlayers"];
			if(players != null) nbPlayers = players.length;
			autoBlinds = property["autoBlinds"];
			autoDeal = property["autoDeal"];
			bigBet = property["bigBet"];
			smallBet = property["smallBet"];
			bigBlind = property["bigBlind"];
			smallBlind = property["smallBlind"];
			
			
			if(String(result["xsi:type"]) == "ns2:detailedHoldemTable"){
				gameType = "No Limit";
			} 
			return;
		}		
		public function getTableId():int{
			var id:int = 0;
			id = this.tableId;
			return id;
		}
		
		public function createRowFromTableList(tableObj:Object):void{
			trace("createRowFromTableList: " + tableObj);
		}
		
	}
}