package org.cspoker.client.gui.swt.window;

import java.text.ParseException;

import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.GameState;
import org.cspoker.common.api.chat.event.ChatListener;
import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.lobby.holdemtable.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.HoldemPlayerContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class TableUserInputComposite
		extends ClientComposite
		implements ChatListener {
	
	int betRaiseAmount;
	Text betAmountTextField;
	Button betRaiseButton;
	Button checkCallButton;
	Button foldButton;
	Composite foldCallRaiseButtonGroup;
	Composite gameActionGroup;
	StyledText gameInfoText;
	Button leaveButton;
	Button sitInOutButton;
	Button potButton;
	Slider betSlider;
	Composite manualEnterBetGroup;
	Button rebuyButton;
	Composite composite2;
	
	HoldemTableContext tableContext;
	
	public TableUserInputComposite(ClientComposite parent, int style, HoldemTableContext holdemTableContext) {
		super(parent, style, parent.getClientCore());
		gameState = parent.getGameState();
		tableContext = holdemTableContext;
		initGUI();
		gameActionGroup.setVisible(false);
	}
	
	void betRaiseButtonMouseDown(MouseEvent evt) {
		tableContext.getHoldemPlayerContext().betOrRaise(betRaiseAmount);
		gameActionGroup.setVisible(false);
	}
	
	void checkCallButtonMouseDown(MouseEvent evt) {
		System.out.println("callButton.mouseDown, event=" + evt);
		tableContext.getHoldemPlayerContext().checkOrCall();
		gameActionGroup.setVisible(false);
	}
	
	void foldButtonMouseDown(MouseEvent evt) {
		System.out.println("foldButton.mouseDown, event=" + evt);
		tableContext.getHoldemPlayerContext().fold();
		gameActionGroup.setVisible(false);
	}
	
	/**
	 * @return The game log to append event info to
	 */
	public StyledText getGameInfoText() {
		return gameInfoText;
	}
	
	long getTableId() {
		return gameState.getTableMemento().getId();
	}
	
	private void initGUI() {
		
		setLayout(new GridLayout(3, false));
		setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
		{
			gameInfoText = new StyledText(this, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
			gameInfoText.setLayoutData(new GridData(242, 100));
		}
		{
			gameActionGroup = new Composite(this, SWT.NONE);
			RowLayout group2Layout = new RowLayout(SWT.VERTICAL);
			group2Layout.center = true;
			gameActionGroup.setLayout(group2Layout);
			GridData group2LData = new GridData();
			group2LData.grabExcessHorizontalSpace = true;
			group2LData.horizontalAlignment = SWT.CENTER;
			gameActionGroup.setLayoutData(group2LData);
			{
				manualEnterBetGroup = new Composite(gameActionGroup, SWT.NONE);
				RowLayout manualEnterBetGroupLayout = new RowLayout(SWT.HORIZONTAL);
				manualEnterBetGroupLayout.center = true;
				manualEnterBetGroupLayout.justify = true;
				manualEnterBetGroup.setLayout(manualEnterBetGroupLayout);
				manualEnterBetGroup.setLayoutData(new RowData(300, 40));
				{
					betSlider = new Slider(manualEnterBetGroup, SWT.NONE);
					betSlider.setIncrement(getGameState().getTableMemento().getGameProperty().getSmallBlind());
					betSlider.setPageIncrement(betSlider.getIncrement() * 5);
					betSlider.setLayoutData(new RowData(150, 25));
					betSlider.addSelectionListener(new SelectionAdapter() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							setNewBetRaiseAmount(betSlider.getSelection()
									- GameState.getValue(gameState.getCurrentBetPile()));
						}
					});
				}
				{
					potButton = new Button(manualEnterBetGroup, SWT.PUSH | SWT.CENTER);
					potButton.setText("Pot");
					potButton.setSize(30, 30);
					potButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							System.err.println("Got pot raise: " + gameState.getPotRaiseAmount());
							setNewBetRaiseAmount(gameState.getPotRaiseAmount());
						}
					});
				}
				{
					betAmountTextField = new Text(manualEnterBetGroup, SWT.CENTER | SWT.BORDER);
					betAmountTextField.setLayoutData(new RowData(52, 20));
					betAmountTextField.setText(ClientGUI.formatBet(0));
					betAmountTextField.addKeyListener(new KeyAdapter() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							betAmountTextField.setToolTipText("Minimum is "
									+ ClientGUI.formatBet(gameState.getMinRaiseAmount()
											+ GameState.getValue(gameState.getCurrentBetPile())));
							try {
								int desiredAmount = parseAmount(betAmountTextField.getText());
								if (desiredAmount >= gameState.getMinRaiseAmount()) {
									setNewBetRaiseAmount(desiredAmount);
								}
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								return;
							}
							
						}
					});
				}
			}
			{
				foldCallRaiseButtonGroup = new Composite(gameActionGroup, SWT.NONE);
				foldCallRaiseButtonGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
				foldCallRaiseButtonGroup.setLayoutData(new RowData(350, 40));
				{
					foldButton = new Button(foldCallRaiseButtonGroup, SWT.PUSH | SWT.CENTER);
					foldButton.setText("Fold");
					foldButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							foldButtonMouseDown(evt);
						}
					});
				}
				{
					checkCallButton = new Button(foldCallRaiseButtonGroup, SWT.PUSH | SWT.CENTER);
					checkCallButton.setText("Call");
					checkCallButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							checkCallButtonMouseDown(evt);
						}
					});
				}
				{
					betRaiseButton = new Button(foldCallRaiseButtonGroup, SWT.PUSH | SWT.CENTER);
					betRaiseButton.setText("Raise");
					betRaiseButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							betRaiseButtonMouseDown(evt);
						}
					});
				}
			}
			{
				GridData composite2LData = new GridData(80, 100);
				composite2LData.verticalAlignment = SWT.CENTER;
				composite2 = new Composite(this, SWT.NONE);
				composite2.setLayout(new FillLayout(SWT.VERTICAL));
				composite2.setLayoutData(composite2LData);
				{
					sitInOutButton = new Button(composite2, SWT.TOGGLE | SWT.CENTER);
					sitInOutButton.setText("Sit In");
					sitInOutButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							sitInOutButtonMouseDown(evt);
						}
					});
				}
				{
					leaveButton = new Button(composite2, SWT.PUSH | SWT.CENTER);
					leaveButton.setText("Leave Table");
					leaveButton.addSelectionListener(new SelectionAdapter() {
						
						@Override
						public void widgetSelected(SelectionEvent evt) {
							leaveButtonWidgetSelected(evt);
						}
					});
				}
				{
					rebuyButton = new Button(composite2, SWT.PUSH | SWT.CENTER);
					rebuyButton.setText("Rebuy");
					rebuyButton.addSelectionListener(new SelectionAdapter() {
						
						@Override
						public void widgetSelected(SelectionEvent evt) {
							rebuyButtonWidgetSelected(evt);
						}
					});
				}
			}
		}
	}
	
	void leaveButtonWidgetSelected(SelectionEvent evt) {
		System.out.println("leaveButton.widgetSelected, event=" + evt);
		
		tableContext.getHoldemPlayerContext().leaveGame();
		tableContext.leaveTable();
		getClientCore().getGui().getGameWindows().remove(getParent());
		getShell().close();
	}
	
	void prepareForUserInput() {
		System.err.println("Users turn");
		setNewBetRaiseAmount(gameState.getMinRaiseAmount());
		gameActionGroup.setVisible(true);
		foldCallRaiseButtonGroup.setVisible(true);
		betAmountTextField.selectAll();
		betAmountTextField.setFocus();
	}
	
	void rebuyButtonWidgetSelected(SelectionEvent evt) {
		new BuyinDialog(getClientCore(), null, getGameState().getTableMemento().getGameProperty().getBigBlind()).open();
	}
	
	private void setNewBetRaiseAmount(int amount) {
		betRaiseAmount = amount;
		updateBetSlider();
		updateButtons();
		if (!betAmountTextField.isFocusControl()) {
			betAmountTextField.setText(ClientGUI.formatBet(betRaiseAmount
					+ GameState.getValue(gameState.getCurrentBetPile())));
		}
	}
	
	void sitInOutButtonMouseDown(MouseEvent evt) {
		System.out.println("sitInOutButton.widgetSelected, event=" + evt);
		
		if (!sitInOutButton.getSelection()) {
			// FIXME Get free seat id ...
			tableContext.sitIn(0);
		} else {
			// TODO new sit out concept
			// clientCore.getCommunication().sitOut(getTableId());
		}
	}
	
	void updateBetSlider() {
		// +10 is some weirdo behavior/bug??
		betSlider.setMaximum(gameState.getUser().getStackValue() + gameState.getUser().getBetChipsValue());
		betSlider.setSelection(gameState.getUser().getStackValue() + gameState.getUser().getBetChipsValue());
		// native windows bug fix;
		int extras = betSlider.getMaximum() - betSlider.getSelection();
		if (extras != 0) {
			betSlider.setMaximum(betSlider.getMaximum() + extras);
		}
		betSlider.setMinimum(gameState.getMinRaiseAmount() + GameState.getValue(gameState.getCurrentBetPile()));
		betSlider.setSelection(betRaiseAmount + GameState.getValue(gameState.getCurrentBetPile()));
	}
	
	void updateButtons() {
		int stackLeft = gameState.getUser().getStackValue();
		int betChips = gameState.getUser().getBetChipsValue();
		int toCall = gameState.getToCallAmount();
		
		if (toCall == 0) {
			if (betChips > 0) {
				// Preflop is special ...
				betRaiseButton.setText("Raise to "
						+ ClientGUI.formatBet(betRaiseAmount
								+ gameState.getTableMemento().getGameProperty().getBigBlind()));
			} else {
				betRaiseButton.setText("Bet " + ClientGUI.formatBet(betRaiseAmount));
				if (toCall + betRaiseAmount >= stackLeft) {
					betRaiseButton.setText(betRaiseButton.getText() + " (ALL IN)");
					betRaiseButton.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				}
			}
			checkCallButton.setText("Check");
		} else {
			betRaiseButton.setText("Raise to "
					+ ClientGUI.formatBet(betRaiseAmount + GameState.getValue(gameState.getCurrentBetPile())));
			checkCallButton.setText("Call " + ClientGUI.formatBet(Math.min(stackLeft, toCall)));
		}
		betRaiseButton.setVisible(true);
		if (toCall >= stackLeft) {
			betRaiseButton.setVisible(false);
			checkCallButton.setText(checkCallButton.getText() + " (ALL IN)");
		}
	}
	
	/**
	 * FIXME Horrible, this should be done by the {@link ClientGUI#betFormatter}
	 * 
	 * @return
	 * @throws ParseException
	 */
	private int parseAmount(String input)
			throws ParseException {
		int desiredAmount = 0;
		
		input = input.replaceAll("€", "");
		input = input.replaceAll(" ", "");
		if (input.indexOf(",") != -1) {
			input = input.replaceAll(",", ".");
		}
		if (input.indexOf(".") == -1) {
			input = input.concat(".00");
		}
		if (input.indexOf(".") == input.length() - 2) {
			input = input.concat("0");
		}
		
		if (input.indexOf("€") == -1) {
			input = input.concat(" €");
		}
		desiredAmount = ClientGUI.betFormatter.parse(input).intValue()
				- GameState.getValue(gameState.getCurrentBetPile());
		
		return desiredAmount;
	}
	
	public void onServerMessage(ServerMessageEvent serverMessageEvent) {
		// Display server messages in red
		gameInfoText.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		gameInfoText.append(System.getProperty("line.separator") + serverMessageEvent.getPlayer() + ": "
				+ serverMessageEvent.getMessage());
		gameInfoText.setTopIndex(gameInfoText.getLineCount() - 5);
		
	}
	
	public void onTableMessage(TableMessageEvent tableMessageEvent) {
		// Display standard messages in black
		gameInfoText.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gameInfoText.append(System.getProperty("line.separator") + tableMessageEvent.getPlayer() + ": "
				+ tableMessageEvent.getMessage());
		gameInfoText.setTopIndex(gameInfoText.getLineCount() - 5);
		
	}
	
	public HoldemPlayerContext getPlayerContext() {
		// TODO Auto-generated method stub
		return tableContext.getHoldemPlayerContext();
	}
}
