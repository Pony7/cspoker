package
{
	import flash.events.Event;
	import cs.*;
	
	public class csEventActions extends Event
	{
		
		
		public static const OnConnectionsStatus:String = "OnConnectionStatus";
		public static const OnGetTablesAction:String = "OnGetTablesAction";
		public static const OnGetTableAction:String = "OnGetTableAction";
		public static const OnJoinTableAction:String = "OnJoinTableAction";
		public static const OnLeaveTableAction:String = "OnLeaveTableAction";
		public static const OnCreateTableAction:String = "OnCreateTableAction";
		
		public static const OnPlayerJoinedTableEvent:String = "OnPlayerJoinedTableEvent";
		public static const OnPlayerLeftTableEvent:String = "OnPlayerLeftTableEvent";
		
				
		public static const OnTableRemovedEvent:String = "OnTableRemovedEvent";
		public static const OnTableChangedEvent:String = "OnTableChangedEvent";
		
		
		
		public static const OnLogin:String = "OnLogin";
		public static const OnIllegalActionEvent:String = "OnIllegalActionEvent";
		
		public static const OnNewDealEvent:String = "OnNewDealEvent";
		public static const OnNewRoundEvent:String = "OnNewRoundEvent";
		public static const OnSmallBlindEvent:String = "OnSmallBlindEvent";
		public static const OnBigBlindEvent:String = "OnBigBlindEvent";
		public static const OnNewPocketCardsEvent:String = "OnNewPocketCardsEvent";
		public static const OnNextPlayerEvent:String = "OnNextPlayerEvent";
		
		public static const OnFoldEvent:String = "OnFoldEvent";
		public static const OnWinnerEvent:String = "OnWinnerEvent";
		public static const OnBetEvent:String = "OnBetEvent";
		public static const OnCallEvent:String = "OnCallEvent";
		public static const OnRaiseEvent:String = "OnRaiseEvent";
		
		
		
		
		
		
		
		
		
				
		
		public var dataAction:*;
		public var dataResult:*;
		 
		public function csEventActions(type:String, Action:*,Result:*)
		{
			this.dataAction= Action;
			this.dataResult= Result;
			super(type);
		}

	}
}