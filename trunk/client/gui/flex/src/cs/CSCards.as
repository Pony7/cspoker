package cs
{
	public class CSCards
	{
		//DEUCE("Two", "2"), THREE("Three", "3"), FOUR("Four", "4"), FIVE("Five", "5"), SIX("Six", "6"), SEVEN("Seven", "7"), EIGHT("Eight", "8"), NINE(
//			"Nine", "9"), TEN("Ten", "T"), JACK("Jack", "J"), QUEEN("Queen", "Q"), KING("King", "K"), ACE("Ace", "A");
//CLUBS("Clubs", "c"), DIAMONDS("Diamonds", "d"), HEARTS("Hearts", "h"), SPADES("Spades", "s");

//	CLUBS Cards
//---------------------------------------------
[Embed(source="images/cards/2c.png")]
[Bindable]
private var CLUBSDEUCE:Class;  

[Embed(source="images/cards/3c.png")]
[Bindable]
private var CLUBSTHREE:Class;  

[Embed(source="images/cards/4c.png")]
[Bindable]
private var CLUBSFOUR:Class;  

[Embed(source="images/cards/5c.png")]
[Bindable]
private var CLUBSFIVE:Class;  

[Embed(source="images/cards/6c.png")]
[Bindable]
private var CLUBSSIX:Class;  

[Embed(source="images/cards/7c.png")]
[Bindable]
private var CLUBSSEVEN:Class;  

[Embed(source="images/cards/8c.png")]
[Bindable]
private var CLUBSEIGHT:Class;

[Embed(source="images/cards/9c.png")]
[Bindable]
private var CLUBSNINE:Class;

[Embed(source="images/cards/10c.png")]
[Bindable]
private var CLUBSTEN:Class;

[Embed(source="images/cards/Jc.png")]
[Bindable]
private var CLUBSJACK:Class;

[Embed(source="images/cards/Qc.png")]
[Bindable]
private var CLUBSQUEEN:Class;

[Embed(source="images/cards/Kc.png")]
[Bindable]
private var CLUBSKING:Class;

[Embed(source="images/cards/Ac.png")]
[Bindable]
private var CLUBSACE:Class;


//	DIAMONDS Cards
//---------------------------------------------

[Embed(source="images/cards/2d.png")]
[Bindable]
private var DIAMONDSDEUCE:Class;  

[Embed(source="images/cards/3d.png")]
[Bindable]
private var DIAMONDSTHREE:Class;  

[Embed(source="images/cards/4d.png")]
[Bindable]
private var DIAMONDSFOUR:Class;  

[Embed(source="images/cards/5d.png")]
[Bindable]
private var DIAMONDSFIVE:Class;  

[Embed(source="images/cards/6d.png")]
[Bindable]
private var DIAMONDSSIX:Class;  

[Embed(source="images/cards/7d.png")]
[Bindable]
private var DIAMONDSSEVEN:Class;  

[Embed(source="images/cards/8d.png")]
[Bindable]
private var DIAMONDSEIGHT:Class;

[Embed(source="images/cards/9d.png")]
[Bindable]
private var DIAMONDSNINE:Class;

[Embed(source="images/cards/10d.png")]
[Bindable]
private var DIAMONDSTEN:Class;

[Embed(source="images/cards/Jd.png")]
[Bindable]
private var DIAMONDSJACK:Class;

[Embed(source="images/cards/Qd.png")]
[Bindable]
private var DIAMONDSQUEEN:Class;

[Embed(source="images/cards/Kd.png")]
[Bindable]
private var DIAMONDSKING:Class;

[Embed(source="images/cards/Ad.png")]
[Bindable]
private var DIAMONDSACE:Class;

//	HEARTS Cards
//---------------------------------------------
[Embed(source="images/cards/2h.png")]
[Bindable]
private var HEARTSDEUCE:Class;  

[Embed(source="images/cards/3h.png")]
[Bindable]
private var HEARTSTHREE:Class;  

[Embed(source="images/cards/4h.png")]
[Bindable]
private var HEARTSFOUR:Class;  

[Embed(source="images/cards/5h.png")]
[Bindable]
private var HEARTSFIVE:Class;  

[Embed(source="images/cards/6h.png")]
[Bindable]
private var HEARTSSIX:Class;  

[Embed(source="images/cards/7h.png")]
[Bindable]
private var HEARTSSEVEN:Class;  

[Embed(source="images/cards/8h.png")]
[Bindable]
private var HEARTSEIGHT:Class;

[Embed(source="images/cards/9h.png")]
[Bindable]
private var HEARTSNINE:Class;

[Embed(source="images/cards/10h.png")]
[Bindable]
private var HEARTSTEN:Class;

[Embed(source="images/cards/Jh.png")]
[Bindable]
private var HEARTSJACK:Class;

[Embed(source="images/cards/Qh.png")]
[Bindable]
private var HEARTSQUEEN:Class;

[Embed(source="images/cards/Kh.png")]
[Bindable]
private var HEARTSKING:Class;

[Embed(source="images/cards/Ah.png")]
[Bindable]
private var HEARTSACE:Class;

//	SPADES Cards
//---------------------------------------------
[Embed(source="images/cards/2s.png")]
[Bindable]
private var SPADESDEUCE:Class;  

[Embed(source="images/cards/3s.png")]
[Bindable]
private var SPADESTHREE:Class;  

[Embed(source="images/cards/4s.png")]
[Bindable]
private var SPADESFOUR:Class;  

[Embed(source="images/cards/5s.png")]
[Bindable]
private var SPADESFIVE:Class;  

[Embed(source="images/cards/6s.png")]
[Bindable]
private var SPADESSIX:Class;  

[Embed(source="images/cards/7s.png")]
[Bindable]
private var SPADESSEVEN:Class;  

[Embed(source="images/cards/8s.png")]
[Bindable]
private var SPADESEIGHT:Class;

[Embed(source="images/cards/9s.png")]
[Bindable]
private var SPADESNINE:Class;

[Embed(source="images/cards/10s.png")]
[Bindable]
private var SPADESTEN:Class;

[Embed(source="images/cards/Js.png")]
[Bindable]
private var SPADESJACK:Class;

[Embed(source="images/cards/Qs.png")]
[Bindable]
private var SPADESQUEEN:Class;

[Embed(source="images/cards/Ks.png")]
[Bindable]
private var SPADESKING:Class;

[Embed(source="images/cards/As.png")]
[Bindable]
private var SPADESACE:Class;


		public function CSCards()
		{
		}
		
		public function  getCard(Suit:String,Rank:String):Class
		{
		
				var cardName:String; 
				
				
		cardName= Suit+Rank;
		
		return this[cardName];
		
		}
		

	}
}