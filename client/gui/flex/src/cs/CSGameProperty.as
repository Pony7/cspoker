package cs
{
	public class CSGameProperty
	{
	
	private var smallBlind:int;	
	private var bigBlind:int;
	private var smallBet:int;
	private var bigBet:int;
	private var _maxNbPlayers:int = 8;
	private var delay:int;
	
		
			
	public function CSGameProperty(objProperty:Object) 
			{
		
				this.smallBet = objProperty.smallBet;
				this.smallBlind = objProperty.smallBlind;
				this.bigBlind = objProperty.bigBlind;
				this.bigBet = objProperty.bigBet;
				this.delay = objProperty.delay;
		}
		
		public function get SmallBet():int
		{
			return this.smallBet;
		}
		
		public function get SmallBlind ():int
		{
			return this.smallBlind ;
		}
		
		public function get BigBlind ():int
		{
			return this.bigBlind ;
		}
		
		public function get BigBet ():int
		{
			return this.bigBet ;
		}
		
		public function get Delay ():int
		{
			return this.delay ;
		}

	}
}