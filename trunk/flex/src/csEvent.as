package
{
	import flash.events.Event;
	
	public class csEvent extends Event
	{
		
		
		public static const OnConnectionsStatus:String = "OnConnectionStatus";
		public static const OnGetTablesAction:String = "OnGetTablesAction";
		public static const OnGetTableAction:String = "OnGetTableAction";
		public static const OnJoinTableAction:String = "OnJoinTableAction";
		public static const OnLeaveTableAction:String = "OnLeaveTableAction";
		public static const OnCreateTableAction:String = "OnCreateTableAction";
		
		public static const OnTableRemovedEvent:String = "OnTableRemovedEvent";
		
		
		
		public static const OnLogin:String = "OnLogin";
		public static const OnIllegalActionEvent:String = "OnIllegalActionEvent";
				
		
		public var dataAction:*;
		public var dataResult:*;
		 
		public function csEvent(type:String, Action:*,Result:*)
		{
			this.dataAction= Action;
			this.dataResult= Result;
			super(type);
		}

	}
}