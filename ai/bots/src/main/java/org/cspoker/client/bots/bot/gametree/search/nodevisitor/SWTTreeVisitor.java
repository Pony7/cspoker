/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.bots.bot.gametree.search.nodevisitor;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.gametree.action.ActionWrapper;
import org.cspoker.client.bots.bot.gametree.action.ProbabilityAction;
import org.cspoker.client.bots.bot.gametree.search.Distribution;
import org.cspoker.client.bots.bot.gametree.search.GameTreeNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class SWTTreeVisitor implements NodeVisitor {

	private final static Logger logger = Logger.getLogger(SWTTreeVisitor.class);
	private final Display display;
	private final Tree tree;
	private final int relStackSize;
	private final AtomicBoolean newDecision = new AtomicBoolean(true);
	private final PlayerId botId;

	public SWTTreeVisitor(Display display, Shell shell, final Tree tree,
			final GameState gameState, PlayerId botId) {
		this.display = display;
		this.tree = tree;
		this.relStackSize = gameState.getPlayer(botId).getStack();
		this.botId = botId;
		display.syncExec(new Runnable() {
			public void run() {
				items  = new LinkedList<TreeItem>();
				rounds = new LinkedList<Round>();
				rounds.push(gameState.getRound());
			}
		});
	}

	private LinkedList<TreeItem> items;
	private LinkedList<Round> rounds;

	@Override
	public void enterNode(final Pair<ActionWrapper,GameTreeNode> pair, final double lowerBound) {
		display.syncExec(new Runnable() {
			public void run() {
				ActionWrapper action = pair.getLeft();
				TreeItem item = items.peek();
				TreeItem newItem;
				if (item == null) {
					if (newDecision.compareAndSet(true, false)) {
						tree.removeAll();
					}
					newItem = new TreeItem(tree, SWT.NONE);
				} else {
					newItem = new TreeItem(item, SWT.NONE);
				}
				ProbabilityAction probAction;
				String samples = "?";
				if (action instanceof ProbabilityAction) {
					probAction = (ProbabilityAction) action;
				} else {
					throw new IllegalStateException("What action is this? "
							+ action);
				}
				GameTreeNode node = pair.getRight();
				Round round = rounds.peek();
				rounds.push(node.getGameState().getRound());
				String actor = action.getAction().actor.equals(botId)?"Bot":"Player "+action.getAction().actor;
				newItem.setText(new String[] { actor,
						action.getAction().toString(), round.getName(),
						Math.round(100 * probAction.getProbability()) + "%",
						samples, "?", "?", "" + node.getNbTokens() , 
						""+Util.parseDollars(node.getUpperWinBound() - relStackSize),
						""+Util.parseDollars(node.getGameState().getGamePotSize()),
						""+Util.parseDollars(lowerBound - relStackSize)
				});
				if (round == Round.FINAL) {
					newItem.setBackground(2, new Color(display, 30, 30, 255));
				} else if (round == Round.TURN) {
					newItem.setBackground(2, new Color(display, 100, 100, 255));
					;
				} else if (round == Round.FLOP) {
					newItem.setBackground(2, new Color(display, 170, 170, 255));
				} else if (round == Round.PREFLOP) {
					newItem.setBackground(2, new Color(display, 240, 240, 255));
				}
				items.push(newItem);
			}
		});
	}

	@Override
	public void leaveNode(final Pair<ActionWrapper,GameTreeNode> pair, final Distribution distribution) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					TreeItem item = items.pop();
					if(distribution.isUpperBound()){
						item.setText(5, "<"+Util.parseDollars((int) Math
								.round(distribution.getMean() - relStackSize)));
						item.setText(6, "");
						item.setBackground(5, new Color(display, 255, 0, 0));
					}else{
						item.setText(5, Util.parseDollars(distribution.getMean() - relStackSize));
						item.setText(6, Util.parseDollars(Math.sqrt(distribution.getVariance())));
					}
					rounds.pop();
				} catch (NoSuchElementException e) {
					tree.redraw();
				}
			}
		});
	}

	@Override
	public void pruneSubTree(Pair<ActionWrapper, GameTreeNode> node,
			Distribution distribution, double lowerBound) {
		enterNode(node, lowerBound);
		leaveNode(node, distribution);
	}

	@Override
	public void callOpponentModel() {
		// no op
	}

	@Override
	public void visitLeafNode(final int winnings, final double probability,
			final int minWinnable, final int maxWinnable) {
		final String text;
		if (winnings == minWinnable) {
			text = "Lose";
		} else if (winnings == maxWinnable) {
			text = "Win";
		} else {
			text = "Split Pot";
		}
		if(winnings<minWinnable || winnings>maxWinnable){
			throw new IllegalArgumentException(winnings +" in "+ minWinnable +" to " + maxWinnable);
		}
		final double winPercentage = (winnings - minWinnable) / (double) (maxWinnable - minWinnable);
		display.syncExec(new Runnable() {
			public void run() {
				TreeItem item = items.peek();
				TreeItem newItem;
				if (item == null) {
					newItem = new TreeItem(tree, SWT.NONE);
				} else {
					newItem = new TreeItem(item, SWT.NONE);
				}
				newItem.setText(new String[] { "", text, "showdown",
						Math.round(100 * probability) + "%", "",
						Util.parseDollars(winnings - relStackSize),
						"$0", "" , "", "", ""});
				try {
					newItem.setBackground(1, new Color(display, (int) Math
							.round((1 - winPercentage) * 255), (int) Math
							.round(winPercentage * 255), 00));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println((int) Math
						.round((1 - winPercentage) * 255)+", "+ (int) Math
						.round(winPercentage * 255)+ ", "+ 00);
					System.out.println(winnings +" in "+ minWinnable +" to " + maxWinnable);
				}
			}
		});
	}

	public static class Factory implements NodeVisitor.Factory {

		private final Display display;
		private Shell shell;
		private Tree tree;

		public Factory(final Display display) {
			this.display = display;
			display.syncExec(new Runnable() {

				public void run() {
					shell = new Shell(display);
					shell.addShellListener(new ShellAdapter() {
						@Override
						public void shellClosed(ShellEvent e) {
							e.doit = false;
						}
					});
					shell.setSize(600, 400);
					shell.setMinimumSize(500, 400);
					shell.setLayout(new FillLayout());
					shell.setText("Game Tree Browser");
					tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL
							| SWT.V_SCROLL);
					tree.setHeaderVisible(true);

					TreeColumn column = new TreeColumn(tree, SWT.LEFT);
					column.setText("Actor");
					column.setWidth(210);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Action");
					column.setWidth(120);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Round");
					column.setWidth(70);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("P(Action)");
					column.setWidth(70);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Samples");
					column.setWidth(70);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("E(V)");
					column.setWidth(80);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("σ(V)");
					column.setWidth(80);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Tokens");
					column.setWidth(50);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Max");
					column.setWidth(80);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Pot");
					column.setWidth(80);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("LowerBound");
					column.setWidth(80);

					shell.pack();
					shell.open();
				}

			});
		}

		@Override
		public NodeVisitor create(GameState gameState, PlayerId actor) {
			SWTTreeVisitor visitor = new SWTTreeVisitor(display, shell, tree, gameState, actor);
			return visitor;
		}

	}

}
