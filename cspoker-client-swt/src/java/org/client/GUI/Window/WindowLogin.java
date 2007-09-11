package org.client.GUI.Window;

import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WindowLogin extends Window {

	/**
	 * The user name entered by the user
	 */
	private String userName;
	/**
	 * The url of the server entered by the user
	 */
	private String url;
	/**
	 * The port of the server entered by the user
	 */
	private int port;
	/**
	 * The password of this user, entered by the user
	 */
	private String password;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	public WindowLogin(Display display, final ClientGUI gui) {
		super(display, gui);
		//Shell constants
		int shellWidth=300;
		int shellHeigth=320;
		loginShell.setSize(shellWidth, shellHeigth);
		
		Label cspoker=new Label(loginShell,SWT.CENTER);
		cspoker.setText("CSPOKER");
		cspoker.setBounds(125, 35, 50, 20);
		cspoker.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
		cspoker.setBackground(display.getSystemColor(SWT.COLOR_DARK_CYAN));
		
		//Label constants
		int xStartLabels=25;
		int labelWidth=55;
		int labelHeigth=20;
			
		//Field constants
		int xStartFields=95;
		int fieldWidth=150;
		int fieldHeigth=labelHeigth;
		
		//userName
		int yUserName=90;
		Label userLabel=new Label(loginShell, SWT.NULL);
		userLabel.setText("User name: ");
		userLabel.setLocation(xStartLabels,yUserName);
		userLabel.setSize(labelWidth,labelHeigth);
		final Text userName = new Text(loginShell, SWT.BORDER);
		userName.setText("default_user");
		userName.setBounds(xStartFields,yUserName,fieldWidth,fieldHeigth);
		userName.setTextLimit(100);
		
		//password
		int yPassword=yUserName+30;
		Label passwLabel=new Label(loginShell, SWT.NULL);
		passwLabel.setText("Password: ");
		passwLabel.setLocation(xStartLabels,yPassword);
		passwLabel.setSize(labelWidth,labelHeigth);
		final Text password = new Text(loginShell, SWT.BORDER);
		password.setText("xxxxxx");
		password.setBounds(xStartFields,yPassword,fieldWidth,fieldHeigth);
		password.setTextLimit(100);
		password.setEchoChar('*');
		
		//url
		int yUrl=yPassword+30;
		Label urlLabel=new Label(loginShell, SWT.NULL);
		urlLabel.setText("Server IP:");
		urlLabel.setLocation(xStartLabels,yUrl);
		urlLabel.setSize(labelWidth,labelHeigth);
		final Text url = new Text(loginShell, SWT.BORDER);
		url.setText("192.168.1.100");
		url.setBounds(xStartFields,yUrl,fieldWidth,fieldHeigth);
		url.setTextLimit(500);
		
		//port
		int yPort=yUrl+30;
		Label portLabel=new Label(loginShell, SWT.NULL);
		portLabel.setText("Server Port:");
		portLabel.setLocation(xStartLabels,yPort);
		portLabel.setSize(labelWidth,labelHeigth);
		final Text port = new Text(loginShell, SWT.BORDER);
		port.setText("8080");
		port.setBounds(xStartFields,yPort,fieldWidth,fieldHeigth);
		
		//button
		int yButton=yPort+50;
		Button button = new Button(loginShell, SWT.PUSH);
		button.setText("LOGIN");
		button.setLocation(xStartFields, yButton);
		button.setSize(100, 30);
		
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				gui.login(url.getText(),port.getText(),userName.getText(),password.getText());
			}
		});
		
		loginShell.open();
		gc = new GC(loginShell);
		
		while (!loginShell.isDisposed()){
			if (!display.readAndDispatch()){
				display.sleep();
			}
		}
	}
	
	/**********************************************************
	 * Functions
	 **********************************************************/
	
	public String getUserName() {
		return userName;
	}
	public String getUrl(){
		return url;
	}
	public int getPort(){
		return port;
	}
	public String getPassword(){
		return password;
	}
}
