package views
{
	import flash.display.Sprite;

	public class TurnTimer extends Sprite
	{
		
		import flash.display.Shape;
		import models.Config;
    	import flash.display.*;
    	import flash.events.*;
   		import flash.geom.*;
    	import flash.utils.*;
		
		public var backgroundObject:Shape = null;
		public var foregroundObject:Shape = null;
		
		private var timerSeconds:int = 0;
		private var startRelativeTime:int = 0;
		private var endRelativeTime:int = 0;
		private var playTimerSound:Boolean = false;
		
		private var graceTimeLimit:int = 0;
		private var stopTimeLimit:int = 0;
		
		public var callBackFunction:Function;
		
		
		public function TurnTimer(graceTimeLimit:int = 4, stopTimeLimit:int = 20, callBackFunction:Function=null)
		{
			super();
			this.graceTimeLimit = graceTimeLimit;
			this.stopTimeLimit = stopTimeLimit;
			this.callBackFunction = callBackFunction;
			this.initGraphics();
		
		}
		
		private function isStopped() : Boolean
        {
            
            return endRelativeTime <= 0;
        }
        
        public function stopTimer() : void
        {
        	this.visible = false;
        	trace("STOP TIMER CALLED!!!!");
            removeEventListener(Event.ENTER_FRAME, onNewTick, false);
            this.startRelativeTime = -1;
            this.endRelativeTime = -1;
            playTimerSound = false;
            if (this.contains(backgroundObject))
            {
                this.removeChild(backgroundObject);
            }// end if
            if (this.contains(foregroundObject))
            {
                this.removeChild(foregroundObject);
            }// end if
            return;
        }// end function
        
        
        public function startTimer() : void
        {
        	this.visible = true;
            removeEventListener(Event.ENTER_FRAME, onNewTick, false);
            if (this.contains(backgroundObject))
            {
                this.removeChild(backgroundObject);
            }// end if
            if (this.contains(foregroundObject))
            {
                this.removeChild(foregroundObject);
            }// end if
            startRelativeTime = getTimer() + graceTimeLimit;
            endRelativeTime = startRelativeTime + graceTimeLimit + stopTimeLimit;
            addEventListener(Event.ENTER_FRAME, onNewTick, false, 0, true);
            return;
        }// end function
		
		private function initGraphics() : void{
            backgroundObject = new Shape();
            var matrix:Matrix = new Matrix();
            //matrix.createGradientBox(50, 50, Math.PI / 2, 0, 0);
            matrix.createGradientBox(50,50,0,0,0);
            backgroundObject.graphics.beginGradientFill(GradientType.LINEAR, [65280, 16711680], [1, 1], [0, 255], matrix, SpreadMethod.PAD);
            backgroundObject.graphics.drawRect(0, 0, Config.TIMER_WIDTH, Config.TIMER_HEIGHT);
            backgroundObject.graphics.endFill();
            foregroundObject = new Shape();
            //this.addChild(backgroundObject);
            return;
        }
        
        
        protected function onNewTick(param1:Event) : void
        {
            var evt:* = param1;
            if (isStopped())
            {
                return;
            }// end if
            var currentRelativeTime:* = getTimer();
            if (currentRelativeTime < startRelativeTime)
            {
                return;
            }// end if
            if (backgroundObject.parent == null)
            {
                this.addChild(backgroundObject);
                this.addChild(foregroundObject);
            }// end if
            foregroundObject.graphics.clear();
            var maskWidth:int;
            var percentComplete:Number;
            if (currentRelativeTime < endRelativeTime)
            {
                percentComplete = Number(currentRelativeTime - startRelativeTime) / Number(stopTimeLimit);
                maskWidth = int(percentComplete * Config.TIMER_WIDTH);
            }// end if
            foregroundObject.graphics.beginFill(0);
            foregroundObject.graphics.drawRect(0, 0, maskWidth, Config.TIMER_HEIGHT);
            foregroundObject.graphics.endFill();
         
           if (!isStopped() && currentRelativeTime >= endRelativeTime || percentComplete > 1)
            {
                stopTimer();
                trace("TurnTimer is stopped...");
               if (callBackFunction != null && callBackFunction is Function)
                {
                    try
                    {
                    	// call the callBackFunction
                        callBackFunction.apply(null, null);
                    }// end try
                    catch (e:Error)
                    {
                    	trace("call back function error: " + e.message);
                    }// end catch
                }// end if
                
            }// end if
            
         
            return;
        }// end function
        
        
        
        
        
		
		
	}
}