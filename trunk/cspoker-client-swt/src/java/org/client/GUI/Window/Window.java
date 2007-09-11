package org.client.GUI.Window;

import org.client.GUI.ClientGUI;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class Window {

	ClientGUI gui;
	final Shell loginShell;
	GC gc;
	
	public Window(Display display,ClientGUI gui){
		loginShell = new Shell(display);
		this.gui = gui;
	}
}
