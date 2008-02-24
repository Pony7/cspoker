package org.client.GUI.Images;

import java.util.HashMap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
/**
 * Singleton class to provide the images for all windows given the diplay for the
 * image and the correct path of the image
 * @author Cedric
 *
 */
public class ImageFactory {

	/**********************************************************
	 * Attributes
	 **********************************************************/
	/**
	 * Maps filenames to valid paths
	 */
	private static HashMap<String,String> images;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	 private static ImageFactory reference;
	/**
	 * Private constuctor to ensure singleton class
	 */
	private ImageFactory()
    {
        images=new HashMap<String, String>();
        initializeImages();
    }
	/**
	 * Static method to obtain reference to single instantiation of the image factory
	 */
    public static ImageFactory getImageFactory()
    {
      if (reference == null){
    	  reference = new ImageFactory();
      }
      return reference;
    }
	/**********************************************************
	 * Image methods
	 **********************************************************/
    /**
     * Returns the image with the given filename to the given display
     * @throws	IllegalArgumentException
     * 			if there doesn't exist an image with the given filename in this image factory
     * 			| !images.containsKey(filename)
     * @param display
     * 			the given display
     * @param filename
	 * 			the given filename
     */
	public Image getImage(Display display,String filename){
		if(!images.containsKey(filename))
			throw new IllegalArgumentException("Image does not exist!");
		return new Image(display,images.get(filename));
	}
	/**
	 * Adds an image entry in this imagefactory for the given filename and
	 * the given valid pathname
	 * @param filename
	 * 			the given filename
	 * @param pathname
	 * 			the given path name
	 */
	private void addImage(String filename,String pathname){
		images.put(filename, pathname);
	}
	/**
	 * Initializes the images for this image factory
	 * (the path and filenames of the images are added to this imagefactory)
	 */
	private void initializeImages() {
		//test
		addImage("two test","images/cards/2x.png");
		
		//General Images
		addImage("pokertafel","images/pokertafel.png");
		addImage("logo","images/cspoker8.jpg");
		addImage("warning","images/warning.png");
		
		//Card of hearts
		addImage("ace hearts","images/cards/as.png");
		addImage("two hearts","images/cards/2s.png");
		addImage("three hearts","images/cards/3s.png");
		addImage("four hearts","images/cards/4s.png");
		addImage("five hearts","images/cards/5s.png");
		addImage("six hearts","images/cards/6s.png");
		addImage("seven hearts","images/cards/7s.png");
		addImage("eight hearts","images/cards/8s.png");
		addImage("nine hearts","images/cards/9s.png");
		addImage("ten hearts","images/cards/10s.png");
		addImage("jack hearts","images/cards/js.png");
		addImage("queen hearts","images/cards/qs.png");
		addImage("king hearts","images/cards/ks.png");
		
		//Card of diamonds
		addImage("ace diamonds","images/cards/ad.png");
		addImage("two diamonds","images/cards/2d.png");
		addImage("three diamonds","images/cards/3d.png");
		addImage("four diamonds","images/cards/4d.png");
		addImage("five diamonds","images/cards/5d.png");
		addImage("six diamonds","images/cards/6d.png");
		addImage("seven diamonds","images/cards/7d.png");
		addImage("eight diamonds","images/cards/8d.png");
		addImage("nine diamonds","images/cards/9d.png");
		addImage("ten diamonds","images/cards/10d.png");
		addImage("jack diamonds","images/cards/jd.png");
		addImage("queen diamonds","images/cards/qd.png");
		addImage("king diamonds","images/cards/kd.png");
		
		//Card of spades
		addImage("ace spades","images/cards/as.png");
		addImage("two spades","images/cards/2s.png");
		addImage("three spades","images/cards/3s.png");
		addImage("four spades","images/cards/4s.png");
		addImage("five spades","images/cards/5s.png");
		addImage("six spades","images/cards/6s.png");
		addImage("seven spades","images/cards/7s.png");
		addImage("eight spades","images/cards/8s.png");
		addImage("nine spades","images/cards/9s.png");
		addImage("ten spades","images/cards/10s.png");
		addImage("jack spades","images/cards/js.png");
		addImage("queen spades","images/cards/qs.png");
		addImage("king spades","images/cards/ks.png");
		
		//Card of clubs
		addImage("ace clubs","images/cards/ac.png");
		addImage("two clubs","images/cards/2c.png");
		addImage("three clubs","images/cards/3c.png");
		addImage("four clubs","images/cards/4c.png");
		addImage("five clubs","images/cards/5c.png");
		addImage("six clubs","images/cards/6c.png");
		addImage("seven clubs","images/cards/7c.png");
		addImage("eight clubs","images/cards/8c.png");
		addImage("nine clubs","images/cards/9c.png");
		addImage("ten clubs","images/cards/10c.png");
		addImage("jack clubs","images/cards/jc.png");
		addImage("queen clubs","images/cards/qc.png");
		addImage("king clubs","images/cards/kc.png");
		
	}
}
