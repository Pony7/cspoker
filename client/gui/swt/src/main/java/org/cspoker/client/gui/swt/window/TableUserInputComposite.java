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
import org.cspoker.client.gui.swt.control.ClientGUI;
import org.cspoker.client.gui.swt.control.UserSeatedPlayer;
import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

/**
 * The bottom composite of the {@link GameWindow}.
 * <p>
 * In this composite reside all the components where the user can execute
 * actions, i.e. Call/Fold/Raise/Sit In/Sit Out/Leave buttons and the Chat Box
 * 
 * @author stephans
 */
public class TableUserInputComposite
		extends ClientComposite
		implements ChatListener {
	
	private final Logger logger = Logger.getLogger(TableUserInputComposite.class);
	
	Text userInputBox;
	StyledText gameInfoText;
	
	Composite gameActionGroup;
	
	Composite manualEnterBetGroup;
	Composite generalActionHolder;
	Slider betSlider;
	Text betAmountTextField;
	
	Composite foldCallRaiseButtonGroup;
	Button betRaiseButton;
	Button checkCallButton;
	Button foldButton;
	
	Button leaveButton;
	Button sitInOutButton;
	Button potButton;
	
	Button rebuyButton;
	
	int betRaiseAmount;
	
	private final UserSeatedPlayer user;
	
	/**
	 * Creates a new TableUserInputComposite
	 * <p>
	 * This Composite contains all elements for communicating with the server:
	 * Bet/Raise/Fold buttons, Sit In /Sit Out, Leave Game/Rebuy, Chat etc.
	 * 
	 * @param parent The containing {@link GameWindow}
	 * @param style The relevant style bits
	 */
	public TableUserInputComposite(GameWindow parent, int style) {
		super(parent, style);
		user = parent.getUser();
		initGUI();
	}
	
	void betRaiseButtonMouseDown(MouseEvent evt) {
		logger.debug("Bet/Raise button pressed");
		try {
			user.getPlayerContext().betOrRaise(betRaiseAmount);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			return;
		} catch (IllegalActionException e) {
			logger.error("This should not happen ", e);
		}
		gameActionGroup.setVisible(false);
	}
	
	void checkCallButtonMouseDown(MouseEvent evt) {
		logger.debug("Check/Call button pressed");
		try {
			user.getPlayerContext().checkOrCall();
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			return;
		} catch (IllegalActionException e) {
			logger.error("This should not happen ", e);
		}
		gameActionGroup.setVisible(false);
	}
	
	void foldButtonMouseDown(MouseEvent evt) {
		logger.debug("Fold button pressed");
		try {
			user.getPlayerContext().fold();
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalActionException e) {
			logger.error("This should not happen ", e);
		}
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
		GridData tableUserInputCompositeLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableUserInputCompositeLData.heightHint = 250;
		tableUserInputCompositeLData.minimumHeight = 100;
		
		setLayoutData(tableUserInputCompositeLData);
		{
			Composite chatBoxHolder = new Composite(this, SWT.NONE);
			chatBoxHolder.setLayout(new GridLayout(1, true));
			GridData chatBoxHolderData = new GridData(SWT.FILL, SWT.FILL, true, true);
			chatBoxHolderData.minimumWidth = 100;
			chatBoxHolderData.minimumHeight = 50;
			chatBoxHolderData.widthHint = 200;
			chatBoxHolderData.heightHint = 100;
			chatBoxHolder.setLayoutData(chatBoxHolderData);
			userInputBox = new Text(chatBoxHolder, SWT.NONE);
			GridData userInputBoxLData = new GridData(SWT.FILL, SWT.FILL, true, true);
			userInputBoxLData.widthHint = 150;
			userInputBoxLData.heightHint = 30;
			userInputBoxLData.minimumHeight = 15;
			userInputBoxLData.minimumWidth = 50;
			userInputBox.setLayoutData(userInputBoxLData);
			userInputBox.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// User pressed enter key
					String message = userInputBox.getText();
					if (message.length() > 0) {
						try {
							user.getChatContext().sendMessage(message);
						} catch (RemoteException ex) {
							getClientCore().handleRemoteException(ex);
						} catch (IllegalActionException ex) {
							logger.error("This should not happen", ex);
						}
					}
				}
			});
			gameInfoText = new StyledText(chatBoxHolder, SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
			GridData gameInfoTextData = new GridData(SWT.FILL, SWT.FILL, true, true);
			gameInfoTextData.widthHint = 200;
			gameInfoTextData.heightHint = 150;
			gameInfoTextData.minimumHeight = 20;
			gameInfoTextData.minimumWidth = 100;
			gameInfoText.setLayoutData(gameInfoTextData);
		}
		{
			gameActionGroup = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
			GridLayout group2Layout = new GridLayout(1, true);
			gameActionGroup.setLayout(group2Layout);
			GridData gameActionGroupLData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
			gameActionGroupLData.heightHint = 150;
			gameActionGroupLData.widthHint = 300;
			gameActionGroupLData.minimumHeight = 80;
			gameActionGroupLData.minimumWidth = 200;
			gameActionGroup.setLayoutData(gameActionGroupLData);
			gameActionGroup.setVisible(false);
			{
				manualEnterBetGroup = new Composite(gameActionGroup, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
				GridLayout manualEnterBetGroupLayout = new GridLayout(3, false);
				manualEnterBetGroup.setLayout(manualEnterBetGroupLayout);
				GridData manualEnterBetGroupLData = new GridData(SWT.FILL, SWT.CENTER, true, true);
				manualEnterBetGroupLData.minimumHeight = 40;
				manualEnterBetGroupLData.minimumWidth = gameActionGroupLData.minimumWidth;
				manualEnterBetGroup.setLayoutData(manualEnterBetGroupLData);
				{
					betSlider = new Slider(manualEnterBetGroup, SWT.NONE);
					betSlider.setIncrement(gameState.getTableConfiguration().getSmallBlind());
					betSlider.setPageIncrement(betSlider.getIncrement() * 5);
					betSlider.setLayoutData(new GridData(100, 20));
					betSlider.addSelectionListener(new SelectionAdapter() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							setNewBetRaiseAmount(betSlider.getSelection() - gameState.getMaxBet());
						}
					});
				}
				{
					potButton = new Button(manualEnterBetGroup, SWT.PUSH | SWT.CENTER);
					potButton.setText("Pot");
					potButton.setLayoutData(new GridData(30, 30));
					potButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							setNewBetRaiseAmount(gameState.getPotRaiseAmount(user.getId()));
						}
					});
				}
				{
					betAmountTextField = new Text(manualEnterBetGroup, SWT.CENTER | SWT.BORDER);
					betAmountTextField.setLayoutData(new GridData(30, 20));
					betAmountTextField.setText(ClientGUI.formatBet(0));
					betAmountTextField.addKeyListener(new KeyAdapter() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							betAmountTextField.setToolTipText("Minimum is "
									+ ClientGUI.formatBet(gameState.getMinBetRaiseAmount(user.getId())
											+ gameState.getMaxBet()));
							try {
								int desiredAmount = ClientGUI.parseBet(betAmountTextField.getText());
								
								if (desiredAmount - gameState.getToCall(user.getId()) >= gameState
										.getMinBetRaiseAmount(user.getId())) {
									setNewBetRaiseAmount(desiredAmount - gameState.getToCall(user.getId()));
								}
							} catch (Exception ex) {
								logger.error("Could not parse manual bet input", ex);
								return;
							}
							
						}
					});
				}
			}
			{
				foldCallRaiseButtonGroup = new Composite(gameActionGroup, SWT.NONE);
				FillLayout foldCallRaiseLayout = new FillLayout(SWT.HORIZONTAL);
				foldCallRaiseLayout.spacing = 5;
				foldCallRaiseButtonGroup.setLayout(foldCallRaiseLayout);
				GridData foldCallRaiseLData = new GridData(SWT.FILL, SWT.CENTER, true, true);
				foldCallRaiseLData.minimumHeight = 30;
				foldCallRaiseLData.minimumWidth = 200;
				foldCallRaiseLData.heightHint = 40;
				foldCallRaiseLData.widthHint = 250;
				foldCallRaiseButtonGroup.setLayoutData(foldCallRaiseLData);
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
				GridData generalActionHolderLData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
				generalActionHolderLData.heightHint = 120;
				generalActionHolderLData.widthHint = 80;
				generalActionHolder = new Composite(this, SWT.NONE | ClientGUI.COMPOSITE_BORDER_STYLE);
				FillLayout generalActionHolderLayout = new FillLayout(SWT.VERTICAL);
				generalActionHolderLayout.spacing = 5;
				generalActionHolder.setLayout(generalActionHolderLayout);
				generalActionHolder.setLayoutData(generalActionHolderLData);
				generalActionHolder.setVisible(false);
				{
					sitInOutButton = new Button(generalActionHolder, SWT.TOGGLE | SWT.CENTER);
					sitInOutButton.setText("Sit In");
					sitInOutButton.addMouseListener(new MouseAdapter() {
						
						@Override
						public void mouseDown(MouseEvent evt) {
							sitInOutButtonMouseDown(evt);
						}
					});
				}
				{
					leaveButton = new Button(generalActionHolder, SWT.PUSH | SWT.CENTER);
					leaveButton.setText("Leave Table");
					leaveButton.addSelectionListener(new SelectionAdapter() {
						
						@Override
						public void widgetSelected(SelectionEvent evt) {
							leaveButtonWidgetSelected(evt);
						}
					});
				}
				{
					rebuyButton = new Button(generalActionHolder, SWT.PUSH | SWT.CENTER);
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
		logger.debug("Leave button pressed");
		try {
			user.getPlayerContext().sitOut();
			user.getTableContext().leaveTable();
		} catch (RemoteException e) {
			getClientCore().handleRemoteException(e);
		} catch (IllegalActionException e) {
			logger.error("This should not happen ", e);
		}
		getClientCore().getGui().getGameWindows().remove(getParent());
		getShell().close();
	}
	
	void prepareForUserInput() {
		logger.info("Users turn");
		boolean toCallAllIn = getUser().getStackValue() <= gameState.getToCall(user.getId());
		updateCheckCallButton(toCallAllIn);
		if (!toCallAllIn) {
			setNewBetRaiseAmount(gameState.getMinBetRaiseAmount(user.getId()));
			betAmountTextField.selectAll();
			betAmountTextField.setFocus();
		}
		gameActionGroup.setVisible(true);
		foldCallRaiseButtonGroup.setVisible(true);
		betRaiseButton.setVisible(!toCallAllIn);
		manualEnterBetGroup.setVisible(!toCallAllIn);
		gameActionGroup.layout(true);
	}
	
	private void updateCheckCallButton(boolean allIn) {
		String amountAsString = ClientGUI.formatBet(Math.min(getUser().getStackValue(), gameState.getToCall(user
				.getId())));
		String text = (gameState.getToCall(user.getId()) == 0) ? "Check" : "Call " + amountAsString;
		checkCallButton.setText(text);
		if (allIn) {
			checkCallButton.setText("All In (" + amountAsString + ")");
		}
		gameActionGroup.layout(true);
	}
	
	/**
	 * Create new BuyinDialog with a maximum rebuy of 100 big blinds
	 */
	void rebuyButtonWidgetSelected(SelectionEvent evt) {
		try {
			new BuyinDialog(getClientCore(), getClientCore().getCommunication().getCashierContext(), 100 * gameState
					.getTableConfiguration().getBigBlind(), false).open();
		} catch (RemoteException e) {
			getClientCore().handleRemoteException(e);
		}
	}
	
	private void setNewBetRaiseAmount(int amount) {
		betRaiseAmount = amount;
		updateBetSlider();
		updateBetRaiseButton();
		if (!betAmountTextField.isFocusControl()) {
			betAmountTextField.setText(ClientGUI.formatBet(betRaiseAmount + gameState.getMaxBet()));
		}
	}
	
	void sitInOutButtonMouseDown(MouseEvent evt) {
		logger.debug("sitInOutButton.widgetSelected, event=" + evt);
		
		try {
			if (!sitInOutButton.getSelection()) {
				user.sitIn(getUser().getSeatId(), getUser().getStackValue());
				sitInOutButton.setText("Sit Out");
			} else {
				user.getPlayerContext().sitOut();
				sitInOutButton.setText("Sit In");
			}
		} catch (RemoteException e) {
			getClientCore().handleRemoteException(e);
		} catch (IllegalActionException e) {
			logger.error("This should not happen", e);
		}
	}
	
	void updateBetSlider() {
		betSlider.setMaximum(getUser().getStackValue() + getUser().getBetChipsValue());
		// +10 is some weirdo behavior/bug??
		// native windows bug fix;
		betSlider.setSelection(getUser().getStackValue() + getUser().getBetChipsValue());
		int extras = betSlider.getMaximum() - betSlider.getSelection();
		if (extras != 0) {
			betSlider.setMaximum(betSlider.getMaximum() + extras);
		}
		betSlider.setMinimum(gameState.getMinBetRaiseAmount(user.getId()) + gameState.getMaxBet());
		betSlider.setSelection(betRaiseAmount + gameState.getMaxBet());
	}
	
	void updateBetRaiseButton() {
		int totalBetRaiseAmount = betRaiseAmount + gameState.getMaxBet();
		boolean isAllIn = (gameState.getToCall(user.getId()) + betRaiseAmount == getUser().getStackValue());
		String amountAsString = ClientGUI.formatBet(totalBetRaiseAmount);
		String text = (gameState.getToCall(user.getId()) != 0) ? "Raise to " : "Bet ";
		betRaiseButton.setText(text + amountAsString);
		if (isAllIn) {
			betRaiseButton.setText("All In (" + amountAsString + ")");
		}
		gameActionGroup.layout(true);
	}
	
	/**
	 * @return The Composite holding the Controls which are to be displayed only
	 *         when it is the user's turn to act.
	 */
	public Composite getGameActionGroup() {
		return gameActionGroup;
	}
	
	/**
	 * Adds the message to the Chat Box, color-coded according to type
	 * <p>
	 * Standard dealer msg: black
	 * <p>
	 * Player msg: blue
	 * <p>
	 * Server msg: red
	 * 
	 * @see org.cspoker.common.api.chat.listener.ChatListener#onMessage(org.cspoker.common.api.chat.event.MessageEvent)
	 */
	@Override
	public void onMessage(MessageEvent messageEvent) {
		// Adjust color based on who sent the message
		Color color = getDisplay().getSystemColor(SWT.COLOR_BLUE);
		String playerName = messageEvent.getPlayer().getName();
		if (playerName.equals("Dealer")) {
			color = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		} else if (playerName.equals("Server")) {
			color = getDisplay().getSystemColor(SWT.COLOR_RED);
		}
		if (!messageEvent.getPlayer().getName().equals("Dealer")) {
			color = getDisplay().getSystemColor(SWT.COLOR_BLUE);
		}
		// Display player messages in blue
		int start = gameInfoText.getCharCount();
		gameInfoText.append(System.getProperty("line.separator") + messageEvent.getPlayer() + ": "
				+ messageEvent.getMessage());
		int end = gameInfoText.getCharCount();
		
		gameInfoText.setTopIndex(gameInfoText.getLineCount() - 5);
		gameInfoText.update();
		gameInfoText.setStyleRange(new StyleRange(start, end - start, color, Display.getDefault().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND)));
		
	}
	
	/**
	 * Helper method to display "Dealer" messages
	 * 
	 * @param event The {@link HoldemTableEvent} to generate and display a
	 *            dealer message for
	 */
	public void showDealerMessage(HoldemTableEvent event) {
		onMessage(new MessageEvent(new Player(new PlayerId(-1), "Dealer"), event.toString()));
	}
	
	public SeatedPlayer getUser() {
		return gameState.getSnapshot(user.getId());
	}
}
