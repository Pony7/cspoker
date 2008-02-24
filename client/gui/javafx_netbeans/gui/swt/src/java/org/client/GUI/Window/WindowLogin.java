package org.client.GUI.Window;

import org.client.ClientCore;
import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
/**
 * A class of login windows
 * @author Cedric
 */
public class WindowLogin extends Window {
	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The logo image of this window login
	 */
	private final Image logo=createLogo();
	/**
	 * The labels for this login window
	 */
	private Label userLabel,passwLabel,urlLabel,portLabel;
	/**
	 * The text values for this login window
	 */
	private Text userName,password,url,port;
	/**
	 * The buttons for this login window
	 */
	private Button loginButton;
	
	/**********************************************************
	 * Layout Constants
	 **********************************************************/
	//Logo Position
	private int logoXPosition=115;
	private int logoYPosition=15;
	//Shell constants
	private int shellWidth=300;
	private int shellHeigth=320;
	//Label constants
	private int xStartLabels=25;
	private int labelWidth=55;
	private int labelHeigth=20;
	//Field constants
	private int xStartFields=95;
	private int fieldWidth=150;
	private int fieldHeigth=labelHeigth;
	//y spacing
	private int yUserName=90;
	private int yPassword=yUserName+30;
	private int yUrl=yPassword+30;
	private int yPort=yUrl+30;
	private int yButton=yPort+50;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new login window with the given display and gui
	 * The user is asked to enter his user name, password, the 
	 * url and the port of the server he wants to connect to.
	 * @super
	 */
	public WindowLogin(Display display, final ClientGUI gui,ClientCore clientCore) {
		super(display, gui,clientCore);
		initializeWindow();
		setAsCurrentWindow();
		draw();
	}
	/**********************************************************
	 * Initialize
	 **********************************************************/
	/**
	 * Initializes all the labels and buttons for this login window
	 */
	private void initializeWindow() {
		createLoginShell();
		createCloseListener();
		createUserNameLabel();
		createPasswordLabel();
		createUrlLabel();
		createPortLabel();
		createLoginButton();
	}
	/**********************************************************
	 * Labels
	 **********************************************************/
	/**
	 * Creates a new user name label
	 */
	private void createUserNameLabel() {
		userLabel=new Label(getShell(), SWT.NULL);
		userLabel.setText("User name: ");
		userLabel.setLocation(xStartLabels,yUserName);
		userLabel.setSize(labelWidth,labelHeigth);
		userName = new Text(getShell(), SWT.BORDER);
		userName.setText("default_user");
		userName.setBounds(xStartFields,yUserName,fieldWidth,fieldHeigth);
		userName.setTextLimit(100);
	}
	/**
	 * Creates a new password label
	 */
	private void createPasswordLabel() {
		passwLabel=new Label(getShell(), SWT.NULL);
		passwLabel.setText("Password: ");
		passwLabel.setLocation(xStartLabels,yPassword);
		passwLabel.setSize(labelWidth,labelHeigth);
		password = new Text(getShell(), SWT.BORDER);
		password.setText("xxxxxx");
		password.setBounds(xStartFields,yPassword,fieldWidth,fieldHeigth);
		password.setTextLimit(100);
		password.setEchoChar('*');
	}
	/**
	 * Creates a new url label
	 */
	private void createUrlLabel(){
		urlLabel=new Label(getShell(), SWT.NULL);
		urlLabel.setText("Server IP:");
		urlLabel.setLocation(xStartLabels,yUrl);
		urlLabel.setSize(labelWidth,labelHeigth);
		url = new Text(getShell(), SWT.BORDER);
		url.setText("localhost");
		url.setBounds(xStartFields,yUrl,fieldWidth,fieldHeigth);
		url.setTextLimit(500);
	}
	/**
	 * Creates a new port label
	 */
	private void createPortLabel(){
		portLabel=new Label(getShell(), SWT.NULL);
		portLabel.setText("Server Port:");
		portLabel.setLocation(xStartLabels,yPort);
		portLabel.setSize(labelWidth,labelHeigth);
		port = new Text(getShell(), SWT.BORDER);
		port.setText("8080");
		port.setBounds(xStartFields,yPort,fieldWidth,fieldHeigth);
	}
	/**********************************************************
	 * Buttons
	 **********************************************************/
	/**
	 * Creates a new login button
	 */
	private void createLoginButton(){
		loginButton = new Button(getShell(), SWT.PUSH);
		loginButton.setText("LOGIN");
		loginButton.setLocation(xStartFields, yButton);
		loginButton.setSize(100, 30);
		
		loginButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				login();
			}
		});
	}
	/**********************************************************
	 * Shell settings
	 **********************************************************/
	/**
	 * Creates a new login shell
	 */
	private void createLoginShell() {
		getShell().setSize(shellWidth, shellHeigth);
		getShell().setText("Login");
	}
	
	/**
	 * Create a scaled version of the cspoker-logo
	 */
	private Image createLogo(){
		Image temp=imageFactory.getImage(getDisplay(),"logo");
		int width = temp.getBounds().width;
		int height = temp.getBounds().height;
		double scaleFactor=0.3;
		return new Image(getDisplay(),
				temp.getImageData().scaledTo((int)(width*scaleFactor),(int)(height*scaleFactor)));
	}
	/**
	 * Draws the images of this login window
	 */
	@Override
	public void drawImages() {
		getGC().drawImage(logo, logoXPosition, logoYPosition);
	}
	/**********************************************************
	 * Images
	 **********************************************************/
	/**
	 * Makes the user login
	 */
	private void login(){
		getClientCore().login(url.getText(),Integer.parseInt(port.getText()),userName.getText(),password.getText());
	}

}
