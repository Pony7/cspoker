package views
{

    import flash.display.Bitmap;
    import flash.display.Sprite;
    import mx.controls.Image;
    
    import models.Config;
    import models.utils.TweenLite;
    import models.utils.easing.*;
           
    public class DealerButton extends Sprite
    {   
        public function DealerButton(x:int = 0, y:int = 0):void
        {
                   
                       
            var dealerButton:Image = new Image();
           	dealerButton.source = "images/dealerButton.bmp";
            this.x = x;
            this.y = y;
            this.addChild(dealerButton);
           
        }
        
        public function moveDealerButton(toX:int = 0, toY:int = 0):void{
        	this.visible = true;
        	
        	TweenLite.to(this, 2, {x:toX, y:toY});
			
        }
		

  
        public function hideDealerButton():void{
        	this.visible = false;
        }
        
    }
}