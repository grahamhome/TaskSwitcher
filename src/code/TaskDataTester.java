package code;

/*import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import code.TaskData.Block;
import code.TaskData.Trial;*/

public class TaskDataTester {}/*
	private static Random randomizer = new Random();
	public static void test() {
		try {
			PrintWriter outputWriter = new PrintWriter("C:/Users/Graham/TaskSwitcherTestOutput4.txt", "UTF-8");
			for (Block block : TaskData.createExperiment(true)) {
				outputWriter.write(block.toString());
			}
			outputWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void testQuantifiably() {
		int[] tests = {1, 10, 100, 1000};//, 10000, 100000};
		List<Character> vowels = new ArrayList<>();
		for (char v : TaskData.VOWELS) {
			vowels.add(v);
		}
		List<Character> odds = new ArrayList<>();
		for (char n : TaskData.ODDS) {
			odds.add(n);
		}
		StringBuilder results = new StringBuilder();
		for (int k=0; k<tests.length; k++) {
			int numTests = tests[k];
			int ltSwitchBalance = 0;
			int ltCongBalance = 0;
			int ltVowelBalance = 0;
			int ltOddBalance = 0;
			int unbalancedSwitchBlocks = 0;
			int unbalancedCongBlocks = 0;
			int unbalancedVowelBlocks = 0;
			int unbalancedOddBlocks = 0;
			float largestUnbalancedVowelFraction = 0;
			float largestUnbalancedOddFraction = 0;
			float totalUnbalancedVowels = 0;
			float totalUnbalancedOdds = 0;
			float totalUnbalancedVowelsBase = 0;
			float totalUnbalancedOddsBase = 0;
			for (int i=0; i<numTests; i++) {
				for (Block block : TaskData.createExperiment(randomizer.nextBoolean())) {
					int switchBalance = 0;
					int congBalance = 0;
					int vowelBalance = 0;
					int oddBalance = 0;
					Trial previous = block.first;
					Trial trial = previous.next;
					while (trial != null) {
						if (trial.congruent) {
							congBalance++; 
						} else {
							congBalance--;
						}
						if (vowels.contains(trial.letter)) {
							vowelBalance++;
						} else {
							vowelBalance--;
						}
						if (odds.contains(trial.number)) {
							oddBalance++;
						} else {
							oddBalance--;
						}
						if (trial.position.value < 2 != previous.position.value < 2) {
							switchBalance++;
						} else {
							switchBalance--;
						}
						trial = trial.next;
					}
					ltSwitchBalance += switchBalance;
					ltCongBalance += congBalance;
					ltVowelBalance += vowelBalance;
					ltOddBalance += oddBalance;
					unbalancedSwitchBlocks += (switchBalance != 0 ? 1 : 0);
					unbalancedCongBlocks += (congBalance != 0 ? 1 : 0);
					unbalancedVowelBlocks += (vowelBalance != 0 ? 1 : 0);
					unbalancedOddBlocks += (oddBalance != 0 ? 1 : 0);
					if (Math.abs(((float)vowelBalance)/block.type.numTrials) > Math.abs(largestUnbalancedVowelFraction)) {
						largestUnbalancedVowelFraction = ((float)vowelBalance)/block.type.numTrials;
					}
					if (Math.abs(((float)oddBalance)/block.type.numTrials) > Math.abs(largestUnbalancedOddFraction)) {
						largestUnbalancedOddFraction = ((float)oddBalance)/block.type.numTrials;
					}
					if (vowelBalance != 0) {
						totalUnbalancedVowels += Math.abs(vowelBalance);
						totalUnbalancedVowelsBase += block.type.numTrials;
					}
					if (oddBalance != 0) {
						totalUnbalancedOdds += Math.abs(oddBalance);
						totalUnbalancedOddsBase += block.type.numTrials;
					}
				}
			}
			int totalTrialsInEachTest = TaskData.EXPERIMENTAL_PREDICTABLE_TRIALS+TaskData.PRACTICE_PREDICTABLE_TRIALS+
					TaskData.EXPERIMENTAL_RANDOM_TRIALS+TaskData.PRACTICE_RANDOM_TRIALS;
			int totalTrialsInAllTests = totalTrialsInEachTest*numTests;
			if (k>0) {
				results.append(System.lineSeparator());
			}
			results.append("TEST " + (k+1)).append(System.lineSeparator());
			results.append("Ran " + numTests + (numTests>1 ? " experiments " : " experiment ") + "for a total of:").append(System.lineSeparator());
			results.append("\t" + numTests*2 + " random blocks and " + numTests*2 + " predictable blocks. (" + numTests*4 + " total blocks)").append(System.lineSeparator());
			results.append("\t" + totalTrialsInAllTests + " trials").append(System.lineSeparator());
			results.append("The first trial from each block was excluded from the statistics.").append(System.lineSeparator());
			results.append("Overall, there were: ").append(System.lineSeparator());
			results.append("\t" + Math.abs(ltSwitchBalance) + " more " + (ltSwitchBalance >= 0 ? "switch" : "non-switch") + 
					" trials than " + (ltSwitchBalance < 0 ? "switch" : "non-switch") + " trials. (" + ((float)Math.abs(ltSwitchBalance)*100)/totalTrialsInAllTests + "% of total trials)").append(System.lineSeparator());
			results.append("\t" + Math.abs(ltCongBalance) + " more " + (ltCongBalance >= 0 ? "congruent" : "incongruent") + 
					" trials than " + (ltCongBalance < 0 ? "congruent" : "incongruent") + " trials. (" + ((float)Math.abs(ltCongBalance)*100)/totalTrialsInAllTests + "% of total trials)").append(System.lineSeparator());
			results.append("\t" + Math.abs(ltVowelBalance) + " more " + (ltVowelBalance >= 0 ? "vowels" : "consonants") + 
					" than " + (ltVowelBalance < 0 ? "vowels" : "consonants") + ". (" + ((float)Math.abs(ltVowelBalance)*100)/totalTrialsInAllTests + "% of total letters)").append(System.lineSeparator());
			results.append("\t" + Math.abs(ltOddBalance) + " more " + (ltOddBalance >= 0 ? "odd" : "even") + 
					" numbers than " + (ltOddBalance < 0 ? "odd" : "even") + " numbers. (" + ((float)Math.abs(ltOddBalance)*100)/totalTrialsInAllTests + "% of total numbers)").append(System.lineSeparator());
			results.append("\t" + Math.abs(unbalancedSwitchBlocks) + " blocks with a different number of switching & non-switching trials. (" + ((float)Math.abs(unbalancedSwitchBlocks)*100)/(numTests*4) + "% of total blocks)").append(System.lineSeparator());
			results.append("\t" + Math.abs(unbalancedCongBlocks) + " blocks with a different number of congruent & incongruent trials. (" + ((float)Math.abs(unbalancedCongBlocks)*100)/(numTests*4) + "% of total blocks)").append(System.lineSeparator());
			results.append("\t" + Math.abs(unbalancedVowelBlocks) + " blocks with a different number of vowel & consonant trials. (" + ((float)Math.abs(unbalancedVowelBlocks)*100)/(numTests*4) + "% of total blocks)").append(System.lineSeparator());
			results.append("\t" + Math.abs(unbalancedOddBlocks) + " blocks with a different number of odd-number & even-number trials. (" + ((float)Math.abs(unbalancedOddBlocks)*100)/(numTests*4) + "% of total blocks)").append(System.lineSeparator());
			results.append("In the block with the largest difference between the numbers of vowels and consonants, " + Math.abs(largestUnbalancedVowelFraction*100) + "% of the letters were " + (largestUnbalancedVowelFraction > 0 ? "vowels." : "consonants.")).append(System.lineSeparator());
			results.append("In the block with the largest difference between the numbers of odd and even numbers, " + Math.abs(largestUnbalancedOddFraction*100) + "% of the numbers were " + (largestUnbalancedVowelFraction > 0 ? "odd." : "even.")).append(System.lineSeparator());
			results.append("Among all blocks with a different number of vowel and consonant trials, the average was that " + (totalUnbalancedVowels*100)/totalUnbalancedVowelsBase + "% of the trials in the block were unevenly distributed between vowels and consonants.").append(System.lineSeparator());
			results.append("Among all blocks with a different number of odd and even-number trials, the average was that " + (totalUnbalancedOdds*100)/totalUnbalancedOddsBase + "% of the trials in the block were unevenly distributed between odds and evens.").append(System.lineSeparator());
		}
		try {
			PrintWriter outputWriter = new PrintWriter("C:/Users/Graham/TaskSwitcherQuantifiableTestOutput.txt", "UTF-8");
			outputWriter.write(results.toString());
			outputWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}*/
