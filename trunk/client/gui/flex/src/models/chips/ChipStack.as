package models.chips
{
	import mx.containers.Canvas;
	import mx.controls.Image;
	
	
	public class ChipStack extends Canvas
	{
		
		public var chipsInStack:int = 0;
		public var container:Canvas = null;
	
		public var leftIncrement:int;
		public var rightIncrement:int;
		public var thirdIncrement:int;
	
		public function ChipStack(passedContainer:Canvas)
		{
			trace("calling ChipStack... " + passedContainer);
			container = passedContainer;
			
		}
		
		public function calculateGraphics(chipCount:int):void{
			
			trace("calculateGraphics... " + chipCount);
			var count10000:int = 0;
			//var count5000:Number = 0;
			var count2500:int = 0;
			var count1000:int = 0;
			var count500:int = 0;
			var count100:int = 0;
			var count25:int = 0;
			var count5:int = 0;
			var count1:int = 0;
			var sum:int = chipCount;
		
		
			while(sum > 0){
				if ((sum - 10000) > 0){
					sum = sum - 10000;
					count10000++;
					continue;
				}
				//if ((sum - 5000) > 0){
					//sum = sum - 5000;
					//count5000++;
					//continue;
				//}
				if ((sum - 2500) > 0){
					sum = sum - 2500;
					count2500++;
					continue;
				}
				
				if ((sum - 1000) >= 0){
					sum = sum - 1000;
					count1000++;
					continue;
				}
				
				if ((sum - 500) >= 0){
					sum = sum - 500;
					count500++;
					continue;
				}
				
				if ((sum - 100) >= 0){
					sum = sum - 100;
					count100++;
					continue;
				}
				
				if ((sum - 25) >= 0){
					sum = sum - 25;
					count25++;
					continue;
				}
				
				if ((sum - 5) >= 0){
					sum = sum - 5;
					count5++;
					continue;
				}
				
				if ((sum - 1) >= 0){
					sum = sum - 1;
					count1++;
					continue;
				}
			}
			
			var counts:Array = new Array();
			var names:Array = new Array();
		
			counts.push(count1, count5, count25, count100, count500, count1000, count2500, count10000);
			//names.push("1", "5", "25", "100", "500", "1000", "2500", "10000");
			names.push("1Chip.gif", "5Chip.gif", "25Chip.gif", "100Chip.gif", "500Chip.gif", "1000Chip.gif", "2500Chip.gif", "10000Chip.gif");
		
			trace(counts);
			trace(names);
		
			drawChips(names, counts, chipCount);
			
		}// end function
		
		
		private function drawChips(names:Array, counts:Array, chipCount:int):void{
			
			var index:int = 0;
			var incrementIndex:int = 0;
			var x:int = 0;  // used for moving chips horizontally.
			var y:int = 0; // used for stacking chips.
			var addX:Boolean = false;
			var maxStack:int = 4;
			
			
			
			for each(var count:int in counts){
				if(maxStack == 0){
					break;
				}
			//trace(names[count] + " = " + counts[count]);
			while(count > 0){
				//trace("attaching movie: " + names[count]);
				//container.createEmptyMovieClip("container"+index, container.getNextHighestDepth());
				//container["container"+index].attachMovie("chip"+names[count], "chip"+index, this.getNextHighestDepth(), {_x:x, _y:y});
				var image:Image = new Image();
				image.source = "images/chips/"+names[incrementIndex];
				image.x = x;
				image.y = y;
				trace("adding child: " + image.source);
				container.addChild(image);
				
				count--;
				
				//counts[count] = counts[count]-1;
				//trace(index);
				index++;
				
				y = y-4;
				addX = true;
			}
			incrementIndex++;
			
				if(addX == true){
					y = 0;
					x = x+25;
					addX = false;
					maxStack--;
				}
			
			
			}
		}
			
		
		private function clearStack():void{
			
		}
		
		public function addToStack(chipAmount:int):void{
			chipsInStack += chipAmount;
			//chipsText.text = "$"+chipsInStack;
			calculateGraphics(chipsInStack);
		
		}
		
	}
}