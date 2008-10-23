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

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog for guiding the user through the process of creating a table.
 * <p>
 * Does not yet contain any checks whether the user has the necessary
 * credentials to create tables
 * 
 * @author Stephan Schmidt
 */
public class TableCreationDialog
		extends ClientDialog {
	
	private final static Logger logger = Logger.getLogger(TableCreationDialog.class);
	
	private LobbyWindow lobby;
	
	/**
	 * Creates and initializes a new table creation dialog, allowing the user to
	 * specify the desired table name, game type and stakes.
	 * 
	 * @param lobby The {@link LobbyWindow} where the dialog was invoked from.
	 */
	public TableCreationDialog(LobbyWindow lobby) {
		super(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE, lobby.getClientCore());
		this.lobby = lobby;
		init();
	}
	
	private Label nameLabel;
	private Label stakeLabel;
	private Label gameTypeLabel;
	private Label nbPlayersLabel;
	
	private Composite holderComposite;
	
	private Text nameInput;
	private Combo stakeCombo;
	private Combo gameTypeCombo;
	private Combo nbPlayersCombo;
	
	static private Button createTableButton;
	
	/**
	 * Open the dialog and wait for user input. This dialog is closed when the
	 * <code>createTableButton</code> is pressed.
	 */
	public void open() {
		getParent().layout();
		getParent().pack();
		getParent().setLocation(getParent().toDisplay(100, 100));
		getParent().open();
		Display display = Display.getCurrent();
		while (!getParent().isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	/**
	 * Dialog initialization of SWT components.
	 */
	private void init() {
		getParent().setText("Create your own table");
		getParent().setMinimumSize(250, 100);
		GridLayout dialogShellLayout = new GridLayout(1, true);
		getParent().setLayout(dialogShellLayout);
		holderComposite = new Composite(getParent(), SWT.NONE);
		GridLayout composite1Layout = new GridLayout(2, false);
		holderComposite.setLayout(composite1Layout);
		GridData composite1LData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		holderComposite.setLayoutData(composite1LData);
		{
			{
				nameLabel = new Label(holderComposite, SWT.CENTER);
				nameLabel.setText("Table name:");
				
				nameInput = new Text(holderComposite, SWT.CENTER | SWT.BORDER);
				nameInput.setText(clientCore.getUser().getUserName() + "'s table");
				
				stakeLabel = new Label(holderComposite, SWT.CENTER);
				stakeLabel.setText("Amount:");
				stakeLabel.setBounds(5, 20, 60, 30);
				
				stakeCombo = new Combo(holderComposite, SWT.READ_ONLY);
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
				
				nbPlayersLabel = new Label(holderComposite, SWT.CENTER);
				nbPlayersLabel.setText("Max # of players:");
				nbPlayersLabel.setBounds(5, 20, 60, 30);
				
				nbPlayersCombo = new Combo(holderComposite, SWT.READ_ONLY);
				nbPlayersCombo.add("2");
				nbPlayersCombo.add("6");
				nbPlayersCombo.add("9");
				nbPlayersCombo.add("10");
				nbPlayersCombo.select(1);
				
			}
			
		}
		{
			{
				gameTypeLabel = new Label(holderComposite, SWT.CENTER);
				gameTypeLabel.setText("Game Type:");
				gameTypeLabel.setBounds(5, 50, 60, 30);
				
				gameTypeCombo = new Combo(holderComposite, SWT.READ_ONLY);
				gameTypeCombo.add("No Limit Holdem");
				// TODO Add more game types as they become available
				// gameTypeCombo.add("Pot Limit Holdem");
				// gameTypeCombo.add("Limit Holdem");
				// gameTypeCombo.add("Pot Limit Omaha Hi");
				gameTypeCombo.select(0);
			}
			
		}
		{
			createTableButton = new Button(holderComposite, SWT.PUSH | SWT.CENTER);
			GridData loginButtonLData = new GridData();
			loginButtonLData.horizontalAlignment = GridData.CENTER;
			createTableButton.setLayoutData(loginButtonLData);
			createTableButton.setText("Create");
			createTableButton.addSelectionListener(new SelectionAdapter() {
				
				/**
				 * Performs a request to create the table according to the
				 * user-selected parameters.
				 * <p>
				 * Upon success, closes this dialog's shell.
				 * 
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent evt) {
					logger.debug("tableCreationButton.mouseDown, event=" + evt);
					try {
						int smallBlind = (int) ((Double.parseDouble(stakeCombo.getText().substring(0,
								stakeCombo.getText().indexOf("/")))) * 100);
						TableConfiguration tConfig = new TableConfiguration(smallBlind * 2, 2000);
						
						try {
							lobby.getContext().createHoldemTable(nameInput.getText(), tConfig);
						} catch (IllegalActionException e) {
							logger.error("This should not happen", e);
						}
						getParent().close();
					} catch (RemoteException e) {
						clientCore.handleRemoteException(e);
					}
				}
			});
		}
	}
}
