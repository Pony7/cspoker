package views
{

    import flash.display.Sprite;
    
    import models.Config;
    
    import models.utils.TweenLite;
    import models.utils.easing.*;
           
    public class SpotLight extends Sprite
    {   
        public function SpotLight(x:int = 0, y:int = 0):void
        {
                   
                       
            var circle:Sprite = new Sprite();
            circle.graphics.beginFill(0x00f6f85b, 1.0);
            circle.graphics.drawCircle(0, 0, Config.SPOTLIGHT_RADIUS);
            circle.graphics.endFill();
            this.x = x;
            this.y = y;
            circle.alpha = Config.SPOTLIGHT_ALPHA;
            //circle.visible = false;
            this.addChild(circle);
           
            
            circle.cacheAsBitmap = true;

        }
        
        public function moveSpotLight(toX:int = 0, toY:int = 0):void{
        	this.visible = true;
        	
        	TweenLite.to(this, 1, {alpha:Config.SPOTLIGHT_ALPHA, x:toX, y:toY});
			
        }
		
		public function onFinishTween(argument1:Number):void {
				trace("The tween has finished (spotLight)! argument1 = " + argument1);
		}

  
        public function hideSpotLight():void{
        	this.visible = false;
        }
        
    }
}