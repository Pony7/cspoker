package cs
{
	import mx.collections.ArrayCollection;
	
	public class CSTableList
	{
	public	var tables:ArrayCollection=new ArrayCollection();
		
		public function CSTableList(objTables:Object)
		{
			
			var objectTables:ArrayCollection; 
			
			// casting, flex bug
			
			if (objTables!=null){
			
			
			
			objectTables=objTables.table as ArrayCollection
			
			if (objectTables == null)
     				{
          		 		objectTables = new ArrayCollection([objTables.table]);
          		 
		     		}
		     		
		     
			
			if (objectTables !=null )
			
			{
			
			var	objTable:Object;
			
			
			for each (objTable in objectTables ){
		
				
				//var objProperty:Object=objTable.property;
				
				
				
				
				
					
						var tempTable:CSTable= new CSTable(objTable);
					
						tables.addItem( tempTable);
					
									
			
			}
				/*
				var table:Object=new Object;
				
				table.id=objTable.id;
				table.name=objTable.name;
				table.playing=objTable.playing;
				table.blinds=objProperty.smallBlind + "/" + objProperty.bigBlind;
				
				
				
				objPlayers=objTable.players.player as ArrayCollection;
		
				if (objPlayers == null)
     				{
          		 		objPlayers = new ArrayCollection([objTable.players.player]);
          		 
		     		}
		     	
		     	table.players=objPlayers.length;	
		     	
		     	tables.addItem(table);
		     	*/
			
				}
			
			}
			else
			
			//no tables
			{
			
			//	objectTables = new ArrayCollection();
				
			
			}
			
			}
	
		public function get  TablesCount():int
		{
			return tables.length; 
		}
		
			
			
	}
			
		

}
