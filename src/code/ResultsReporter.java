package code;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import code.TaskData.BlockType;
import code.TaskData.End;
import code.TaskData.SubTask;
import code.TaskData.Trial;
import code.TaskData.Trial.Result;

/**
 * Writes the results of an experiment to a file.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class ResultsReporter {
	private static String trialHeaders = "Participant Number,Predictable(1)/Random(2),Practice(1)/Experimental(2),Trial Number (per block),Correct(1)/Incorrect(2),Response Time (ms),Character Pair,Position (1=UL; 2=UR; 3=LR; 4=LL),Non-Switch(1)/Switch(2),Congruent(1)/Incongruent(2),Key Pressed,Include In Error Statistics(1)/Exclude From Error Statistics(2), Include In Response Time Statistics(1)/Exclude from Response Time Statistics(2)" + System.lineSeparator();
	private static String blocksTitle = "Block Analyses:";
	public static void report() {
		SubTask task = TaskData.first;
		if (task == null) { return; }
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
				if (trial.index > 1) { // First trial in each block is excluded from analyses
					blockResults.trials.add(trial);
				}			}
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
		public ArrayList<Trial> trials = new ArrayList<>();
		
		public String report() {
			StringBuilder results = new StringBuilder();
			results.append(trials.get(0).type).append(" block:").append(System.lineSeparator());
			results.append("average response time:").append(System.lineSeparator());
			results.append("switch trials,non-switch trials,congruent trials,incongruent trials").append(System.lineSeparator());
			// Calculate avg response time of switch trials
			float count = trials.stream().filter((t) -> t.useInResponse && t.switching).count();
			AtomicLong totalSwitch = new AtomicLong(0);
			trials.stream().filter((t) -> t.useInResponse && t.switching).forEach((t) -> {totalSwitch.getAndAdd(t.time);});
			float avgSwitchTime = count>0 ? ((float)totalSwitch.get())/count : 0;
			results.append(avgSwitchTime>0 ? String.format("%.3f", avgSwitchTime) : "0.000").append(",");
			// Calculate avg response time of non-switch trials
			count = trials.stream().filter((t) -> t.useInResponse && !t.switching).count();
			AtomicLong totalNonSwitch = new AtomicLong(0);
			AtomicInteger nonSwitchCount = new AtomicInteger(0);
			trials.stream().filter((t) -> t.useInResponse && !t.switching).forEach((t) -> {totalNonSwitch.getAndAdd(t.time); nonSwitchCount.getAndIncrement(); });
			float avgNonSwitchTime = count>0 ? ((float)totalNonSwitch.get())/count : 0;
			results.append(avgNonSwitchTime>0 ? String.format("%.3f", avgNonSwitchTime) : "0.000").append(",");
			// Calculate avg response time of congruent trials
			count = trials.stream().filter((t) -> t.useInResponse && t.congruent).count();
			AtomicLong total = new AtomicLong(0);
			trials.stream().filter((t) -> t.useInResponse && t.congruent).forEach((t) -> {total.getAndAdd(t.time);});
			results.append(count>0 ? String.format("%.3f", ((float)total.get())/count) : "0.000").append(",");
			// Calculate avg response time of congruent trials
			count = trials.stream().filter((t) -> t.useInResponse && !t.congruent).count();
			total.set(0);
			trials.stream().filter((t) -> t.useInResponse && !t.congruent).forEach((t) -> {total.getAndAdd(t.time);});
			results.append(count>0 ? String.format("%.3f", ((float)total.get())/count) : "0.000").append(",").append(System.lineSeparator());
			results.append("total number of errors:").append(System.lineSeparator());
			results.append("switch trials,non-switch trials,congruent trials,incongruent trials").append(System.lineSeparator());
			// Calculate total errors of switch trials
			results.append(trials.stream().filter((t) -> t.useInError && t.switching && t.result.equals(Result.INCORRECT)).count()).append(",");
			// Calculate total errors of non-switch trials
			results.append(trials.stream().filter((t) -> t.useInError && !t.switching && t.result.equals(Result.INCORRECT)).count()).append(",");
			// Calculate total errors of congruent trials
			results.append(trials.stream().filter((t) -> t.useInError && t.congruent && t.result.equals(Result.INCORRECT)).count()).append(",");
			// Calculate total errors of switch trials
			results.append(trials.stream().filter((t) -> t.useInError && !t.congruent && t.result.equals(Result.INCORRECT)).count()).append(",").append(System.lineSeparator());
			results.append("switch cost").append(System.lineSeparator());
			// Calculate switch cost
			float switchCost = avgSwitchTime-avgNonSwitchTime;
			results.append(String.format("%.3f", switchCost)).append(System.lineSeparator());
			results.append("proportional switch cost:").append(System.lineSeparator());
			// Calculate proportional switch cost
			results.append(avgNonSwitchTime>0 ? String.format("%.3f", (float)(switchCost/avgNonSwitchTime)*100) : "0.000").append(System.lineSeparator());
			results.append("percentage of trials removed from error statistics:").append(System.lineSeparator());
			// Calculate % of trials removed from error statistics
			count = trials.size();
			long removed = trials.stream().filter((t) -> !t.useInError).count();
			results.append(count > 0 ? String.format("%.3f", (((float)removed)/count)*100) : "0.000").append(System.lineSeparator());
			results.append("percentage of trials removed from response time statistics:").append(System.lineSeparator());
			// Calculate % of trials removed from error statistics
			removed = trials.stream().filter((t) -> !t.useInResponse).count();
			results.append(count > 0 ? String.format("%.3f", (((float)removed)/count)*100) : "0.000").append(System.lineSeparator());
			return results.toString();
		}
	}
}
