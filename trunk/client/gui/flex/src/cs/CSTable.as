package cs
{
	import mx.collections.ArrayCollection;
	//import mx.collections.SortField;
    //import mx.collections.Sort;
	
	public class CSTable
	{
		
		public var id:int;
	
		public var name:String;
		public var players:ArrayCollection=new ArrayCollection;

		public var playing:Boolean;
		public var property:CSGameProperty;

		
		public function CSTable(table:Object)
		
		{

		id=table.id;
		name=table.name;
		playing=table.playing;		
				
		property=new CSGameProperty(table.property);
		
		var objPlayers:ArrayCollection;
		objPlayers=table.players.player as ArrayCollection;
		
		
		if (objPlayers == null)
     		{
          		 objPlayers = new ArrayCollection([table.players.player]);
          		 
     		}
		var objPlayer:Object;
				
		for each (objPlayer in objPlayers)
		{
					
					var tempPlayer:CSPlayer= new CSPlayer(objPlayer);
					
					players.addItem( tempPlayer);
					
		}					
		
		
			
		}
		
		public function get  PlayersCount():int
		
		{
			return players.length; 
		}
		

	}
}