package org.client.GUI.Window;

import org.client.GUI.ClientGUI;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
/**
 * A superclass of all windows
 * @author Cedric
 */
public abstract class Window {

	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The display for this window
	 */
	private final Display display;
	/**
	 * The gui that uses this window
	 */
	private final ClientGUI gui;
	/**
	 * The shell used in this window
	 */
	private final Shell shell;
	/**
	 * The graphical context of this window
	 */
	private final GC gc;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new window with the given display and gui
	 * @param display
	 * 			the given display
	 * @param gui
	 * 			the given gui
	 */
	public Window(Display display,ClientGUI gui){
		this.display=display;
		this.shell = new Shell(display);
		this.gui = gui;
		this.gc=new GC(this.shell);
	}
	/**********************************************************
	 * Methods
	 **********************************************************/
	public void draw(){
		shell.open();
		while (!shell.isDisposed()){
			if (!display.readAndDispatch()){
				display.sleep();
			}
		}
	}
	/**
	 * Returns the gui used in this window
	 */
	public ClientGUI getGui(){
		return gui;
	}
	/**
	 * Returns the shell used in this window
	 */
	public Shell getShell(){
		return shell;
	}
	/**
	 * Returns the display used in this window
	 */
	public Display getDisplay(){
		return display;
	}
	/**
	 * Returns the graphical context used in this window
	 */
	public GC getGC(){
		return gc;
	}
	void setAsCurrentWindow(){
		gui.setAsCurrentWindow(this);
	}
}
