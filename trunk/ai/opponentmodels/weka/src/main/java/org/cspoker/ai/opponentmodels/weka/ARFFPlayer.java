package org.cspoker.ai.opponentmodels.weka;

import java.io.*;

import org.apache.log4j.Logger;

import weka.core.Instance;

public class ARFFPlayer {

	private final static Logger logger = Logger.getLogger(ARFFPlayer.class);
	
	private final String folder = "../../src/main/resources/org/cspoker/ai/opponentmodels/weka/models/arff/"; 
	
	private final Object player;
	
	private ARFFFile preCheckBetFile;
	private ARFFFile postCheckBetFile;	
	private ARFFFile preFoldCallRaiseFile;
	private ARFFFile postFoldCallRaiseFile;
	private ARFFFile showdownFile;
	
	private boolean modelCreated = false;
	
	private WekaRegressionModel model = null;
	private WekaOptions config = new WekaOptions();
	
	private long writeCounter = 0;
	
	public ARFFPlayer(Object player, WekaRegressionModel baseModel, WekaOptions config) {
		if (!config.useOnlineLearning()) 
			throw new IllegalStateException("ARFFPlayer can only be used with online learning!");
		
		this.player = player;
		this.config = config;	
		this.model = baseModel;
		
		try {
			String path = (getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath() + folder).replace("%20", " ");
			
			preCheckBetFile = new ARFFFile(path, player, "PreCheckBet.arff",
					ARFFPropositionalizer.getPreCheckBetInstance().toString(), config);
			postCheckBetFile = new ARFFFile(path, player, "PostCheckBet.arff",
					ARFFPropositionalizer.getPostCheckBetInstance().toString(), config);
			preFoldCallRaiseFile = new ARFFFile(path, player, "PreFoldCallRaise.arff", 
					ARFFPropositionalizer.getPreFoldCallRaiseInstance().toString(), config);
			postFoldCallRaiseFile = new ARFFFile(path, player, "PostFoldCallRaise.arff", 
					ARFFPropositionalizer.getPostFoldCallRaiseInstance().toString(), config);
			showdownFile = new ARFFFile(path, player, "Showdown.arff",
					ARFFPropositionalizer.getShowdownInstance().toString(), config);
		} catch (IOException io) {
			throw new RuntimeException(io);
		}
	}

	public void close() throws IOException {
		if (model != null) {
			preCheckBetFile.close();
			postCheckBetFile.close();
			preFoldCallRaiseFile.close();
			postFoldCallRaiseFile.close();
			showdownFile.close();
		}
	}
	
	private String msgModelNotReady(ARFFFile file) {
		return file.getName().substring(0,file.getName().indexOf(".")) + " is not ready to be learned " + 
			"(learning examples: " + file.getNrExamples() + 
			" < " + config.getMinimalLearnExamples() + " required)";
	}
	public void learnNewModel() {
//		if (!(preCheckBetFile.isModelReady() && postCheckBetFile.isModelReady() && preFoldCallRaiseFile.isModelReady() 
//				&& postFoldCallRaiseFile.isModelReady() && showdownFile.isModelReady())) {
//			System.out.println("\n MODEL NOT READY \n");			
//			return;
//		}
		System.out.println("");
		logger.info("Learning new opponentModel for player " + player);
		modelCreated = true;
		
		// learning preCheckBetModel
		if (preCheckBetFile.isModelReady()) learnPreCheckBet();
		else logger.info(msgModelNotReady(preCheckBetFile));
		// learning postCheckBetModel
		if (postCheckBetFile.isModelReady()) learnPostCheckBet();
		else logger.info(msgModelNotReady(postCheckBetFile));
		// learning preFoldCallRaiseModel
		if (preFoldCallRaiseFile.isModelReady()) learnPreFoldCallRaise();
		else logger.info(msgModelNotReady(preFoldCallRaiseFile));
		// learning postFoldCallRaiseModel
		if (postFoldCallRaiseFile.isModelReady()) learnPostFoldCallRaise();
		else logger.info(msgModelNotReady(postFoldCallRaiseFile));
		// learning showdownModel
		if (showdownFile.isModelReady()) learnShowdown();
		else logger.info(msgModelNotReady(showdownFile));
		
		System.out.println("");
	}
	
	public boolean writeAllowed() {
		return !modelCreated || (modelCreated && (config.continuousLearning() || config.continueAfterCreation()));
	}
	
	public boolean learningAllowed() {
		return (modelCreated && config.continuousLearning()) || 
				(!modelCreated && (writeCounter >= config.modelCreationTreshold()));
	}
	
	public boolean modelCreated() {
		return modelCreated;
	}
	
	public void writePreCheckBet(Instance instance) {
		if (writeAllowed()) {
			preCheckBetFile.write(instance);
			incrementWriteCounter();
			if (learningAllowed()) {
				if (modelCreated) 
					learnPreCheckBet();
				else 
					learnNewModel();
			}
		}
	}
	
