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

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
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
	private Combo communicationCombo;
	private Label serverLabel;
	private Label communicationLabel;
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
		getParent().addShellListener(new ShellAdapter() {
			
			@Override
			public void shellClosed(ShellEvent e) {
				if (!loginButton.isFocusControl())
					System.exit(0);
			}
		});
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
		
		GridData ldata = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		ldata.widthHint = 70;
		getParent().setMinimumSize(250, 100);
		GridLayout dialogShellLayout = new GridLayout();
		dialogShellLayout.makeColumnsEqualWidth = true;
		getParent().setLayout(dialogShellLayout);
		composite1 = new Composite(getParent(), SWT.NONE);
		composite1.setLayout(new GridLayout(2, false));
		composite1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		{
			{
				userNameLabel = new Label(composite1, SWT.CENTER);
				userNameLabel.setText("User Name:");
			}
			{
				userNameText = new Text(composite1, SWT.SINGLE | SWT.CENTER | SWT.BORDER);
				userNameText.setText(clientCore.getUser().getUserName());
				userNameText.setLayoutData(ldata);
				
			}
		}
		{
			{
				passwordLabel = new Label(composite1, SWT.CENTER);
				passwordLabel.setText("Password:");
			}
			{
				passwordText = new Text(composite1, SWT.SINGLE | SWT.CENTER | SWT.BORDER);
				passwordText.setText(clientCore.getUser().getPassword());
				passwordText.setEchoChar('*');
				passwordText.setLayoutData(ldata);
			}
			{
				serverLabel = new Label(composite1, SWT.CENTER);
				serverLabel.setText("Server:");
				// serverLabel.setBounds(5, 50, 60, 30);
			}
			{
				serverCombo = new Combo(composite1, SWT.CENTER);
				serverCombo.add(ClientCore.DEFAULT_URL);
				serverCombo.add("192.168.178.21");
				serverCombo.select(0);
				// serverCombo.setBounds(38, 20, 60, 30);
				
				communicationLabel = new Label(composite1, SWT.CENTER);
				communicationLabel.setText("Communication:");
				// communicationLabel.setBounds(5, 50, 60, 30);
				
				communicationCombo = new Combo(composite1, SWT.CENTER);
				communicationCombo.add("RMI");
				communicationCombo.add("HTTP");
				communicationCombo.add("SOCKETS");
				communicationCombo.select(0);
				// communicationCombo.setBounds(38, 20, 60, 30);
			}
		}
		{
			loginButton = new Button(getParent(), SWT.PUSH | SWT.CENTER);
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
//					try {
						// RemoteCSPokerServer server =
						// CommunicationProvider.global_provider.getProviders().get(0);
						clientCore.setUser(new User(userNameText.getText(), passwordText.getText()));
						String communicationType = communicationCombo.getText();
						RemoteCSPokerServer server = null;
//						if (communicationType.equalsIgnoreCase("RMI")) {
//							server = new RemoteRMIServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_RMI);
//						} else if (communicationType.equalsIgnoreCase("HTTP")) {
//							server = new RemoteHTTPServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_HTTP);
//						} else if (communicationType.equalsIgnoreCase("SOCKETS")) {
//							server = new RemoteSocketServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_SOCKET);
//						} else {
							throw new IllegalArgumentException("No communication specified");
//						}
//						result = clientCore.login(server);
//						assert (result != null) : "No exception thrown but still no Server Context?!";
//						getParent().close();
//					} catch (LoginException e) {
//						ClientGUI.displayException(e);
//					}
					// Make sure we do have a RemoteServerContext before exiting
					// the LoginDialog
					
				}
			});
		}
	}
}
