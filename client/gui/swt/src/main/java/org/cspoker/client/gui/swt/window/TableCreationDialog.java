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
	
	private LobbyWindow lobby;
	
	public TableCreationDialog(LobbyWindow lobby) {
		super(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE, lobby.getClientCore());
		this.lobby = lobby;
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
				nameInput.setText(clientCore.getUser().getUserName() + "'s table");
				
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
						
						lobby.getContext().createHoldemTable(nameInput.getText(), tConfig);
						getParent().close();
					} catch (Exception e) {
						e.printStackTrace();
						
						switch (ClientGUI.displayErrorMessage(e)) {
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