	public void learnPreCheckBet() {
		try {
			logger.trace("Learning preBetModel for player " + player);
			model.setPreBetModel(preCheckBetFile.createModel("preBet", "betProb", new String[]{"action"}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writePostCheckBet(Instance instance) {
		if (writeAllowed()) {
			postCheckBetFile.write(instance);
			incrementWriteCounter();
			if (learningAllowed()) {
				if (modelCreated) 
					learnPostCheckBet();
				else 
					learnNewModel();
			}
		}
	}
	
	public void learnPostCheckBet() {
		try {
			logger.trace("Learning postBetModel for player " + player);
			model.setPostBetModel(postCheckBetFile.createModel("postBet", "betProb", new String[]{"action"}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writePreFoldCallRaise(Instance instance) {
		if (writeAllowed()) {
			preFoldCallRaiseFile.write(instance);
			incrementWriteCounter();
			if (learningAllowed()) {
				if (modelCreated) 
					learnPreFoldCallRaise();
				else 
					learnNewModel();
			}
		}
	}
	
	public void learnPreFoldCallRaise() {
		try {
			logger.trace("Learning preFoldModel for player " + player);
			model.setPreFoldModel(preFoldCallRaiseFile.createModel("preFold", "foldProb",  new String[]{"callProb", "raiseProb", "action"}));
			logger.trace("Learning preCallModel for player " + player);
			model.setPreCallModel(preFoldCallRaiseFile.createModel("preCall", "callProb",  new String[]{"foldProb", "raiseProb", "action"}));
			logger.trace("Learning preRaiseModel for player " + player);
			model.setPreRaiseModel(preFoldCallRaiseFile.createModel("preRaise", "raiseProb", new String[]{"callProb", "foldProb", "action"}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writePostFoldCallRaise(Instance instance) {
		if (writeAllowed()) {
			postFoldCallRaiseFile.write(instance);
			incrementWriteCounter();
			if (learningAllowed()) {
				if (modelCreated) 
					learnPostFoldCallRaise();
				else 
					learnNewModel();
			}
		}
	}

	public void learnPostFoldCallRaise() {
		try {
			logger.trace("Learning postFoldModel for player " + player);
			model.setPostFoldModel(postFoldCallRaiseFile.createModel("postFold", "foldProb",  new String[]{"callProb", "raiseProb", "action"}));
			logger.trace("Learning postCallModel for player " + player);
			model.setPostCallModel(postFoldCallRaiseFile.createModel("postCall", "callProb",  new String[]{"foldProb", "raiseProb", "action"}));
			logger.trace("Learning postRaiseModel for player " + player);
			model.setPostRaiseModel(postFoldCallRaiseFile.createModel("postRaise", "raiseProb", new String[]{"callProb", "foldProb", "action"}));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void writeShowdown(Instance instance) {
		if (writeAllowed()) {
			showdownFile.write(instance);
			incrementWriteCounter();
			if (learningAllowed()) {
				if (modelCreated) 
					learnShowdown();
				else 
					learnNewModel();
			}
		}
	}
	
	public void learnShowdown() {
		try {
			logger.trace("Learning showdown0Model for player " + player);
			model.setShowdown0Model(showdownFile.createModel("showdown0", "part0Prob", new String[]{"part1Prob", "part2Prob", "part3Prob", "part4Prob", "part5Prob", "avgPartition"}));
			logger.trace("Learning showdown1Model for player " + player);
			model.setShowdown1Model(showdownFile.createModel("showdown1", "part1Prob", new String[]{"part0Prob", "part2Prob", "part3Prob", "part4Prob", "part5Prob", "avgPartition"}));
			logger.trace("Learning showdown2Model for player " + player);
			model.setShowdown2Model(showdownFile.createModel("showdown5", "part2Prob", new String[]{"part0Prob", "part1Prob", "part3Prob", "part4Prob", "part5Prob", "avgPartition"}));
			logger.trace("Learning showdown3Model for player " + player);
			model.setShowdown3Model(showdownFile.createModel("showdown3", "part3Prob", new String[]{"part0Prob", "part1Prob", "part2Prob", "part4Prob", "part5Prob", "avgPartition"}));
			logger.trace("Learning showdown4Model for player " + player);
			model.setShowdown4Model(showdownFile.createModel("showdown4", "part4Prob", new String[]{"part0Prob", "part1Prob", "part2Prob", "part3Prob", "part5Prob", "avgPartition"}));
			logger.trace("Learning showdown5Model for player " + player);
			model.setShowdown5Model(showdownFile.createModel("showdown5", "part5Prob", new String[]{"part0Prob", "part1Prob", "part2Prob", "part3Prob", "part4Prob", "avgPartition"}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void incrementWriteCounter() {
		writeCounter++;
//		System.out.println("=" + writeCounter + "=");
	}
}
