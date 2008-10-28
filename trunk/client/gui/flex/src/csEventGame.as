package images
{
	import flash.events.Event;
	
	public class csEventGame extends Event
	{
		public static const OnNewDealEvent:String = "OnNewDealEvent";
		public static const OnNewRoundEvent:String = "OnNewRoundEvent";
		public static const OnSmallBlindEvent:String = "OnSmallBlindEvent";
		public static const OnBigBlindEvent:String = "OnBigBlindEvent";
		public static const OnNewPocketCardsEvent:String = "OnNewPocketCardsEvent";
		public static const OnNextPlayerEvent:String = "OnNextPlayerEvent";
		
		public static const OnFoldEvent:String = "OnFoldEvent";
		public static const OnWinnerEvent:String = "OnWinnerEvent";
		
		
		
		
		
		
		
		public var dataAction:*;
		public var dataResult:*;
		
		public function csEventGame(type:String, Action:*,Result:*)
		{
			this.dataAction= Action;
			this.dataResult= Result;
			super(type);
			
		}

	}
}