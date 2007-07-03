package game.cards;

public enum Suit {
	
	SPADES{
		public String toString(){
			return "spades";
		}
	}, 
	
	
	HEARTS{
		public String toString(){
			return "hearts";
		}
	}
	,
	
	DIAMONDS{
		public String toString(){
			return "diamonds";
		}
	}
	,
	
	CLUBS{
		public String toString(){
			return "clubs";
		}
	};
	
	
	public abstract String toString();

}
