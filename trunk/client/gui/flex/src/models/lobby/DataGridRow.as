package models.lobby
{
	import flash.utils.Dictionary;
	
	public class DataGridRow extends Object
	{
		public var stakes:String = null;
        public var numSpotsLeft:String = null;
        public var tableAvailability:String = null;
        public var numPlayers:int = 0;
        public var tableId:Number = -1;
        public var bigBlindSize:int = -1;
        public var maxNumPlayers:int = 0;
        public var lastPot:int = 0;
        public var maximumBuyin:int = -1;
        public var smallBlindSize:int = -1;
        public var casinoId:int = -1;
        public var tableName:String = "";
        public var numViewers:int = 0;
        public var minimumBuyin:int = -1;

		
		public function DataGridRow(params:Dictionary)
		{
			for(var element:String in params){
				this[element] = params[element];
			}
		}
		
	}
}