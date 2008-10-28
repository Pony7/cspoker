package cs
{
	public class CSPlayer
	{
			
	 	private var id:int;
		private var seatId:int;
		private var name:String;
		
		
		private var betChipsValue:int;
		private var stackValue:int;		
		private var sittingOut:Boolean;
		
		
		//public function Player(var id:int , var SeatId:int  ,var name:String , var stackValue:int, var betChipsValue:int , var sittingOut:Boolean=false)
		public function CSPlayer( objPlayer:Object)
		{
			this.id=objPlayer.id;
			this.seatId=objPlayer.seatId;
			this.name=objPlayer.name;
			this.stackValue=objPlayer.stackValue;
			this.betChipsValue=objPlayer.betChipsValue;
			
			if (objPlayer.sittingOut!=null){
				
				this.sittingOut=objPlayer.sittingOut;			
			}
			else{
			
			this.sittingOut=false;
			}
			
			
			
		}
		
		public function get Name():String
		{
			return this.name;
		}
		
		public function get Id():int
		{
			return this.id;
		}
		
		public function get SeatId():int
		{
			return this.seatId;
		}
		
		public function get StackValue():int
		{
			return this.stackValue;
		}
		
		public function get BetChipsValue():int
		{
			return this.betChipsValue;
		}
		
		public function get SittingOut():Boolean
		{
			return this.sittingOut;
		}

	}
}