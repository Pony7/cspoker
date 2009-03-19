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
package org.cspoker.client.bots.bot.search.node.visitor;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.SampledAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.node.ActionNode;
import org.cspoker.client.bots.bot.search.node.BotActionNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
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

public class SWTTreeVisitor implements NodeVisitor{

	private final static Logger logger = Logger.getLogger(SWTTreeVisitor.class);
	private final Display display;
	private final Shell shell;
	private final Tree tree;
	private final int relStackSize;
	private final AtomicBoolean newDecision = new AtomicBoolean(true);

	public SWTTreeVisitor(Display display, Shell shell, final Tree tree, int relStackSize) {
		this.display = display;
		this.shell = shell;
		this.tree = tree;
		this.relStackSize = relStackSize;

	}

	private final LinkedList<TreeItem> items = new LinkedList<TreeItem>();

	@Override
	public void enterNode(final ActionNode node, final ActionWrapper action, final int tokens) {
		display.syncExec(new Runnable(){
			public void run(){
				TreeItem item = items.peek();
				TreeItem newItem;
				if(item==null){
					if(newDecision.compareAndSet(true, false)){
						tree.removeAll();
					}
					newItem = new TreeItem(tree, SWT.NONE);
				}else{
					newItem = new TreeItem(item, SWT.NONE);
				}
				ProbabilityAction probAction;
				String samples = "n/a";
				if(action instanceof SampledAction){
					SampledAction sampledAction = (SampledAction)action;
					probAction = sampledAction.getProbabilityAction();
					samples = sampledAction.getTimes()+"/"+sampledAction.getOutof();
				}else if(action instanceof ProbabilityAction){
					probAction = ((ProbabilityAction)action);
				}else{
					throw new IllegalStateException("What action is this? "+action);
				}
				String actor = (node instanceof BotActionNode) ? "Bot": "Player "+node.getPlayerId();
				Round round = node.getGameState().getRound();
				newItem.setText(new String[] { 
						actor, 
						action.getAction().toString(), 
						round.getName(),
						Math.round(100*probAction.getProbability())+"%" , 
						samples,
						"?", 
						"?", 
						""+tokens 
				});
				if(round==Round.FINAL){
					newItem.setBackground(2,new Color(display, 30,30,255));
				}else if(round==Round.TURN){
					newItem.setBackground(2,new Color(display, 100,100,255));;	
				}else if(round==Round.FLOP){
					newItem.setBackground(2,new Color(display, 170,170,255));
				}else if(round==Round.PREFLOP){
					newItem.setBackground(2,new Color(display, 240,240,255));
				}
				items.push(newItem);
			}
		});
	}



	@Override
	public void leaveNode(final EvaluatedAction<? extends ActionWrapper> evaluation) {
		display.syncExec(new Runnable(){
			@Override
			public void run() {
				try {
					TreeItem item = items.pop();
					item.setText(5, SearchBotAction.parseDollars((int)Math.round(evaluation.getEV()-relStackSize)));
					item.setText(6, SearchBotAction.parseDollars((int)Math.round(Math.sqrt(evaluation.getVarEV()))));
				} catch (NoSuchElementException e) {
					tree.redraw();
				}
			}
		});
	}

	@Override
	public void visitLoseNode(final int ev, final double p) {
		display.syncExec(new Runnable(){
			public void run(){
				TreeItem item = items.peek();
				TreeItem newItem;
				if(item==null){
					newItem = new TreeItem(tree, SWT.NONE);
				}else{
					newItem = new TreeItem(item, SWT.NONE);
				}
				newItem.setText(new String[] { 
						"", 
						"Lose", 
						"showdown",
						Math.round(100*p)+"%" , 
						"",
						SearchBotAction.parseDollars(ev-relStackSize),
						"$0", 
						"" 
				});
				newItem.setBackground(1,new Color(display, 255,00,00));
			}
		});
	}

	@Override
	public void visitWinNode(final int ev, final double p) {
		display.syncExec(new Runnable(){
			public void run(){
				TreeItem item = items.peek();
				TreeItem newItem;
				if(item==null){
					newItem = new TreeItem(tree, SWT.NONE);
				}else{
					newItem = new TreeItem(item, SWT.NONE);
				}
				newItem.setText(new String[] { 
						"", 
						"Win", 
						"showdown",
						Math.round(100*p)+"%" , 
						"",
						SearchBotAction.parseDollars(ev-relStackSize),
						"$0", 
						"" 
				});
				newItem.setBackground(1,new Color(display, 00,255,00));
			}
		});
	}



	public static class Factory implements NodeVisitor.Factory{

		private final Display display;
		private Shell shell;
		private Tree tree;

		public Factory(final Display display) {
			this.display = display;
			display.syncExec(new Runnable(){

				public void run(){
					shell = new Shell(display);
					shell.addShellListener(new ShellAdapter() {
						public void shellClosed(ShellEvent e) {
							e.doit = false;
						}
					});
					shell.setSize(600, 400);
					shell.setMinimumSize(500, 400);
					shell.setLayout(new FillLayout());
					shell.setText("Game Tree Browser");
					tree = new Tree(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
					tree.setHeaderVisible(true);

					TreeColumn column = new TreeColumn(tree, SWT.LEFT);
					column.setText("Actor");
					column.setWidth(200);

					column = new TreeColumn(tree, SWT.CENTER);
					column.setText("Action");
					column.setWidth(100);

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

					shell.pack();
					shell.open();
				}

			});
		}

		@Override
		public NodeVisitor create(GameState gameState, PlayerId actor) {
			SWTTreeVisitor visitor = new SWTTreeVisitor(display, shell, tree, gameState.getPlayer(actor).getStack());
			return visitor;
		}

	}

}

