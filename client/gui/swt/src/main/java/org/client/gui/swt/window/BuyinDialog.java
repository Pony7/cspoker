package org.cspoker.client.gui.swt.window;

import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
public class BuyinDialog
		extends ClientDialog {
	
	public BuyinDialog(TableUserInputComposite tableUserInputComposite) {
		this(new Shell(Display.getDefault(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), SWT.NONE, tableUserInputComposite
				.getGui(), tableUserInputComposite.getClientCore(), tableUserInputComposite);
		
	}
	
	public BuyinDialog(Shell parent, int style, ClientGUI gui, ClientCore clientCore,
			TableUserInputComposite tableUserInputComposite) {
		super(parent, style, gui, clientCore);
		this.tableUserInputComposite = tableUserInputComposite;
		init();
	}
	
	static private Label rebuyAmountLabel;
	static private Text rebuyAmountText;
	static private Composite composite1;
	static private Button rebuyButton;
	static private Text maxRebuyText;
	static private Label maxRebuyLabel;
	
	private final TableUserInputComposite tableUserInputComposite;
	
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
		getParent().setText("Table Cashier");
		getParent().setMinimumSize(250, 100);
		GridLayout dialogShellLayout = new GridLayout();
		dialogShellLayout.makeColumnsEqualWidth = true;
		getParent().setLayout(dialogShellLayout);
		composite1 = new Composite(getParent(), SWT.NONE);
		GridLayout composite1Layout = new GridLayout(2, true);
		composite1Layout.makeColumnsEqualWidth = true;
		composite1.setLayout(composite1Layout);
		GridData composite1LData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		composite1.setLayoutData(composite1LData);
		{
			{
				rebuyAmountLabel = new Label(composite1, SWT.CENTER);
				rebuyAmountLabel.setText("Amount:");
				rebuyAmountLabel.setBounds(5, 20, 60, 30);
			}
			{
				rebuyAmountText = new Text(composite1, SWT.SINGLE | SWT.CENTER | SWT.BORDER);
				rebuyAmountText.setBounds(38, 20, 60, 30);
				// TODO Add logic which checks for valid amount when issuing
				// rebuy request
				rebuyAmountText.setText(ClientGUI.formatBet(tableUserInputComposite.getGameState().getTableMemento()
						.getGameProperty().getBigBlind() * 100));
			}
		}
		{
			{
				maxRebuyLabel = new Label(composite1, SWT.CENTER);
				maxRebuyLabel.setText("Table Maximum:");
				maxRebuyLabel.setBounds(5, 50, 60, 30);
			}
			{
				maxRebuyText = new Text(composite1, SWT.CENTER | SWT.BORDER);
				// TODO maybe different rebuy allowed from 100 BB?
				maxRebuyText.setText(ClientGUI.formatBet(tableUserInputComposite.getGameState().getTableMemento()
						.getGameProperty().getBigBlind() * 100));
				maxRebuyText.setEditable(false);
			}
		}
		{
			rebuyButton = new Button(composite1, SWT.PUSH | SWT.CENTER);
			GridData loginButtonLData = new GridData();
			loginButtonLData.horizontalAlignment = GridData.CENTER;
			rebuyButton.setLayoutData(loginButtonLData);
			rebuyButton.setText("Rebuy");
			rebuyButton.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent evt) {
					System.out.println("rebuyButton.mouseDown, event=" + evt);
					try {
						clientCore.getCommunication().rebuy(tableUserInputComposite.getTableId(),
								ClientGUI.betFormatter.parse(rebuyAmountText.getText()).intValue() * 100);
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
					}
				}
			});
		}
	}
}
