package org.cspoker.client.gui.swt.window;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.common.elements.table.TableConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class TableCreationDialog
		extends ClientDialog {
	
	public TableCreationDialog(ClientGUI gui, ClientCore core) {
		this(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE, gui, core);
		
	}
	
	public TableCreationDialog(Shell parent, int style, ClientGUI gui, ClientCore clientCore) {
		super(parent, style, gui, clientCore);
		init();
	}
	
	private Label nameLabel;
	private Label stakeLabel;
	private Label gameTypeLabel;
	private Label nbPlayersLabel;
	
	private Composite composite1;
	
	private Text nameInput;
	private Combo stakeCombo;
	private Combo gameTypeCombo;
	private Combo nbPlayersCombo;
	
	static private Button createTableButton;
	
	public void open() {
		try {
			getParent().layout();
			getParent().pack();
			getParent().setLocation(getParent().toDisplay(100, 100));
			getParent().open();
			Display display = Display.getCurrent();
			while (!getParent().isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		getParent().setText("Create your own table");
		getParent().setMinimumSize(250, 100);
		GridLayout dialogShellLayout = new GridLayout(1, true);
		getParent().setLayout(dialogShellLayout);
		composite1 = new Composite(getParent(), SWT.NONE);
		GridLayout composite1Layout = new GridLayout(2, false);
		composite1.setLayout(composite1Layout);
		GridData composite1LData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		composite1.setLayoutData(composite1LData);
		{
			{
				nameLabel = new Label(composite1, SWT.CENTER);
				nameLabel.setText("Table name:");
				
				nameInput = new Text(composite1, SWT.CENTER | SWT.BORDER);
				nameInput.setText(getClientCore().getUser().getUserName() + "'s table");
				
				stakeLabel = new Label(composite1, SWT.CENTER);
				stakeLabel.setText("Amount:");
				stakeLabel.setBounds(5, 20, 60, 30);
				
				stakeCombo = new Combo(composite1, SWT.READ_ONLY);
				stakeCombo.add("0.01/0.02");
				stakeCombo.add("0.05/0.10");
				stakeCombo.add("0.25/0.50");
				stakeCombo.add("0.50/1");
				stakeCombo.add("1/2");
				stakeCombo.add("2/4");
				stakeCombo.add("5/10");
				stakeCombo.add("10/20");
				stakeCombo.add("25/50");
				stakeCombo.add("50/100");
				stakeCombo.add("100/200");
				stakeCombo.select(stakeCombo.getItemCount() - 1);
				
				nbPlayersLabel = new Label(composite1, SWT.CENTER);
				nbPlayersLabel.setText("Max # of players:");
				nbPlayersLabel.setBounds(5, 20, 60, 30);
				
				nbPlayersCombo = new Combo(composite1, SWT.READ_ONLY);
				nbPlayersCombo.add("2");
				nbPlayersCombo.add("6");
				nbPlayersCombo.add("9");
				nbPlayersCombo.add("10");
				nbPlayersCombo.select(1);
				
			}
			
		}
		{
			{
				gameTypeLabel = new Label(composite1, SWT.CENTER);
				gameTypeLabel.setText("Game Type:");
				gameTypeLabel.setBounds(5, 50, 60, 30);
				
				gameTypeCombo = new Combo(composite1, SWT.READ_ONLY);
				gameTypeCombo.add("No Limit Holdem");
				// gameTypeCombo.add("Pot Limit Holdem");
				// gameTypeCombo.add("Limit Holdem");
				// gameTypeCombo.add("Pot Limit Omaha Hi");
				gameTypeCombo.select(0);
			}
			
		}
		{
			createTableButton = new Button(composite1, SWT.PUSH | SWT.CENTER);
			GridData loginButtonLData = new GridData();
			loginButtonLData.horizontalAlignment = GridData.CENTER;
			createTableButton.setLayoutData(loginButtonLData);
			createTableButton.setText("Create");
			createTableButton.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent evt) {
					System.out.println("tableCreationButton.mouseDown, event=" + evt);
					try {
						int smallBlind = (int) ((Double.parseDouble(stakeCombo.getText().substring(0,
								stakeCombo.getText().indexOf("/")))) * 100);
						TableConfiguration tConfig = new TableConfiguration(smallBlind * 2, 2000);
						
						clientCore.getCommunication().createTable(nameInput.getText(), tConfig);
						getParent().close();
					} catch (Exception e) {
						e.printStackTrace();
						
						switch (gui.displayErrorMessage(e)) {
							case SWT.RETRY:
								return;
							default:
								if (!getParent().isDisposed())
									getParent().close();
						}
						getParent().close();
					}
				}
			});
		}
	}
}
