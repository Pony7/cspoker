package cs
{
	import mx.collections.ArrayCollection;
	
	public class CSGameEvent
	{
		
				
		public var players:ArrayCollection=new ArrayCollection;
		public var dealer:CSPlayer;
		public var player:CSPlayer;
		public var amount:int;
		public var pots:int; 
		public var roundname:String;
		public var pocketCards:Object;
		
		
	
		public function CSGameEvent(event:String, obj:Object)
		{
			
			switch (event){
				
				
				case "newDealEvent":
				
					var objPlayer:Object; 
				
					dealer= new CSPlayer( obj.dealer);
				
					for each (objPlayer in obj.players)
					{
					
						var tempPlayer:CSPlayer= new CSPlayer(objPlayer);
					
						players.addItem( tempPlayer);
					
					}
					break;	
					
				case "newRoundEvent":
				
				
					roundname= obj.roundName;
					player= new CSPlayer( obj.player);
				
					break;	
					
				case "smallBlindEvent":
				
					pots=obj.pots;
					amount=obj.amount;
				
					player= new CSPlayer( obj.player);
				
					break;	
				
				case "bigBlindEvent":
				
					pots=obj.pots;
					amount=obj.amount;
				
					player= new CSPlayer( obj.player);
				
					break;	
				
				case "newPocketCardsEvent":
				
					pots=obj.pots;
					amount=obj.amount;
				
					player= new CSPlayer( obj.player);
					this.pocketCards= obj.pocketCards;
				
					break;	
				
				
				
			}
			
		}

	}
}