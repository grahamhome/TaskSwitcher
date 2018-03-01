package code;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import code.TaskData.BlockType;
import code.TaskData.End;
import code.TaskData.SubTask;
import code.TaskData.Trial;
import code.TaskData.Trial.Result;
import javafx.scene.input.KeyCode;

/**
 * Writes the results of an experiment to a file.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class ResultsReporter {
	public static void report() {
		SubTask task = TaskData.first;
		BlockType currentBlockType = null;
		BlockResults blockResults = new BlockResults();
		StringBuilder results = new StringBuilder();
		while (!(task instanceof End)) {
			if (task instanceof Trial) {
				Trial trial = (Trial)task;
				if (currentBlockType == null) {
					currentBlockType = trial.type;
				} else if (!currentBlockType.equals(trial.type)) {
					if (results.length() > 0) {
						results.append(System.lineSeparator());
					}
					results.append(blockResults.report());
					blockResults = new BlockResults();
					currentBlockType = trial.type;
				}
				blockResults.add(trial);
			}
			task = task.next;
		}
		if (results.length() > 0) {
			results.append(System.lineSeparator());
		}
		results.append(blockResults.report());
		try {
			PrintWriter outputWriter = new PrintWriter(StartScreen.subjectNumber + ".csv", "UTF-8");
				outputWriter.write(results.toString());
			outputWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stores the trials of a single block and generates the results of the block.
	 */
	private static class BlockResults {
		private int totalSwitch = 0;
		private int totalNonSwitch = 0;
		private int totalCongruent = 0;
		private int totalIncongruent = 0;
		private double totalResponseTimeSwitch = 0;
		private double totalResponseTimeNonSwitch = 0;
		private double totalResponseTimeCongruent = 0;
		private double totalResponseTimeIncongruent = 0;
		private int errorsSwitch = 0;
		private int errorsNonSwitch = 0;
		private int errorsCongruent = 0;
		private int errorsIncongruent = 0;
		private ArrayList<Trial> trials = new ArrayList<>();
		
		public void add(Trial trial) {
			trials.add(trial);
			if (trial.counted) {
				if (trial.switching) {
					if (trial.time >= 0) {
						totalSwitch++;
						totalResponseTimeSwitch += (trial.time);
					}
					if (!trial.result.equals(Result.CORRECT)) {
						errorsSwitch++;
					}
				} else {
					if (trial.time >= 0) {
						totalNonSwitch++;
						totalResponseTimeNonSwitch += (trial.time);
					}
					if (!trial.result.equals(Result.CORRECT)) {
						errorsNonSwitch++;
					}
				}
				if (trial.congruent) {
					if (trial.time >= 0) {
						totalCongruent++;
						totalResponseTimeCongruent += (trial.time);
					}
					if (!trial.result.equals(Result.CORRECT)) {
						errorsCongruent++;
					}
				} else {
					if (trial.time >= 0) {
						totalIncongruent++;
						totalResponseTimeIncongruent += (trial.time);
					}
					if (!trial.result.equals(Result.CORRECT)) {
						errorsIncongruent++;
					}
				}
			}
		}
		
		public String report() {
			StringBuilder results = new StringBuilder();
			results.append(trials.get(0).type).append(" block").append(System.lineSeparator());
			results.append("block statistics:").append(System.lineSeparator());
			results.append("average response time:").append(System.lineSeparator());
			results.append("switch trials,non-switch trials,congruent trials,incongruent trials").append(System.lineSeparator());
			results.append(totalSwitch>0 ? Math.round(totalResponseTimeSwitch/totalSwitch) : "0.000").append(",");
			results.append(totalNonSwitch>0 ? Math.round(totalResponseTimeNonSwitch/totalNonSwitch) : "0.000").append(",");
			results.append(totalCongruent>0 ? Math.round(totalResponseTimeCongruent/totalCongruent) : "0.000").append(",");
			results.append(totalIncongruent>0 ? Math.round(totalResponseTimeIncongruent/totalIncongruent) : "0.000").append(System.lineSeparator());
			results.append("total number of errors:").append(System.lineSeparator());
			results.append("switch trials,non-switch trials,congruent trials,incongruent trials").append(System.lineSeparator());
			results.append(errorsSwitch).append(",");
			results.append(errorsNonSwitch).append(",");
			results.append(errorsCongruent).append(",");
			results.append(errorsIncongruent).append(System.lineSeparator());
			results.append("trials in ").append(trials.get(0).type).append(" block:").append(System.lineSeparator());
			results.append("character pair,position,congruent/incongruent,key pressed,correct/incorrect,response time (ms),included in statistics").append(System.lineSeparator());
			for (Trial trial : trials) {
				results.append(trial.letter).append(trial.number).append(",")
				.append(trial.position).append(",")
				.append((trial.congruent ? "congruent" : "incongruent")).append(",")
				.append(trial.actualResponse.equals(KeyCode.CANCEL) ? "none" : trial.actualResponse.equals(TaskData.LEFT_KEY) ? TaskData.LEFT_KEY_NAME : TaskData.RIGHT_KEY_NAME).append(",")
				.append(trial.result.equals(Result.CORRECT) ? "yes" : "no").append(",")
				.append(trial.time > 0 ? trial.time : "none").append(",")
				.append(trial.counted ? "yes" : "no").append(System.lineSeparator());
			}
			return results.toString();
		}
	}
}
