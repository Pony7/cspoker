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
package org.cspoker.client.bots.bot.gametree.opponentmodel.weka;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;

import weka.classifiers.Classifier;

@ThreadSafe
public class WekaRegressionModelFactory implements OpponentModel.Factory {

	private final Classifier preBetModel, preFoldModel, preCallModel, preRaiseModel, postBetModel, postFoldModel, postCallModel, postRaiseModel,
	showdown0Model, showdown1Model, showdown2Model, showdown3Model, showdown4Model, showdown5Model, showdown6Model, showdown7Model, showdown8Model, showdown9Model;

	private final static Logger logger = Logger
	.getLogger(WekaRegressionModelFactory.class);

	public WekaRegressionModelFactory(String modelDirPath) throws IOException, ClassNotFoundException {
		File modelDir = new File(modelDirPath);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preBet.model")));
		preBetModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preFold.model")));
		preFoldModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preCall.model")));
		preCallModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preRaise.model")));
		preRaiseModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postBet.model")));
		postBetModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postFold.model")));
		postFoldModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postCall.model")));
		postCallModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postRaise.model")));
		postRaiseModel = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown0.model")));
		showdown0Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown1.model")));
		showdown1Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown2.model")));
		showdown2Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown3.model")));
		showdown3Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown4.model")));
		showdown4Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown5.model")));
		showdown5Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown6.model")));
		showdown6Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown7.model")));
		showdown7Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown8.model")));
		showdown8Model = (Classifier)in.readObject();
		in.close();
		in = new ObjectInputStream(new FileInputStream(new File(modelDir,"showdown9.model")));
		showdown9Model = (Classifier)in.readObject();
		in.close();
	}

	@Override
	public OpponentModel create() {
		return new WekaRegressionModel(preBetModel, preFoldModel, preCallModel, preRaiseModel, postBetModel, postFoldModel, postCallModel, postRaiseModel,
				showdown0Model, showdown1Model, showdown2Model, showdown3Model, showdown4Model, showdown5Model, showdown6Model, showdown7Model, showdown8Model, showdown9Model);
	}

	@Override
	public String toString() {
		return "WekaRegressionModel";
	}

}
