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

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.client.rmi.RemoteRMIServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * A Simple Dialog which asks the user to provide a user name and corresponding
 * credentials as well as a server he wants to log into.
 */
public class LoginDialog
		extends ClientDialog {
	
	private final static Logger logger = Logger.getLogger(LoginDialog.class);
	
	/**
	 * Create and initialize new login dialog
	 * 
	 * @param parent The containing shell
	 * @param style The relevant style bits
	 * @param clientCore The {@link ClientCore}
	 */
	public LoginDialog(Shell parent, int style, ClientCore clientCore) {
		super(parent, style, clientCore);
		initGUI();
	}
	
	private Label userNameLabel;
	private Text userNameText;
	private Text passwordText;
	private Label passwordLabel;
	private Combo serverCombo;
	private Label serverLabel;
	private Composite composite4;
	private Composite composite3;
	private Composite composite2;
	private Composite composite1;
	private Button loginButton;
	
	/**
	 * Open this Dialog in a new shell.
	 * <p>
	 * This method blocks until the user has either
	 * <li>disposed of this dialog
	 * <li>pressed the <code>Login</code> button
	 */
	public void open() {
		getParent().layout();
		getParent().pack();
		getParent().open();
		Display display = clientCore.getGui().getDisplay();
		while (!getParent().isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
	/**
	 * SWT initialization
	 */
	private void initGUI() {
		getParent().setText("Login to the CSPoker Server");
		getParent().setImage(SWTResourceManager.getImage(ClientGUI.Resources.CS_POKER_ICON));
		
		getParent().setMinimumSize(250, 100);
		GridLayout dialogShellLayout = new GridLayout();
		dialogShellLayout.makeColumnsEqualWidth = true;
		getParent().setLayout(dialogShellLayout);
		composite1 = new Composite(getParent(), SWT.NONE);
		GridLayout composite1Layout = new GridLayout();
		composite1Layout.makeColumnsEqualWidth = true;
		composite1.setLayout(composite1Layout);
		GridData composite1LData = new GridData();
		composite1LData.grabExcessHorizontalSpace = true;
		composite1LData.grabExcessVerticalSpace = true;
		composite1LData.horizontalAlignment = GridData.CENTER;
		composite1.setLayoutData(composite1LData);
		{
			composite2 = new Composite(composite1, SWT.NONE);
			FillLayout composite2Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			GridData composite2LData = new GridData();
			composite2LData.horizontalAlignment = GridData.CENTER;
			composite2LData.grabExcessHorizontalSpace = true;
			composite2.setLayoutData(composite2LData);
			composite2.setLayout(composite2Layout);
			{
				userNameLabel = new Label(composite2, SWT.CENTER);
				userNameLabel.setText("User Name:");
				userNameLabel.setBounds(5, 20, 60, 30);
			}
			{
				userNameText = new Text(composite2, SWT.SINGLE | SWT.CENTER);
				userNameText.setBounds(38, 20, 60, 30);
				userNameText.setText(clientCore.getUser().getUserName());
			}
		}
		{
			composite3 = new Composite(composite1, SWT.NONE);
			FillLayout composite3Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			GridData composite3LData = new GridData();
			composite3LData.horizontalAlignment = GridData.CENTER;
			composite3.setLayoutData(composite3LData);
			composite3.setLayout(composite3Layout);
			{
				passwordLabel = new Label(composite3, SWT.CENTER);
				passwordLabel.setText("Password:");
				passwordLabel.setBounds(5, 50, 60, 30);
			}
			{
				passwordText = new Text(composite3, SWT.CENTER);
				passwordText.setText(clientCore.getUser().getPassword());
				passwordText.setEchoChar('*');
			}
			composite4 = new Composite(composite1, SWT.NONE);
			FillLayout composite4Layout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
			GridData composite4LData = new GridData();
			composite3LData.horizontalAlignment = GridData.CENTER;
			composite4.setLayoutData(composite4LData);
			composite4.setLayout(composite4Layout);
			{
				serverLabel = new Label(composite4, SWT.CENTER);
				serverLabel.setText("Server:");
				serverLabel.setBounds(5, 50, 60, 30);
			}
			{
				serverCombo = new Combo(composite4, SWT.CENTER);
				serverCombo.add(ClientCore.DEFAULT_URL);
				serverCombo.add("192.168.178.21");
				serverCombo.select(0);
				serverCombo.setBounds(38, 20, 60, 30);
			}
		}
		{
			loginButton = new Button(composite1, SWT.PUSH | SWT.CENTER);
			GridData loginButtonLData = new GridData();
			loginButtonLData.horizontalAlignment = GridData.CENTER;
			loginButton.setLayoutData(loginButtonLData);
			loginButton.setText("Login");
			loginButton.addSelectionListener(new SelectionAdapter() {
				
				private RemoteServerContext result;
				
				/**
				 * Parse the user and password fields, set the ClientCore's user
				 * to the relevant new user, and try to login.
				 * 
				 * @param evt Ignored
				 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
				 */
				@Override
				public void widgetSelected(SelectionEvent evt) {
					try {
						// RemoteCSPokerServer server =
						// CommunicationProvider.global_provider.getProviders().get(0);
						clientCore.setUser(new User(userNameText.getText(), passwordText.getText()));
						RemoteCSPokerServer server = new RemoteRMIServer(ClientCore.DEFAULT_URL,
								ClientCore.DEFAULT_PORT_RMI);
						result = clientCore.login(server);
						assert (result != null) : "No exception thrown but still no Server Context?!";
						getParent().close();
					} catch (LoginException e) {
						ClientGUI.displayException(e);
					}
					// Make sure we do have a RemoteServerContext before exiting
					// the LoginDialog
					
				}
			});
		}
	}
}
