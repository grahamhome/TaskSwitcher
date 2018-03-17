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
	private static String trialHeaders = "Participant Number,Predictable(1)/Random(2),Practice(1)/Experimental(2),Trial Number (per block),Correct(1)/Incorrect(2),Response Time (ms),Character Pair,Position (1=UL; 2=UR; 3=LR; 4=LL),Non-Switch(1)/Switch(2),Congruent(1)/Incongruent(2),Key Pressed,Include In Statistics(1)/Exclude From Statistics(2)" + System.lineSeparator();
	private static String blocksTitle = "Block Analyses:";
	public static void report() {
		SubTask task = TaskData.first;
		BlockType currentBlockType = null;
		BlockResults blockResults = new BlockResults();
		StringBuilder trialResultsString = new StringBuilder(trialHeaders);
		StringBuilder blockResultsString = new StringBuilder(blocksTitle);
		while (!(task instanceof End)) {
			if (task instanceof Trial) {
				Trial trial = (Trial)task;
				trialResultsString.append(trial.toString());
				if (currentBlockType == null) {
					currentBlockType = trial.type;
				} else if (!currentBlockType.equals(trial.type)) {
					blockResultsString.append(System.lineSeparator());
					blockResultsString.append(blockResults.report());
					blockResults = new BlockResults();
					currentBlockType = trial.type;
				}
				blockResults.add(trial);
			}
			task = task.next;
		}
		blockResultsString.append(System.lineSeparator());
		blockResultsString.append(blockResults.report());
		try {
			PrintWriter outputWriter = new PrintWriter(StartScreen.subjectNumber + ".csv", "UTF-8");
				outputWriter.write(trialResultsString.toString());
				outputWriter.write(System.lineSeparator());
				outputWriter.write(System.lineSeparator());
				outputWriter.write(blockResultsString.toString());
			outputWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stores the trials of a single block and generates the results of the block.
	 */
	private static class BlockResults {
		private float total = 0;
		private float notCounted = 0;
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
			total++;
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
			} else {
				notCounted++;
			}
		}
		
		public String report() {
			StringBuilder results = new StringBuilder();
			results.append(trials.get(0).type).append(" block:").append(System.lineSeparator());
			results.append("average response time:").append(System.lineSeparator());
			results.append("switch trials,non-switch trials,congruent trials,incongruent trials").append(System.lineSeparator());
			results.append(totalSwitch>0 ? totalResponseTimeSwitch/totalSwitch : "0").append(",");
			results.append(totalNonSwitch>0 ? totalResponseTimeNonSwitch/totalNonSwitch : "0").append(",");
			results.append(totalCongruent>0 ? totalResponseTimeCongruent/totalCongruent : "0").append(",");
			results.append(totalIncongruent>0 ? totalResponseTimeIncongruent/totalIncongruent : "0").append(System.lineSeparator());
			results.append("total number of errors:").append(System.lineSeparator());
			results.append("switch trials,non-switch trials,congruent trials,incongruent trials").append(System.lineSeparator());
			results.append(errorsSwitch).append(",");
			results.append(errorsNonSwitch).append(",");
			results.append(errorsCongruent).append(",");
			results.append(errorsIncongruent).append(System.lineSeparator());
			results.append("switch cost:").append(System.lineSeparator());
			results.append(totalResponseTimeSwitch-totalResponseTimeNonSwitch).append(System.lineSeparator());
			results.append("proportional switch cost:").append(System.lineSeparator());
			results.append(((totalResponseTimeSwitch-totalResponseTimeNonSwitch)/totalResponseTimeNonSwitch)*100).append(System.lineSeparator());
			results.append("percentage of trials removed:").append(System.lineSeparator());
			results.append((notCounted/total)*100).append(System.lineSeparator());
			return results.toString();
		}
	}
}
