package org.client.GUI.Window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.client.ClientCore;
import org.client.GUI.ClientGUI;
import org.cspoker.common.game.elements.table.TableId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A class of windows used to let the user select the table he wants to join
 * @author Cedric
 *
 */
public class WindowTableSelection extends Window {
	/**
	 * the height of the buttons
	 */
	private int buttonHeight=30;
	/**
	 * the width of the buttons
	 */
	private int buttonWidth=75;
	private int xButton=10;
	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The list of tables of this window
	 */
	private List<TableId> tables=new ArrayList<TableId>();
	
	private TableId selectedTable;
	
	private Label label;
	private int xLabel=75;
	private int yLabel=40;
	/**
	 * Layout constants of the shell
	 */
	private int shellHeigth=250;
	private int shellWidth=300;
	/**
	 * The text field of this table selection window and it's layout constants
	 */
	private Text text;
	private String defaultText="Table Selected: ";
	private int widthText=150;
	private int heigthText=30;
	/**
	 * The selection list of this window
	 */
	private org.eclipse.swt.widgets.List selectionList;
	private int xList=xLabel;
	private int yList=yLabel+heigthText+5;
	
	private Button joinButton,refreshButton;
	/**********************************************************
	 * Constructor
	 **********************************************************/
	public WindowTableSelection(Display display, ClientGUI gui,ClientCore clientCore) {
		super(display, gui,clientCore);
		intitializeWindow();
		setAsCurrentWindow();
		draw();
	}
	/**
	 * Sets the initial settings of this window
	 */
	private void intitializeWindow() {
		createSelectionShell();
		createSelectionList();
		createTextField();
		refreshTables();
		loadJoinButton();
		loadRefreshButton();
	}
	private void loadRefreshButton() {
		int x=text.getBounds().x+buttonWidth+5;
		int y=text.getBounds().y+buttonHeight+10;
		refreshButton=new Button(getShell(),SWT.PUSH);
		refreshButton.setText("REFRESH");
		refreshButton.setBounds(x, y, buttonWidth, buttonHeight);
		refreshButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				refreshTables();
			}
		});
	}
	private void loadJoinButton() {
		int x=text.getBounds().x;
		int y=text.getBounds().y+buttonHeight+10;
		joinButton=new Button(getShell(),SWT.PUSH);
		joinButton.setText("JOIN");
		joinButton.setBounds(x, y, buttonWidth, buttonHeight);
		joinButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(selectedTable==null)
					gui.displayErrorMessage("You have to select a table first!");
				else {
					System.out.println("Attempt to join: "+selectedTable.toString());
					getClientCore().joinTable(selectedTable);
				}
			}
		});
	}
	/**
	 * Creates the text field of this window
	 */
	private void createTextField() {
		 text=new Text(shell, SWT.BORDER);
		 text.setEditable(false);
		 text.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
	}
	/**
	 * Creates the selection list of this window
	 */
	private void createSelectionList() {
		label=new Label(getShell(), SWT.BORDER|SWT.BOLD|SWT.CENTER);
		label.setText("Click on the table");
		label.setLocation(xLabel,yLabel);
		label.setSize(widthText,heigthText);

		selectionList=new org.eclipse.swt.widgets.List(shell, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL |SWT.CENTER);
		selectionList.setBounds(xList, yList, widthText, 100);
		
		selectionList.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent event){
		    	int index=selectionList.getSelectionIndex();
		    	selectedTable=tables.get(index);
		        text.setText(defaultText+ selectedTable.toString());
		        System.out.println("Selected "+selectedTable.toString());
		      }
		      public void widgetDefaultSelected(SelectionEvent event) {
		        text.setText(defaultText);
		      }
		    });
	}
	/**
	 * Creates the shell of this window
	 */
	private void createSelectionShell() {
		shell.setSize(shellWidth, shellHeigth);
		shell.setText("Select your table ");
		shell.addListener(SWT.Close, new Listener() {
		      public void handleEvent(Event event) {
		        int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
		        MessageBox messageBox = new MessageBox(getShell(), style);
		        messageBox.setText("Information");
		        messageBox.setMessage("Close the login screen?");
		        event.doit = messageBox.open() == SWT.YES;
		      }
		    });
		shell.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
	}
	/**********************************************************
	 * Tables
	 **********************************************************/
	/**
	 * Makes the user join the table with the given table ID
	 */
	private void selectTable(TableId id){
		System.out.println("Selected Table: "+id.toString());
	}
	private void clearSelectionList(){
		selectionList.removeAll();
	}
	private void refreshTables(){
		System.out.println("Attempt to refresh tables");
		clearSelectionList();
		tables=getClientCore().getTableList();
		Iterator tableIterator=tables.iterator();
		while(tableIterator.hasNext()){
			selectionList.add(((TableId) tableIterator.next()).toString());
		}
		selectedTable=null;
		reLayOut();
	}
	/**
	 * Lay-out
	 */
	private void reLayOut(){
		selectionList.pack();
		Rectangle rect=selectionList.getBounds();
		rect.width=widthText;
		selectionList.setBounds(rect);
		int xText=selectionList.getLocation().x;
		int yText=10+selectionList.getLocation().y+selectionList.getItemCount()*selectionList.getItemHeight();
		text.setBounds(xText,yText,widthText,heigthText);
		text.setText(defaultText);
	}
	/**********************************************************
	 * Images
	 **********************************************************/
	@Override
	public void drawImages() {
		// TODO Auto-generated method stub
	}

}
