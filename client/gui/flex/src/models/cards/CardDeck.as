package models.cards
{
	public class CardDeck extends Object
	{
		public function CardDeck()
		{
			super();
		}
		
		public static function getCardGraphicName(suit:String, rank:String):String{
			var cardGraphic:String = rank + "-" + suit + ".swf";
			return cardGraphic;
		}
		
	}
}