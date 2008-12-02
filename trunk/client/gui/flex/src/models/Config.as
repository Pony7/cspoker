package models
{
	public class Config extends Object
	{
		
		public static const TABLE_IMAGE_URL:String = "http://socialholdem.com/table1.jpg";
		public static const SHOW_CONNECT_DIALOG:Boolean = false;
		public static const SHOW_DISCONNECT_DIALOG:Boolean = true;
		
		public static const POCKETCARD_WIDTH:int = 45;
		public static const POCKETCARD_HEIGHT:int = 55;
		public static const FLOPCARD_WIDTH:int = 55;
		public static const FLOPCARD_HEIGHT:int = 65;
		
		public static const SPOTLIGHT_RADIUS:int = 100;
		public static const SPOTLIGHT_ALPHA:Number = 0.5;
		
		public static const TIMER_WIDTH:int = 60;
		public static const TIMER_HEIGHT:int = 10;
		public static const TIMER_SECONDS:int = 12;
		
		public static const AUTOCONNECT:Boolean = true;
		
		public static const AUTOLOGIN:Boolean = true;
		public static const AUTOLOGIN_USER:String = "kenzo";
		public static const AUTOLOGIN_PASS:String = "test";
		
		public static const AUTOJOINGAME:Boolean = false;
		
		public static const IS_PRO_CLIENT:Boolean = true;
		
		public static const CHAT_BUBBLE_MAX_WIDTH:int = 110;
		
		public static const CARD_DECK_WIDTH:int = 50;
		public static const CARD_DECK_HEIGHT:int = 25;

		//public static const USE_OLD_API:Boolean = true;
		public function Config()
		{
			super();
		}
		
	}
}