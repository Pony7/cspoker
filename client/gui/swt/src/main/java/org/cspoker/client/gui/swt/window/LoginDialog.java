package org.cspoker.client.gui.swt.window;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.SWTResourceManager;
import org.cspoker.client.rmi.RemoteLoginServerForRMI;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.context.ServerContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
public class LoginDialog
		extends ClientDialog {
	
	private CSPokerServer loginServer;
	private ServerContext result;
	
	public LoginDialog(Shell parent, int style, ClientGUI gui, ClientCore clientCore) {
		super(parent, style, clientCore);
		init();
	}
	
	static private Label userNameLabel;
	static private Text userNameText;
	static private Text passwordText;
	static private Label passwordLabel;
	static private Combo serverCombo;
	static private Label serverLabel;
	static private Composite composite4;
	static private Composite composite3;
	static private Composite composite2;
	static private Composite composite1;
	static private Button loginButton;
	
	public ServerContext open() {
		getParent().layout();
		getParent().pack();
		getParent().open();
		Display display = Display.getCurrent();
		while (!getParent().isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}
	
	private void init() {
		getParent().setText("Login to the CSPoker Server");
		getParent().setImage(SWTResourceManager.getImage(ClientGUI.CS_POKER_ICON));
		
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
				
				@Override
				public void widgetSelected(SelectionEvent evt) {
					doLogin(evt);
					
				}
				
			});
		}
	}
	
	/**
	 * @param evt
	 * @throws RemoteException
	 * @throws LoginException
	 */
	private void doLogin(SelectionEvent evt) {
		System.out.println("loginButton.mouseDown, event=" + evt);
		User newUser = new User(userNameText.getText(), passwordText.getText());
		clientCore.setUser(newUser);
		try {
			
			loginServer = new RemoteLoginServerForRMI(serverCombo.getText(), ClientCore.DEFAULT_PORT_RMI);
			result = loginServer.login(newUser.getUserName(), newUser.getPassword());
			getParent().close();
		} catch (NotBoundException e) {
			e.printStackTrace();
			ClientGUI.displayErrorMessage(e);
		} catch (RemoteException e) {
			e.printStackTrace();
			ClientGUI.displayErrorMessage(e);
		} catch (LoginException e) {
			e.printStackTrace();
			ClientGUI.displayErrorMessage(e);
		}
		
	}
}
