/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.window;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.common.api.chat.event.ChatEvent;
import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.event.LobbyEvent;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * The main lobby window. Listens to {@link LobbyEvent}s and {@link ChatEvent}s
 * from the Server.
 */
public class LobbyWindow
		extends ClientComposite
		implements LobbyListener, ChatListener {
	
	private final static Logger logger = Logger.getLogger(LobbyWindow.class);
	/** {@link LobbyContext} for callbacks to server. */
	private RemoteLobbyContext context;
	
	/**
	 * @return The {@link LobbyContext} provided by the server at login.
	 */
	public RemoteLobbyContext getContext() {
		return context;
	}
	
	// ************************************************************
	// SWT Member Variables
	// ************************************************************
	
	private Menu menu1;
	private Button createTableButton;
	private TableItem tableItem1;
	private Composite composite1;
	private TableColumn playersColumn;
	private TableColumn typeColumn;
	private TableColumn stakesColumn;
	private TableColumn idColumn;
	private MenuItem aboutMenuItem;
	private MenuItem contentsMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private TableColumn nameColumn;
	private org.eclipse.swt.widgets.Table availableGameTables;
	private CTabItem cTabItem1;
	private CTabFolder tableFolder;
	private MenuItem exitMenuItem;
	private MenuItem newFileMenuItem;
	private Menu lobbyMenu;
	private MenuItem lobbyMenuItem;
	
	private MenuItem uiMenuItem;
	private Menu uiMenu;
	private MenuItem cardsMenuItem;
	private MenuItem chipsMenuItem;
	
	private Menu cardsMenu;
	private Menu chipsMenu;
	
	private MenuItem starsCardsMenuItem;
	private MenuItem ftpCardsMenuItem;
	private MenuItem starsChipsMenuItem;
	private MenuItem eptChipsMenuItem;
	private MenuItem pokerWikiaChipsMenuItem;
	
	/**
	 * Creates and initializes the SWT components of the Lobby.
	 * 
	 * @param core The {@link ClientCore}
	 * @throws IllegalArgumentException If the retrieval of the
	 *             {@link RemoteLobbyContext} for callbacks failed
	 */
	public LobbyWindow(ClientCore core)
			throws IllegalArgumentException {
		super(new Shell(core.getGui().getDisplay()), SWT.NONE, core);
		try {
			this.context = core.getCommunication().getLobbyContext(this);
		} catch (RemoteException e) {
			throw new IllegalArgumentException(e);
		}
		initGUI();
		createCloseListener();
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	/**
	 * Initializes the GUI. SWT stuff.
	 */
	private void initGUI() {
		try {
			setSize(new Point(400, 300));
			setLayout(new FillLayout(SWT.HORIZONTAL));
			{
				tableFolder = new CTabFolder(this, SWT.NONE);
				{
					cTabItem1 = new CTabItem(tableFolder, SWT.NONE);
					cTabItem1.setText("Tables");
					cTabItem1.setToolTipText("Available Tables");
					{
						composite1 = new Composite(tableFolder, SWT.NONE);
						cTabItem1.setControl(composite1);
						GridLayout composite1Layout = new GridLayout();
						composite1Layout.makeColumnsEqualWidth = true;
						composite1.setLayout(composite1Layout);
						{
							GridData table1LData = new GridData();
							table1LData.widthHint = 343;
							table1LData.heightHint = 164;
							availableGameTables = new org.eclipse.swt.widgets.Table(composite1, SWT.SINGLE | SWT.BORDER);
							availableGameTables.setLayoutData(table1LData);
							availableGameTables.setHeaderVisible(true);
							availableGameTables.setLinesVisible(true);
							availableGameTables.addMouseListener(new MouseAdapter() {
								
								@Override
								public void mouseDoubleClick(MouseEvent evt) {
									System.out.println("table1.mouseDoubleClick, event=" + evt);
									TableItem[] selectedItems = availableGameTables.getSelection();
									if (selectedItems.length == 1) {
										// Open selected table
										long tid = (Long.parseLong(selectedItems[0].getText(1)));
										
										GameWindow w = getClientCore().getGui().getGameWindow(tid, true);
										w.show();
									}
								}
							});
							availableGameTables.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									System.out.println("table1.widgetSelected, event=" + evt);
									// TODO add your code for
									// table1.widgetSelected
								}
							});
							{
								nameColumn = new TableColumn(availableGameTables, SWT.NONE);
								nameColumn.setText("Name");
								nameColumn.setWidth(82);
							}
							{
								idColumn = new TableColumn(availableGameTables, SWT.NONE);
								idColumn.setText("Id");
								idColumn.setWidth(15);
							}
							{
								stakesColumn = new TableColumn(availableGameTables, SWT.NONE);
								stakesColumn.setText("Stakes");
								stakesColumn.setWidth(60);
							}
							{
								typeColumn = new TableColumn(availableGameTables, SWT.NONE);
								typeColumn.setText("Type");
								typeColumn.setWidth(60);
							}
							{
								playersColumn = new TableColumn(availableGameTables, SWT.NONE);
								playersColumn.setText("Players");
								playersColumn.setWidth(60);
							}
							{
								tableItem1 = new TableItem(availableGameTables, SWT.NONE);
								tableItem1.setText("No tables available ...");
							}
						}
						{
							createTableButton = new Button(composite1, SWT.PUSH | SWT.CENTER);
							GridData createTableButtonLData = new GridData();
							createTableButtonLData.horizontalAlignment = GridData.CENTER;
							createTableButtonLData.verticalAlignment = GridData.END;
							createTableButtonLData.grabExcessVerticalSpace = true;
							createTableButton.setLayoutData(createTableButtonLData);
							createTableButton.setText("Create Your Own Table");
							createTableButton.addMouseListener(new MouseAdapter() {
								
								@Override
								public void mouseDown(MouseEvent evt) {
									logger.info("Table creation requested ...");
									new TableCreationDialog(LobbyWindow.this).open();
								}
							});
						}
					}
				}
				tableFolder.setSelection(0);
			}
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				
				{
					lobbyMenuItem = new MenuItem(menu1, SWT.CASCADE);
					lobbyMenuItem.setText("Lobby");
					{
						lobbyMenu = new Menu(lobbyMenuItem);
						{
							newFileMenuItem = new MenuItem(lobbyMenu, SWT.CASCADE);
							newFileMenuItem.setText("Login ...");
							newFileMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									System.out.println("newFileMenuItem.widgetSelected, event=" + evt);
									// clientCore.login(clientCore., port,
									// userName, password)
								}
							});
						}
						{
							exitMenuItem = new MenuItem(lobbyMenu, SWT.CASCADE);
							exitMenuItem.setText("Exit");
							exitMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									System.out.println("exitMenuItem.widgetSelected, event=" + evt);
									getShell().getDisplay().close();
									// TODO Leave all open tables
									// TODO Log out (via AccountListener??)
								}
							});
						}
						lobbyMenuItem.setMenu(lobbyMenu);
					}
				}
				
				{
					uiMenuItem = new MenuItem(menu1, SWT.CASCADE);
					uiMenuItem.setText("Appearance");
					{
						uiMenu = new Menu(uiMenuItem);
						uiMenuItem.setMenu(uiMenu);
						{
							cardsMenuItem = new MenuItem(uiMenu, SWT.CASCADE);
							cardsMenuItem.setText("Cards");
						}
						
						cardsMenu = new Menu(cardsMenuItem);
						cardsMenuItem.setMenu(cardsMenu);
						starsCardsMenuItem = new MenuItem(cardsMenu, SWT.RADIO);
						starsCardsMenuItem.setText("Poker Stars");
						starsCardsMenuItem.setSelection(true);
						starsCardsMenuItem.addSelectionListener(new SelectionAdapter() {
							
							@Override
							public void widgetSelected(SelectionEvent evt) {
								try {
									ClientGUI.setActiveCardDeck(ClientGUI.Resources.STARS_DECK_IMG_FILE);
								} catch (FileNotFoundException e) {
									logger.error("Could not change card deck style", e);
								}
							}
						});
						ftpCardsMenuItem = new MenuItem(cardsMenu, SWT.RADIO);
						ftpCardsMenuItem.setText("Full Tilt Poker");
						ftpCardsMenuItem.addSelectionListener(new SelectionAdapter() {
							
							@Override
							public void widgetSelected(SelectionEvent evt) {
								try {
									ClientGUI.setActiveCardDeck(ClientGUI.Resources.FTP_DECK_IMG_FILE);
								} catch (FileNotFoundException e) {
									logger.error("Could not change card deck style", e);
								}
							}
						});
						{
							chipsMenuItem = new MenuItem(uiMenu, SWT.CASCADE);
							chipsMenuItem.setText("Chips");
							
							chipsMenu = new Menu(chipsMenuItem);
							chipsMenuItem.setMenu(chipsMenu);
							starsChipsMenuItem = new MenuItem(chipsMenu, SWT.RADIO);
							starsChipsMenuItem.setText("Poker Stars");
							starsChipsMenuItem.setSelection(true);
							starsChipsMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									try {
										ClientGUI.setActiveChipsStyle(ClientGUI.Resources.STARS_CHIP_IMG_DIR);
									} catch (FileNotFoundException e) {
										logger.error("Could not change chip style", e);
									}
								}
							});
							eptChipsMenuItem = new MenuItem(chipsMenu, SWT.RADIO);
							eptChipsMenuItem.setText("European Poker Tour");
							eptChipsMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									try {
										ClientGUI.setActiveChipsStyle(ClientGUI.Resources.EPT_CHIP_IMG_DIR);
									} catch (FileNotFoundException e) {
										logger.error("Could not change chip style", e);
									}
								}
							});
							
							pokerWikiaChipsMenuItem = new MenuItem(chipsMenu, SWT.RADIO);
							pokerWikiaChipsMenuItem.setText("Poker Wikia (Free) Chips");
							pokerWikiaChipsMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									try {
										ClientGUI.setActiveChipsStyle(ClientGUI.Resources.FREE_CHIP_IMAGE_FILE);
									} catch (FileNotFoundException e) {
										logger.error("Could not change chip style", e);
									}
								}
							});
						}
					}
				}
				
				{
					helpMenuItem = new MenuItem(menu1, SWT.CASCADE);
					helpMenuItem.setText("Help");
					{
						helpMenu = new Menu(helpMenuItem);
						{
							contentsMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							contentsMenuItem.setText("Contents");
						}
						{
							aboutMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							aboutMenuItem.setText("About");
							aboutMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									MessageBox aboutInfo = new MessageBox(getShell());
									aboutInfo.setMessage("CSPoker SWT Client 0.1");
									aboutInfo.setText("About");
									aboutInfo.open();
								}
							});
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
			getShell().setImage(SWTResourceManager.getImage(ClientGUI.Resources.CS_POKER_ICON));
			getShell().addShellListener(new ShellAdapter() {
				
				@Override
				public void shellClosed(ShellEvent evt) {
				// TODO Log out (via AccountListener??)
				}
			});
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens this {@link LobbyWindow} in a new shell and listens to the OS event
	 * queue until this window's shell is disposed.
	 */
	public void show() {
		Shell shell = getShell();
		shell.setText("CSPoker - Logged in as " + getClientCore().getUser().getUserName());
		shell.setLayout(new FillLayout());
		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}
	}
	
	/**
	 * Asks the server for an updated snapshot of all available tables, and
	 * updates its display status according to the received {@link TableList}.
	 */
	public void refreshTables() {
		TableList tl;
		java.util.List<DetailedHoldemTable> tables = new ArrayList<DetailedHoldemTable>();
		try {
			tl = context.getTableList();
			for (Table t : tl.getTables()) {
				tables.add(context.getHoldemTableInformation(t.getId()));
			}
		} catch (RemoteException e) {
			getClientCore().handleRemoteException(e);
		}
		
		for (DetailedHoldemTable t : tables) {
			insertInformation(t);
		}
	}
	
	/**
	 * Very simply just refreshs all tables for now TODO Slim down
	 * 
	 * @throws RemoteException
	 * @see org.cspoker.common.api.lobby.listener.LobbyListener#onTableCreated(org.cspoker.common.api.lobby.event.TableCreatedEvent)
	 */
	public void onTableCreated(TableCreatedEvent tableCreatedEvent) {
		Table t = tableCreatedEvent.getTable();
		// TODO Get detail information to display in the list from the server
		try {
			DetailedHoldemTable detailedTable = context.getHoldemTableInformation(t.getId());
			insertInformation(detailedTable);
		} catch (RemoteException e) {
			getClientCore().handleRemoteException(e);
		}
	}
	
	/**
	 * Helper method to insert the information for a given table into the
	 * {@link #availableGameTables}
	 * 
	 * @param t A {@link DetailedHoldemTable} object containing all the relevant
	 *            information
	 */
	private void insertInformation(DetailedHoldemTable t) {
		assert (t != null) : "Cannot insert information, passed null parameter";
		TableConfiguration tInfo = t.getGameProperty();
		TableItem item = new TableItem(availableGameTables, SWT.NONE);
		item.setText(new String[] {
				t.getName(),
				Long.toString(t.getId()),
				ClientGUI.betFormatter.format(tInfo.getSmallBlind()) + "/"
						+ ClientGUI.betFormatter.format(tInfo.getBigBlind()), "Holdem No Limit",
				Integer.toString(t.getNbPlayers()) + "/" + tInfo.getMaxNbPlayers() });
		item.setData(t);
	}
	
	/**
	 * Looks for the given table and removes them from the list displayed in the
	 * UI
	 * 
	 * @see org.cspoker.common.api.lobby.listener.LobbyListener#onTableRemoved(org.cspoker.common.api.lobby.event.TableRemovedEvent)
	 */
	public void onTableRemoved(TableRemovedEvent tableRemovedEvent) {
		// Search for the table and dispose of the widget
		for (TableItem ti : availableGameTables.getItems()) {
			if (((Table) ti.getData()).getId() == tableRemovedEvent.getTableId().getId()) {
				ti.dispose();
				redraw();
				return;
			}
		}
	}
	
	/**
	 * @param serverMessageEvent The server message event
	 * @see org.cspoker.common.api.chat.listener.ChatListener#onServerMessage(org.cspoker.common.api.chat.event.ServerMessageEvent)
	 */
	public void onServerMessage(ServerMessageEvent serverMessageEvent) {
		// TODO Show the server event maybe in the lobby as well
		for (GameWindow openWindow : getClientCore().getGui().getGameWindows()) {
			serverMessageEvent.dispatch(openWindow.getUserInputComposite());
		}
		
	}
	
	/**
	 * @param tableMessageEvent The table message event
	 * @see org.cspoker.common.api.chat.listener.ChatListener#onTableMessage(org.cspoker.common.api.chat.event.TableMessageEvent)
	 */
	public void onTableMessage(TableMessageEvent tableMessageEvent) {
		// TODO Don't we need a table id here?
		long tableId = 0;
		GameWindow relevantWindow = getClientCore().getGui().getGameWindow(tableId, false);
		assert (relevantWindow != null) : "Window for table " + tableId + " not found";
		tableMessageEvent.dispatch(relevantWindow.getUserInputComposite());
	}
}
