package org.client.GUI.Window;

import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * A class of game windows
 * @author Cedric
 */
public class WindowGame extends Window {

	/**********************************************************
	 * Variables
	 **********************************************************/

	private Menu menuBar,gameMenu;
	/**
	 * The different items of the menu bar
	 */
	private MenuItem gameItem,joinTableItem,leaveTableItem,createTableItem,startGameItem;
	/**
	 * The different game buttons
	 */
	private Button foldButton,callButton,betButton,checkButton,raiseButton,dealButton,allInButton;
	/**
	 * The numerical fields in the game
	 */
	private Text betAmount,raiseAmount;
	/**
	 * The different images
	 */
	private Image table;
	/**********************************************************
	 * Layout Constants
	 **********************************************************/
	/**
	 * the height of the game buttons
	 */
	private int buttonHeight=30;
	/**
	 * the width of the game buttons
	 */
	private int buttonWidth=75;
	/**
	 * the vertical distance between the different buttons
	 */
	private int buttonDistance=45;
	/**
	 * the x coordinate of all game buttons
	 */
	private int xButtons=525;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Creates a new game window
	 * @super
	 */
	public WindowGame(Display display, ClientGUI gui) {
		super(display, gui);
		setAsCurrentWindow();
		getShell().setSize(750, 450);
		getShell().setText("Game Window");
		loadImages();
		loadMenuBar();
		loadButtons();
		draw();
	}
	/**********************************************************
	 * Images
	 **********************************************************/
	@Override public void draw(){
		getShell().open();
		while (!getShell().isDisposed()){
			drawImages();
			if (!getDisplay().readAndDispatch()){
				getDisplay().sleep();
			}
		}
	}
	/**
	 * Load the images from the respective files
	 */
	private void loadImages(){
		table=new Image(getDisplay(),"images/pokertafel.png");
	}
	/**
	 * Draws the images
	 */
	private void drawImages(){
		getGC().drawImage(table,100, 50);
	}
	/**********************************************************
	 * Menu Bar
	 **********************************************************/
	/**
	 * Loads the menu bar
	 */
	private void loadMenuBar(){
		final Shell gameShell=getShell();
		gameShell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(gameShell, style);
		        messageBox.setText("Information");
		        messageBox.setMessage("Are you sure you want to exit the game?");
		        event.doit = messageBox.open() == SWT.YES;
		      }
		    });
		
		menuBar = new Menu(gameShell , SWT.BAR);
		gameShell.setMenuBar(menuBar);
		
		gameItem = new MenuItem(menuBar,SWT.CASCADE);
		gameItem.setText("actions");
		gameMenu = new Menu(gameShell,SWT.DROP_DOWN);
		gameItem.setMenu(gameMenu);
		
		addJoinButton();
		addLeaveTableButton();
		addCreateTableButton();
		addStartGameButton();
	}
	/**********************************************************
	 * Menu Buttons
	 **********************************************************/
	private void addStartGameButton() {
		startGameItem = new MenuItem(gameMenu,SWT.PUSH);
		startGameItem.setText("Start Game &Ctrl+S");
		startGameItem.setEnabled(false);
		startGameItem.setAccelerator(SWT.CTRL + 'S');
		
		startGameItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to start a game");
				//TODO: handle event
			}
		}
		);
	}
	private void addCreateTableButton() {
		createTableItem = new MenuItem(gameMenu,SWT.PUSH);
		createTableItem.setText("Create Table &Ctrl+C");
		createTableItem.setEnabled(true);
		createTableItem.setAccelerator(SWT.CTRL + 'C');
		
		createTableItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to create a table");
				//TODO: handle event
			}
		}
		);
	}
	private void addLeaveTableButton() {
		leaveTableItem = new MenuItem(gameMenu,SWT.PUSH);
		leaveTableItem.setText("Leave Table &Ctrl+L");
		leaveTableItem.setEnabled(false);
		leaveTableItem.setAccelerator(SWT.CTRL + 'L');
		
		leaveTableItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to leave the table");
				//TODO: handle event
			}
		}
		);
	}
	private void addJoinButton() {
		joinTableItem = new MenuItem(gameMenu,SWT.PUSH);
		joinTableItem.setText("Join Table &Ctrl+J");
		joinTableItem.setEnabled(false);
		joinTableItem.setAccelerator(SWT.CTRL + 'J');
		
		joinTableItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to join a table");
				//TODO: request list of all tables and represent them as buttons
			}
		}
		);
	}
	/**********************************************************
	 * Game Buttons
	 **********************************************************/
	/**
	 * Loads all the game buttons
	 */
	private void loadButtons(){
		loadCallButton();
		loadBetButton();
		loadFoldButton();
		loadCheckButton();
		loadRaiseButton();
		loadDealButton();
		loadAllInButton();
	}
	/**
	 * Loads the fold button
	 */
	private void loadFoldButton() {
		foldButton=new Button(getShell(),SWT.PUSH);
		foldButton.setText("Fold");
		foldButton.setBounds(xButtons, 50, buttonWidth, buttonHeight);
		foldButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to fold");
				//TODO: handle event
				getGui().displayErrorMessage("test");
			}
		});
	}
	/**
	 * Loads the call button
	 */
	private void loadCallButton() {
		callButton=new Button(getShell(),SWT.PUSH);
		callButton.setText("Call");
		callButton.setBounds(xButtons, 50+buttonDistance, buttonWidth, buttonHeight);
		callButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to call");
				//TODO: handle event
			}
		});
	}
	/**
	 * Loads the check button
	 */
	private void loadCheckButton() {
		checkButton=new Button(getShell(),SWT.PUSH);
		checkButton.setText("Check");
		checkButton.setBounds(xButtons, 50+2*buttonDistance, buttonWidth, buttonHeight);
		checkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to check");
				//TODO: handle event
			}
		});
	}
	/**
	 * Load the deal button
	 */
	private void loadDealButton() {
		dealButton=new Button(getShell(),SWT.PUSH);
		dealButton.setText("Deal");
		dealButton.setBounds(xButtons, 50+3*buttonDistance, buttonWidth, buttonHeight);
		dealButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to deal");
				//TODO: handle event
			}
		});
	}
	/**
	 * Load the bet button
	 */
	private void loadBetButton() {
		betButton=new Button(getShell(),SWT.PUSH);
		betButton.setText("Bet");
		betButton.setBounds(xButtons, 50+4*buttonDistance, buttonWidth, buttonHeight);
		betButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to bet "+betAmount.getText());
				betAmount.setText("0");
				//TODO: handle event
			}
		});
		betAmount=new Text(getShell(),SWT.BORDER);
		betAmount.setText("0");
		betAmount.setBounds(xButtons+buttonWidth+5, betButton.getLocation().y, 30, buttonHeight);
	}
	/**
	 * Load the raise button
	 */
	private void loadRaiseButton() {
		raiseButton=new Button(getShell(),SWT.PUSH);
		raiseButton.setText("Raise");
		raiseButton.setBounds(xButtons, 50+5*buttonDistance, buttonWidth, buttonHeight);
		raiseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to raise "+raiseAmount.getText());
				raiseAmount.setText("0");
				//TODO: handle event
			}
		});
		raiseAmount=new Text(getShell(),SWT.BORDER);
		raiseAmount.setText("0");
		raiseAmount.setBounds(xButtons+buttonWidth+5, raiseButton.getLocation().y, 30, buttonHeight);
	}
	/**
	 * Load the all in button
	 */
	private void loadAllInButton() {
		allInButton=new Button(getShell(),SWT.PUSH);
		allInButton.setText("All In");
		allInButton.setBounds(xButtons, 50+6*buttonDistance, buttonWidth, buttonHeight);
		allInButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Attempt to go all in");
				//TODO: handle event
			}
		});
	}
}
