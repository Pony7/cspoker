package game.cards;

public enum Rank {
	DEUCE{
		public int getValue(){
			return 2;
		}
	}, 
	
	THREE{
		public int getValue(){
			return 3;
		} 
	},
	
	FOUR{
		public int getValue(){
			return 4;
		} 
	}, 
	
	FIVE{
		public int getValue(){
			return 5;
		} 	
	}, 
	
	SIX{
		public int getValue(){
			return 6;
		} 
	},

    SEVEN{
		public int getValue(){
			return 7;
		} 
	}, 
    
    EIGHT{
		public int getValue(){
			return 8;
		} 
	},
    
    NINE{
		public int getValue(){
			return 9;
		} 
	}, 
    
    TEN{
		public int getValue(){
			return 10;
		} 
	}, 
    
    JACK{
		public int getValue(){
			return 11;
		}
		public String toString(){
			return "jack";
		}
	}, 
    
    QUEEN{
		public int getValue(){
			return 12;
		}
		public String toString(){
			return "queen";
		}
	}, 
    
    KING{
		public int getValue(){
			return 13;
		}
		public String toString(){
			return "king";
		}
	},
    
    ACE{
		public int getValue(){
			return 14;
		}
		public String toString(){
			return "ace";
		}
	};
	
	public abstract int getValue();
	public String toString(){
		return Integer.toString(getValue());
	}
}
