package org.client.GUI.Window;

import java.awt.GraphicsEnvironment;

import org.client.ClientCore;
import org.client.GUI.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
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
	private Image table=createTableImage();
	/**********************************************************
	 * Layout Constants
	 **********************************************************/
	private int tableXPosition=(int) (getWindowWidth()*0.1);
	private int tableYPosition=(int) (getWindowHeight()*0.1);
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
	public WindowGame(Display display, ClientGUI gui,ClientCore clientCore) {
		super(display, gui,clientCore);
		createGameShell();
		createCloseListener();
		loadMenuBar();
		loadButtons();
		setAsCurrentWindow();
		draw();
	}
	/**********************************************************
	 * Shell
	 **********************************************************/
	/**
	 * Creates a new game shell
	 */
	private void createGameShell() {
		getShell().setMaximized(true);
		getShell().setText("Game Window");
	}
	/**********************************************************
	 * Images
	 **********************************************************/
	/**
	 * Draws the images
	 */
	@Override public void drawImages(){
		drawTable();
	}
	/**********************************************************
	 * Table
	 **********************************************************/
	/**
	 * Creates a new scaled table image for this game window
	 */
	private Image createTableImage() {
		Image temp=imageFactory.getImage(getDisplay(),"pokertafel");
		int width = (int) (getWindowWidth()*0.8);
		int height = (int) (getWindowHeight()*0.7);
		return new Image(getDisplay(),temp.getImageData().scaledTo(width,height));
	}
	/**
	 * Draws the table on this window game
	 */
	private void drawTable() {
		getGC().drawImage(table,tableXPosition,tableYPosition);
	}
	/**********************************************************
	 * Menu Bar
	 **********************************************************/
	/**
	 * Loads the menu bar
	 */
	private void loadMenuBar(){
		menuBar = new Menu(getShell() , SWT.BAR);
		getShell().setMenuBar(menuBar);
		
		gameItem = new MenuItem(menuBar,SWT.CASCADE);
		gameItem.setText("actions");
		gameMenu = new Menu(getShell(),SWT.DROP_DOWN);
		gameItem.setMenu(gameMenu);
		
		addJoinButton();
		addLeaveTableButton();
		addCreateTableButton();
		addStartGameButton();
	}
	/**********************************************************
	 * Menu Buttons
	 **********************************************************/
	/**
	 * Adds the start game button to this game window
	 */
	private void addStartGameButton() {
		startGameItem = new MenuItem(gameMenu,SWT.PUSH);
		startGameItem.setText("Start Game &Ctrl+S");
		startGameItem.setEnabled(false);
		startGameItem.setAccelerator(SWT.CTRL + 'S');
		
		startGameItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to start a game");
				getClientCore().startGame();
			}
		}
		);
	}
	/**
	 * Adds the create table button to this game window
	 */
	private void addCreateTableButton() {
		createTableItem = new MenuItem(gameMenu,SWT.PUSH);
		createTableItem.setText("Create Table &Ctrl+C");
		createTableItem.setEnabled(true);
		createTableItem.setAccelerator(SWT.CTRL + 'C');
		
		createTableItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to create a table");
				getClientCore().createTable();
			}
		}
		);
	}
	/**
	 * Adds the leave table button to this game window
	 */
	private void addLeaveTableButton() {
		leaveTableItem = new MenuItem(gameMenu,SWT.PUSH);
		leaveTableItem.setText("Leave Table &Ctrl+L");
		leaveTableItem.setEnabled(false);
		leaveTableItem.setAccelerator(SWT.CTRL + 'L');
		
		leaveTableItem.addListener( SWT.Selection, new Listener()
		{
			public void handleEvent(Event e) {
				System.out.println("Attempt to leave the table");
				getClientCore().leaveTable();
			}
		}
		);
	}
	/**
	 * Adds the join button to this game window
	 */
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
				getClientCore().fold();
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
				getClientCore().call();
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
				getClientCore().check();
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
				getClientCore().deal();
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
				int amount=Integer.parseInt(betAmount.getText());
				System.out.println("Attempt to bet "+amount);
				getClientCore().bet(amount);
				betAmount.setText("0");
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
				int amount=Integer.parseInt(betAmount.getText());
				System.out.println("Attempt to raise "+amount);
				getClientCore().raise(amount);
				raiseAmount.setText("0");
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
				getClientCore().allIn();
			}
		});
	}
}
