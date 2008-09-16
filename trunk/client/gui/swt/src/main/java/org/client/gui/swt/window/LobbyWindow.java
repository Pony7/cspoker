package org.cspoker.client.gui.swt.window;

import java.rmi.RemoteException;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.common.eventlisteners.server.AllServerEventsListener;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableChangedEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.common.events.serverevents.TableRemovedEvent;
import org.cspoker.common.exceptions.IllegalActionException;
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
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class LobbyWindow
		extends ClientComposite
		implements AllServerEventsListener {
	
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
	private Table table1;
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
	{
		// Register as a resource user - SWTResourceManager will
		// handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}
	
	public LobbyWindow(Display display, final ClientGUI gui, final ClientCore clientCore) {
		super(new Shell(display), gui, clientCore, SWT.NONE);
		gui.lobby = this;
		init();
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void init() {
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
							table1 = new Table(composite1, SWT.SINGLE | SWT.BORDER);
							table1.setLayoutData(table1LData);
							table1.setHeaderVisible(true);
							table1.setLinesVisible(true);
							table1.addMouseListener(new MouseAdapter() {
								
								@Override
								public void mouseDoubleClick(MouseEvent evt) {
									System.out.println("table1.mouseDoubleClick, event=" + evt);
									TableItem[] selectedItems = table1.getSelection();
									if (selectedItems.length == 1) {
										// Open selected table
										TableId tid = new TableId(Long.parseLong(selectedItems[0].getText(1)));
										gui.getGameWindow(tid).show();
									}
								}
							});
							table1.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									System.out.println("table1.widgetSelected, event=" + evt);
									// TODO add your code for
									// table1.widgetSelected
								}
							});
							{
								nameColumn = new TableColumn(table1, SWT.NONE);
								nameColumn.setText("Name");
								nameColumn.setWidth(82);
							}
							{
								idColumn = new TableColumn(table1, SWT.NONE);
								idColumn.setText("Id");
								idColumn.setWidth(15);
							}
							{
								stakesColumn = new TableColumn(table1, SWT.NONE);
								stakesColumn.setText("Stakes");
								stakesColumn.setWidth(60);
							}
							{
								typeColumn = new TableColumn(table1, SWT.NONE);
								typeColumn.setText("Type");
								typeColumn.setWidth(60);
							}
							{
								playersColumn = new TableColumn(table1, SWT.NONE);
								playersColumn.setText("Players");
								playersColumn.setWidth(60);
							}
							{
								tableItem1 = new TableItem(table1, SWT.NONE);
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
									createTableButtonMouseDown(evt);
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
									try {
										for (GameWindow gw : gui.gameWindows) {
											clientCore.getCommunication().leaveTable(gw.getTableId());
										}
									} catch (RemoteException e) {
										e.printStackTrace();
									} catch (IllegalActionException e) {
										e.printStackTrace();
									}
									try {
										clientCore.getCommunication().kill();
									} catch (RemoteException e) {
										e.printStackTrace();
									} catch (IllegalActionException e) {
										e.printStackTrace();
									}
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
								ClientGUI.setActiveCardDeck(ClientGUI.STARS_DECK_IMG_FILE);
							}
						});
						ftpCardsMenuItem = new MenuItem(cardsMenu, SWT.RADIO);
						ftpCardsMenuItem.setText("Full Tilt Poker");
						ftpCardsMenuItem.addSelectionListener(new SelectionAdapter() {
							
							@Override
							public void widgetSelected(SelectionEvent evt) {
								ClientGUI.setActiveCardDeck(ClientGUI.FTP_DECK_IMG_FILE);
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
									ClientGUI.setActiveChipsStyle(ClientGUI.STARS_CHIP_IMG_DIR);
								}
							});
							eptChipsMenuItem = new MenuItem(chipsMenu, SWT.RADIO);
							eptChipsMenuItem.setText("European Poker Tour");
							eptChipsMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									ClientGUI.setActiveChipsStyle(ClientGUI.EPT_CHIP_IMG_DIR);
								}
							});
							
							pokerWikiaChipsMenuItem = new MenuItem(chipsMenu, SWT.RADIO);
							pokerWikiaChipsMenuItem.setText("Poker Wikia (Free) Chips");
							pokerWikiaChipsMenuItem.addSelectionListener(new SelectionAdapter() {
								
								@Override
								public void widgetSelected(SelectionEvent evt) {
									ClientGUI.setActiveChipsStyle(ClientGUI.FREE_CHIP_IMAGE_FILE);
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
									aboutInfo.setMessage("CSPoker Client 0.1");
									aboutInfo.setText("About");
									aboutInfo.open();
								}
							});
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
			getShell().setImage(SWTResourceManager.getImage(ClientGUI.CS_POKER_ICON));
			getShell().addShellListener(new ShellAdapter() {
				
				@Override
				public void shellClosed(ShellEvent evt) {
					try {
						clientCore.getCommunication().kill();
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (IllegalActionException e) {
						e.printStackTrace();
					}
				}
			});
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void show() {
		Shell shell = getShell();
		shell.setText("CSPoker - Logged in as " + clientCore.getUser().getUserName());
		shell.setLayout(new FillLayout());
		shell.setSize(getSize());
		
		shell.open();
		refreshTables();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}
	}
	
	public void refreshTables() {
		TableList tl = clientCore.getTables();
		table1.clearAll();
		table1.setItemCount(0);
		for (org.cspoker.common.elements.table.Table t : tl.getTables()) {
			GameProperty gp = t.getGameProperty();
			TableItem item = new TableItem(table1, SWT.NONE);
			item.setText(new String[] { t.getName(), Long.toString(t.getId().getId()),
					ClientGUI.formatBet(gp.getSmallBlind()) + "/" + ClientGUI.formatBet(gp.getBigBlind()),
					"Holdem No Limit", Integer.toString(t.getNbPlayers()) + "/" + gp.getMaxNbPlayers() });
		}
	}
	
	@Override
	public void onTableCreatedEvent(TableCreatedEvent event) {
		refreshTables();
	}
	
	@Override
	public void onTableChangedEvent(TableChangedEvent event) {
		refreshTables();
	}
	
	@Override
	public void onTableRemovedEvent(TableRemovedEvent event) {
		refreshTables();
	}
	
	@Override
	public void onServerMessageEvent(ServerMessageEvent event) {
		refreshTables();
	}
	
	private void createTableButtonMouseDown(MouseEvent evt) {
		System.out.println("Table creation requested ...");
		// clientCore.createTable();
		new TableCreationDialog(gui, clientCore).open();
	}
}
