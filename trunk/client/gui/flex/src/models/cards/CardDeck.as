package models.cards
{
	import mx.containers.Canvas;
	
	import views.CardView;
	
	public class CardDeck extends Object
	{
		public var container:Canvas = null;	
		public var cardView:CardView;
	
		public function CardDeck(passedContainer:Canvas){
			
			trace("calling CardDeck... " + passedContainer);
			container = passedContainer;
			cardView= new CardView();
		
			container.addChild(cardView);
			
			//calculateCardGraphics(suit, rank);
			
		}
		
		public function calculateCardGraphics(suit:String, rank:String, width:int, height:int, x:int=0, y:int=0):void{
			var imgSource:String = "images/cards/" + CardDeck.getCardGraphicName(suit, rank);
			cardView.width=width;
			cardView.height=height;
			cardView.x = x;
			cardView.y = y;
			cardView.displayCard(suit, rank, imgSource, width, height);
		}
		
		public function loadHiddenCard(width:int=50, height:int=75, x:int=0, y:int=0):void{
			cardView.width=width;
			cardView.height=height;
			cardView.x = x;
			cardView.y = y;
			cardView.displayCard("", "", "images/cards/back.png", width, height);
		}
		
		public static function getCardGraphicName(suit:String, rank:String):String{
			var cardGraphic:String = rank + "-" + suit + ".swf";
			return cardGraphic;
		}
		
		public function clearCard():void{
			cardView.visible = false;
			
			delete this;
		}
		
	}
}