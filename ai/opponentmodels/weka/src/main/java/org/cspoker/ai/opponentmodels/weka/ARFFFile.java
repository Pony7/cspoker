package org.cspoker.ai.opponentmodels.weka;

import java.io.*;

import org.cspoker.ai.opponentmodels.weka.instances.InstancesBuilder;

import weka.core.Instance;

public class ARFFFile {
	
	private final String nl = InstancesBuilder.nl;	
	private final String folder = "org/cspoker/ai/opponentmodels/weka/models/"; 
	private final String path;
	
	private Writer preCheckBetFile;
	private Writer postCheckBetFile;	
	private Writer preFoldCallRaiseFile;
	private Writer postFoldCallRaiseFile;
	private Writer showdownFile;
	
	public ARFFFile(Object player, boolean overwrite) throws IOException {
		this.path = (getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath() + folder).replace("%20", " ");
		
		boolean preCheckBetExists = prepareFile("PreCheckBet.arff", player);
		preCheckBetFile = new BufferedWriter(new FileWriter(path + player
				+ "PreCheckBet.arff", !overwrite));
		boolean postCheckBetExists = prepareFile("PostCheckBet.arff", player);
		postCheckBetFile = new BufferedWriter(new FileWriter(path + player
				+ "PostCheckBet.arff", !overwrite));
		boolean preFoldCallRaiseExists = prepareFile("PreFoldCallRaise.arff", player);
		preFoldCallRaiseFile = new BufferedWriter(new FileWriter(path + player
				+ "PreFoldCallRaise.arff", !overwrite));
		boolean postFoldCallRaiseExists = prepareFile("PostFoldCallRaise.arff", player);
		postFoldCallRaiseFile = new BufferedWriter(new FileWriter(path + player
				+ "PostFoldCallRaise.arff", !overwrite));
		boolean showdownExists = prepareFile("Showdown.arff", player);
		showdownFile = new BufferedWriter(new FileWriter(path + player
				+ "Showdown.arff", !overwrite));

		if (overwrite || !preCheckBetExists)
			preCheckBetFile.write(ARFFPropositionalizer.getPreCheckBetInstance().toString());
		if (overwrite || !postCheckBetExists)
			postCheckBetFile.write(ARFFPropositionalizer.getPostCheckBetInstance().toString());
		if (overwrite || !preFoldCallRaiseExists)
			preFoldCallRaiseFile.write(ARFFPropositionalizer.getPreFoldCallRaiseInstance().toString());
		if (overwrite || !postFoldCallRaiseExists)
			postFoldCallRaiseFile.write(ARFFPropositionalizer.getPostFoldCallRaiseInstance().toString());
		if (overwrite || !showdownExists)
			showdownFile.write(ARFFPropositionalizer.getShowdownInstance().toString());
	}

	private boolean prepareFile(String fileName, Object player) throws FileNotFoundException {
		return new File(path + player + fileName).exists();
	}

	public void close() throws IOException {
		preCheckBetFile.close();
		postCheckBetFile.close();
		preFoldCallRaiseFile.close();
		postFoldCallRaiseFile.close();
		showdownFile.close();
	}

	public void writePreCheckBet(Instance instance) {
		try {
			preCheckBetFile.write(instance.toString()+nl);
			preCheckBetFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	public void writePostCheckBet(Instance instance) {
		try {
			postCheckBetFile.write(instance.toString()+nl);
			postCheckBetFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	public void writePreFoldCallRaise(Instance instance) {
		try {
			preFoldCallRaiseFile.write(instance.toString()+nl);
			preFoldCallRaiseFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	public void writePostFoldCallRaise(Instance instance) {
		try {
			postFoldCallRaiseFile.write(instance.toString()+nl);
			postFoldCallRaiseFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	public void writeShowdown(Instance instance) {
		try {
			showdownFile.write(instance.toString()+nl);
			showdownFile.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		} catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}
}
