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

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.GameState;
import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * The bottom composite of the {@link GameWindow}.
 * <p>
 * In this composite reside all the components where the user can execute
 * actions, i.e. Call/Fold/Raise/Sit In/Sit Out/Leave buttons and the Chat Box
 * 
 * @author stephans
 */
class TableUserInputComposite
		extends ClientComposite
		implements ChatListener {
	
	private final Logger logger = Logger.getLogger(TableUserInputComposite.class);
	
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
	private SeatedPlayer userSnapshot;
	private GameState gameState;
	
	public TableUserInputComposite(GameWindow parent, int style, HoldemTableContext holdemTableContext) {
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
			// Available in SWT version 3.4
			// group2Layout.center = true;
			gameActionGroup.setLayout(group2Layout);
			GridData group2LData = new GridData();
			group2LData.grabExcessHorizontalSpace = true;
			group2LData.horizontalAlignment = SWT.CENTER;
			gameActionGroup.setLayoutData(group2LData);
			{
				manualEnterBetGroup = new Composite(gameActionGroup, SWT.NONE);
				RowLayout manualEnterBetGroupLayout = new RowLayout(SWT.HORIZONTAL);
				// Available in SWT version 3.4
				// manualEnterBetGroupLayout.center = true;
				manualEnterBetGroupLayout.justify = true;
				manualEnterBetGroup.setLayout(manualEnterBetGroupLayout);
				manualEnterBetGroup.setLayoutData(new RowData(300, 40));
				{
					betSlider = new Slider(manualEnterBetGroup, SWT.NONE);
					betSlider.setIncrement(gameState.getTableMemento().getGameProperty().getSmallBlind());
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
									+ ClientGUI.formatBet(gameState.getMinBetRaiseAmount()
											+ GameState.getValue(gameState.getCurrentBetPile())));
							try {
								int desiredAmount = ClientGUI.betFormatter.parse(betAmountTextField.getText())
										.intValue();
								if (desiredAmount - gameState.getToCallAmount() >= gameState.getMinBetRaiseAmount()) {
									setNewBetRaiseAmount(desiredAmount - gameState.getToCallAmount());
								}
							} catch (ParseException ex) {
								logger.error("Could not parse manual bet input", ex);
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
		logger.info("Users turn");
		boolean toCallAllIn = gameState.getRemainingStack() <= gameState.getToCallAmount();
		updateCheckCallButton(toCallAllIn);
		if (!toCallAllIn) {
			setNewBetRaiseAmount(gameState.getMinBetRaiseAmount());
			betAmountTextField.selectAll();
			betAmountTextField.setFocus();
		}
		gameActionGroup.setVisible(true);
		foldCallRaiseButtonGroup.setVisible(true);
		betRaiseButton.setVisible(!toCallAllIn);
		manualEnterBetGroup.setVisible(!toCallAllIn);
	}
	
	private void updateCheckCallButton(boolean allIn) {
		String text = (gameState.getToCallAmount() == 0) ? "Check" : "Call "
				+ ClientGUI.betFormatter.format(Math.min(gameState.getRemainingStack(), gameState.getToCallAmount()));
		
		checkCallButton.setText(text);
		if (allIn) {
			markAllIn(checkCallButton);
		}
		
	}
	
	private void markAllIn(Button button) {
		button.setText(button.getText() + " (All In)");
		button.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
		
	}
	
	void rebuyButtonWidgetSelected(SelectionEvent evt) {
		new BuyinDialog(getClientCore(), null, gameState.getTableMemento().getGameProperty().getBigBlind()).open();
	}
	
	private void setNewBetRaiseAmount(int amount) {
		betRaiseAmount = amount;
		updateBetSlider();
		updateBetRaiseButton();
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
		betSlider.setMaximum(userSnapshot.getStackValue() + userSnapshot.getBetChipsValue());
		// native windows bug fix;
		betSlider.setSelection(userSnapshot.getStackValue() + userSnapshot.getBetChipsValue());
		int extras = betSlider.getMaximum() - betSlider.getSelection();
		if (extras != 0) {
			betSlider.setMaximum(betSlider.getMaximum() + extras);
		}
		betSlider.setMinimum(gameState.getMinBetRaiseAmount() + GameState.getValue(gameState.getCurrentBetPile()));
		betSlider.setSelection(betRaiseAmount + GameState.getValue(gameState.getCurrentBetPile()));
	}
	
	void updateBetRaiseButton() {
		int totalBetRaiseAmount = betRaiseAmount + GameState.getValue(gameState.getCurrentBetPile());
		boolean isAllIn = (gameState.getToCallAmount() + betRaiseAmount == gameState.getRemainingStack());
		String text = (gameState.getBetChipsThisRound() > 0) ? "Raise to " : "Bet";
		betRaiseButton.setText(text + ClientGUI.betFormatter.format(totalBetRaiseAmount));
		if (isAllIn) {
			markAllIn(betRaiseButton);
		}
	}
	
	/**
	 * Adds the message to the Chat Box (in red)
	 * 
	 * @see org.cspoker.common.api.chat.listener.ChatListener#onServerMessage(org.cspoker.common.api.chat.event.ServerMessageEvent)
	 */
	public void onServerMessage(ServerMessageEvent serverMessageEvent) {
		// Display server messages in red
		gameInfoText.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		gameInfoText.append(System.getProperty("line.separator") + serverMessageEvent.getPlayer() + ": "
				+ serverMessageEvent.getMessage());
		gameInfoText.setTopIndex(gameInfoText.getLineCount() - 5);
		
	}
	
	/**
	 * Adds the message to the Chat Box (in black)
	 * 
	 * @see org.cspoker.common.api.chat.listener.ChatListener#onTableMessage(org.cspoker.common.api.chat.event.TableMessageEvent)
	 */
	public void onTableMessage(TableMessageEvent tableMessageEvent) {
		// Display standard messages in black
		gameInfoText.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gameInfoText.append(System.getProperty("line.separator") + tableMessageEvent.getPlayer() + ": "
				+ tableMessageEvent.getMessage());
		gameInfoText.setTopIndex(gameInfoText.getLineCount() - 5);
		
	}
	
	public HoldemPlayerContext getPlayerContext() {
		return tableContext.getHoldemPlayerContext();
	}
	
}
