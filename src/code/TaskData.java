package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;
/**
 * This class creates and stores the data to be used in the experiment:
 * the sequence of numbers and letters which will appear in different
 * quadrants of the display.
 * Note that all comments appear above the line which they refer to.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class TaskData {
	/*
	 * Experimental Variables:
	 * This section defines the variables which control the way the experiment works. 
	 * They are set to the given default values,
	 * but you can change them to (almost) anything you want.
	 */
	
	/*
	 * Percentages:
	 * Percentages may be decimal values if you want.
	 */
	
	/*
	 * Times:
	 * All times are in milliseconds and must be whole numbers.
	 */
	
	// Length of time for which number/letter pairs will remain visible if no input is detected
	public static final int NO_INPUT_DURATION = 5000;
	// Length of time after number/letter pairs appear before input is no longer considered valid.
	public static final int INPUT_DEADLINE = 2000;
	// Minimum length of time before an input is considered valid
	public static final int INPUT_MINIMUM = 150;
	// Length of time between number/letter pair appearances after a correct input is detected
	public static final int CORRECT_INPUT_PAUSE = 150;
	// Length of time between number/letter pair appearances after an incorrect input is detected
	public static final int INCORRECT_INPUT_PAUSE = 1500;
	// Length of time between number/letter pair appearances if no input is detected
	public static final int NO_INPUT_PAUSE = 1500;
	// Length of time between when the participant presses the "Enter" key and when the trials begin
	public static final int COUNTDOWN_DURATION = 5000;
	
	/*
	 * Data Options:
	 * Feel free to add/remove any values from any list. 
	 * The only character which you may not use is the '@' character.
	 * Don't even think about it.
	 */
	
	// Vowels
	public static final char[] VOWELS = new char[] { 'A', 'E', 'I', 'U' };
	// Consonants
	public static final char[] CONSONANTS = new char[] { 'G', 'K', 'M', 'R' };
	// Odd numbers
	public static final char[] ODDS = new char[] { '3', '5', '7', '9' };
	// Even numbers
	public static final char[] EVENS = new char[] { '2', '4', '6', '8' };
	
	 // Input Keys:
	
	// Left-hand-side key
	public static final KeyCode LEFT_KEY = KeyCode.Z;
	// Key name
	public static final String LEFT_KEY_NAME = "z";
	// Right-hand-side key
	public static final KeyCode RIGHT_KEY = KeyCode.SLASH;
	// Key name
	public static final String RIGHT_KEY_NAME = "/";
	
	/*
	 * Correct Keys For Input Types:
	 * Which keys should be pressed in response to each data option.
	 * Feel free to swap these if you want.
	 */
	
	// Vowel key
	public static KeyCode VOWEL_KEY = LEFT_KEY;
	// Consonant key
	public static KeyCode CONSONANT_KEY = RIGHT_KEY;
	// Odd number key
	public static KeyCode ODD_KEY = LEFT_KEY;
	// Even number key
	public static KeyCode EVEN_KEY = RIGHT_KEY;
	
	/*
	 * End of Experimental Variables section.
	 * Don't change anything below this line 
	 * if you want the program to work correctly :)
	 */
	
	// The first task of the current set of experimental data.
	public static SubTask first;
	// The current task of the current set of experimental data.
	private static SubTask current;
	
	// Returns the next task of the current set of experimental data.
	public static SubTask next() {
		return (current == null ? (current = first) : current instanceof End ? current : (current = current.next));
	}
	
	private static Random randomizer = new Random();
	
	/**
	 * Abstract class which defines attributes which apply to components of an 
	 * experiment, including blocks of trials, instruction sets and pauses.
	 */
	public static abstract class SubTask{
		// This is the task that will run after this task, or null if this is the last task.
		public SubTask next;
		
		public SubTask() {}
		
		public SubTask(SubTask nextTask) {
			next = nextTask;
		}
	}
	
	/**
	 * This is a method to generate the 4 blocks of trials in an experiment, as well
	 * as the instructions and the pause between the random and predictable trials.
	 * @param randomFirst : Specifies whether the blocks with randomly-placed trials 
	 * should come first (true) or second (false).
	 */
	public static void createExperiment(boolean randomFirst) {
		if (randomFirst) {
			// Generate the random trials followed by the predictable trials.
			first =  new Instructions(
				new Message(Strings.PRACTICE_RANDOM_STARTING,
				new Countdown(
				generateBlock(BlockType.PRACTICE_RANDOM, 
				new Message(Strings.PRACTICE_ENDED_EXPERIMENT_STARTING,
				new Countdown(
				generateBlock(BlockType.EXPERIMENTAL_RANDOM, 
				new Break(
				new Message(Strings.PRACTICE_PREDICTABLE_STARTING,
				new Countdown(
				generateBlock(BlockType.PRACTICE_PREDICTABLE,
				new Message(Strings.PRACTICE_ENDED_EXPERIMENT_STARTING,
				new Countdown(
				generateBlock(BlockType.EXPERIMENTAL_PREDICTABLE, 
				new Message(Strings.EXPERIMENT_ENDED,
				new End())))))))))))))));
		} else {
			// Generate the predictable trials followed by the random trials.
			first = new Instructions(
				new Message(Strings.PRACTICE_PREDICTABLE_STARTING,
				new Countdown(
				generateBlock(BlockType.PRACTICE_PREDICTABLE, 
				new Message(Strings.PRACTICE_ENDED_EXPERIMENT_STARTING,
				new Countdown(
				generateBlock(BlockType.EXPERIMENTAL_PREDICTABLE, 
				new Break(
				new Message(Strings.PRACTICE_RANDOM_STARTING,
				new Countdown(
				generateBlock(BlockType.PRACTICE_RANDOM,
				new Message(Strings.PRACTICE_ENDED_EXPERIMENT_STARTING,
				new Countdown(
				generateBlock(BlockType.EXPERIMENTAL_RANDOM, 
				new Message(Strings.EXPERIMENT_ENDED,
				new End())))))))))))))));
		}
	}
	
	/*
	 *  These are the different types of blocks in an experiment.
	 *  I know this looks confusing, but don't worry about it.
	 *  Everything is fine.
	 */
	public enum BlockType {
		PRACTICE_PREDICTABLE(Config.practiceTrialCount),
		EXPERIMENTAL_PREDICTABLE(Config.experimentalTrialCount),
		PRACTICE_RANDOM(Config.practiceTrialCount),
		EXPERIMENTAL_RANDOM(Config.experimentalTrialCount),
		;
		public int numTrials;
		private BlockType(int n) {
			/*
			 * We need to generate one extra trial in each block, 
			 * since the first trial will not be used when tallying the results.
			 */
			numTrials = n+1;
		}
		
		@Override
		public String toString() {
			return name().toLowerCase().replace("_", " ");
		}
		
	}
		
	/**
	 * This creates a block of the specified type.
	 * Each block contains a number of trials that is one greater than the number that was specified.
	 * This is because the first trial will not be used when tallying the results.
	 * @param type : The type of block to create.
	 * @param next : The block which comes after this block.
	 */
	public static Trial generateBlock (BlockType type, SubTask nextTask) {
		Trial firstTask = null;
		Trial previous = null;
		// This variable keeps track of which quadrant of the grid the current task is in
		int quadrant = 0;
		/** We need to make sure that exactly half of the trials in each block are "incongruent".
		 * That is, the key that would be pressed for the letter option is different from the key that 
		 * would be pressed for the number option. The other half of the trials must be congruent. 
		 * In other words, the same key could be pressed for both the letter and the number option. 
		 * Congruent and incongruent trials must be randomly distributed throughout the trial sequence.
		 * 
		 * Additionally, we need to ensure that half of the trials in each block are "task-switching" trials.
		 * That is, if the previous trial was a number identification task, this trial will be a letter-identification
		 * task, and vice versa. This will happen automatically in the predictable blocks, but must be enforced in the 
		 * random blocks. In the random blocks, task-switching and non-task-switching trials must be distributed randomly.
		 */
		 /**
		  * To start, we generate two lists of the indexes of all the trials which will be in the list, 
		  * excluding the first trial which will not be counted in the results.
		  */
		List<Integer> congruentTrialIndices = new ArrayList<Integer>(type.numTrials);
		List<Integer> switchTrialIndices = new ArrayList<Integer>(type.numTrials);
		for (int i=1; i<type.numTrials; i++) {
			congruentTrialIndices.add(i);
			switchTrialIndices.add(i);
		}
		// Next, we randomly shuffle the lists of indexes.
		Collections.shuffle(congruentTrialIndices);
		Collections.shuffle(switchTrialIndices);
		/*
		 * Then we take a subset of the shuffled indexes that is one half of the size of the total
		 * number of indexes from each list of indexes. The random indexes in these new lists will be 
		 * the locations of the congruent and switch trials in the trial sequence to be generated. All 
		 * indexes not in these lists will be the locations of the incongruent and non-switch trials in the trial sequence.
		 */
		congruentTrialIndices = congruentTrialIndices.subList(0, type.numTrials/2);
		switchTrialIndices = switchTrialIndices.subList(0, type.numTrials/2);
		
		/*
		 * Now we're ready to generate the trials for this block.
		 * First we check to see if we are generating predictably-placed or randomly-placed trials.
		 */
		if (type.equals(BlockType.PRACTICE_PREDICTABLE) || type.equals(BlockType.EXPERIMENTAL_PREDICTABLE)) {
			/**
			 * Here we generate the specified number of predictably-placed trials, specifying their position,
			 * their congruence or incongruence, and whether or not they will be counted in the experimental results.
			 */
			for (int i=0; i<type.numTrials; i++) {
				boolean switchTrial = (i>0 && ((quadrant%2) == 0));
				Trial next = new Trial(i+1, (quadrant++)%4, (i==0 ? randomizer.nextBoolean() : congruentTrialIndices.contains(i)), switchTrial,  type);
				if (previous != null) {
					previous.next = next;
				} else {
					firstTask = next;
				}
				previous = next;
			}
		} else {
			// Here we generate the specified number of randomly-placed trials.
			for (int i=0; i<type.numTrials; i++) {
				if (i == 0) {
					quadrant = randomizer.nextInt(3);
				} else {
					/*
					 * We need to know the position of the last trial to ensure that this trial will
					 * be task-switching or non-task-switching as specified.
					 */
					quadrant = previous.position.value;
					/*
					 * Here we ensure that this trial is in a different position than the last one.
					 * We also ensure that only trials at the "switch indices" will make the subject
					 * switch between letter-identification and number-identification.
					 */
					if (switchTrialIndices.contains(i)) {
						// Set the trial's quadrant to create a task-switch trial
						if (quadrant < 2) {
							quadrant = (randomizer.nextBoolean() ? 2 : 3);
						} else {
							quadrant = (randomizer.nextBoolean() ? 0 : 1);
						}
					} else {
						// Set the trial's quadrant to create a non-task-switch trial.
						if (quadrant < 2) {
							quadrant = (quadrant == 0 ? 1 : 0);
						} else {
							quadrant = (quadrant == 2 ? 3 : 2);
						}
					}
				}
				/*
				 * Finally we generate the new trial, specifying its position, its congruence or incongruence, 
				 * and whether or not it will be counted in the experimental results.
				 */
				Trial next = new Trial(i+1, quadrant, (i==0 ? randomizer.nextBoolean() : congruentTrialIndices.contains(i)), switchTrialIndices.contains(i), type);
				if (previous != null) {
					previous.next = next;
				} else {
					firstTask = next;
				}
				previous = next;
			}
		}
		previous.next = nextTask;
		return firstTask;
	}
	
	/**
	 * A trial is an appearance of a number and letter combination in a specified position.
	 * Each trial is guaranteed not to contain the same number or letter as the 
	 * trial which was generated before it.
	 * Each trial belongs to a set of trials called a block, and each trial records which block it belongs to.
	 */
	public static class Trial extends SubTask {
		
		/**
		 * Trial Data Variables
		 */
		
		// The index of this particular trial (relative to its block) - used for output only
		public int index;
		
		// The block to which this particular trial belongs.
		public BlockType type;
		
		// These are the different positions in which trials may appear.
		public enum Position {
			UPPER_LEFT(0),
			UPPER_RIGHT(1),
			LOWER_RIGHT(2),
			LOWER_LEFT(3),
			;
			public int value;
			private Position(int v) {
				this.value = v;
			}
			@Override
			public String toString() {
				return name().toLowerCase().replace("_",  " ");
			}
		}
		// The position of this particular trial
		public Position position;
		// The letter which appears during this trial
		public char letter;
		// The number which appears during this trial
		public char number;
		// The correct response to this trial
		public KeyCode correctReponse;
		
		/*
		 * If this variable is set to 'true', the trial will be congruent.
		 * This means that its letter and number will both correspond to the
		 * same key press. 
		 * If this variable is set to 'false', the trial will be incongruent.
		 * In other words, its letter and number will correspond to different
		 * key presses.
		 */
		public boolean congruent;
		
		/*
		 * If this variable is set to 'true', this trial will be a task-switching trial.
		 * This means that, if the previous trial was a number-identification task, this
		 * trial will be a letter-identification task, and vice-versa.
		 * If this variable is set to 'false', this trial will not be a task-switching trial:
		 * it will be the same kind of task as its predecessor.
		 */
		public boolean switching;
		
		/**
		 * Trial Response Variables
		 */
		
		// The time-to-input of this trial
		public long time = 0;
		
		// The potential results of a trial.
		public enum Result {
			MISSED_DEADLINE,
			NO_INPUT,
			CORRECT,
			INCORRECT,
			NOT_SET,
			;
		}
		
		// The result of this trial.
		public Result result = Result.NOT_SET;
		
		// The actual response to this trial (CANCEL indicates no response/missed deadline)
		public KeyCode actualResponse = KeyCode.CANCEL;
		// Whether or not the trial received a response in the "valid response time" window
		private boolean validResponseTime = false;
		// Variable indicating whether or not this trial has completed. Defaults to false so that if
		// the experiment is terminated early, uncompleted trials will not be counted.
		private boolean seen = false;
		
		// Whether or not this trial follows a failed trial
		private boolean followsFailure = false;
		
		// Whether or not this trial should be used for error and response time statistics
		public boolean useInError = false;
		public boolean useInResponse = false;
		
		
		/**
		 * Trial Generation Utility Variables
		 */
		
		// The letter which was generated most recently (so it won't be used in the next trial to be generated)
		// Starts as '@', which is why that may not be included in the list of letters at the top of this file.
		private static char lastLetter = '@';
		// The number which was generated most recently (so it won't be used in the next trial to be generated)
		// Starts as '@', which may not be included in the list of numbers at the top of this file (not that you would do that anyway).
		private static char lastNumber = '@';
		// The trial result which was saved most recently (so that trials after error trials can be ignored in the statistics)
		private static Result lastResult = Result.NOT_SET;
		
		/**
		 * This creates a congruent or incongruent trial with the specified position.
		 * @param index : The index of this trial relative to its block
		 * @param quadrant : A number 0-3 where 0 = upper left, 1 = upper right, 2 = lower right, 3 = lower left.
		 * @param isCongruent : True for a congruent trial, false for an incongruent trial.
		 * @param isFirst : Indicates whether or not the trial is the first in a block
		 * @param isSwitching : Indicates whether the trial is switching or non-switching
		 * @param next: The task which comes after this trial.
		 */
		public Trial(int trialIndex, int quadrant, boolean isCongruent, boolean isSwitching, BlockType blockType) {
			index = trialIndex;
			congruent = isCongruent;
			switching = isSwitching;
			type = blockType;
			// Randomly decide if the trial will contain a vowel or consonant.
			boolean hasVowel = randomizer.nextBoolean();
			char[] letters = (hasVowel ? VOWELS : CONSONANTS);
			/*
			 * Now we know if the trial must contain an odd or even number 
			 * in order to be congruent or incongruent as specified.
			 */
			boolean hasOdd = false;
			if ((congruent && hasVowel) || (!congruent && !hasVowel)) {
				hasOdd = (VOWEL_KEY == ODD_KEY);
			} else {
				hasOdd = (CONSONANT_KEY == ODD_KEY);
			}
			char[] numbers = (hasOdd ? ODDS : EVENS);
			letter = lastLetter;
			number = lastNumber;
			// Choose a letter at random from the selected set.
			// Ensure that it is different from the last trial's letter.
			while (letter == lastLetter) {
				letter = letters[randomizer.nextInt(letters.length)];
			}
			// Choose a number at random from the selected set.
			// Ensure that it is different from the last trial's number.
			while (number == lastNumber) {
				number = numbers[randomizer.nextInt(numbers.length)];
			}
			lastLetter = letter;
			lastNumber = number;
			/*
			 * Assign the specified position to the trial, and store the correct response 
			 * for this trial so it can be quickly checked during the experiment.
			 */
			switch (quadrant) {
			case 0:
				position = Position.UPPER_LEFT;
				correctReponse = (hasVowel ? VOWEL_KEY : CONSONANT_KEY);
				break;
			case 1:
				position = Position.UPPER_RIGHT;
				correctReponse = (hasVowel ? VOWEL_KEY : CONSONANT_KEY);
				break;
			case 2:
				position = Position.LOWER_RIGHT;
				correctReponse = (hasOdd ? ODD_KEY : EVEN_KEY);
				break;
			case 3:
				position = Position.LOWER_LEFT;
				correctReponse = (hasOdd ? ODD_KEY : EVEN_KEY);
				break;
			}
		}
		
		public synchronized Result end(KeyCode input, long elapsedTime) {
			// This is true if the trial has already ended.
			if (!result.equals(Result.NOT_SET)) {
				return result;
			}
			// Mark the trial as seen
			seen = true;
			// Indicate whether or not the trial is the successor to a failed trial
			followsFailure = lastResult.equals(Result.INCORRECT);		
			// This is true if the trial received no input.
			if (input.equals(KeyCode.CANCEL)) {
				validResponseTime = false;
				time = NO_INPUT_DURATION;
				return (lastResult = (result = Result.NO_INPUT));
			}
			// This is true if the trial received input before or after the input deadline.
			// Indicate the trial's result
			result = (input.equals(correctReponse) ? Result.CORRECT : Result.INCORRECT);
			// Indicate whether or not the trial's response was received in the "valid response time" window
			validResponseTime = (elapsedTime > INPUT_MINIMUM && elapsedTime < INPUT_DEADLINE);
			// Indicate whether or not this trial should be used in error and response time calculations
			useInError = seen && index>1 && validResponseTime;
			useInResponse = seen && index>1 && validResponseTime && !result.equals(Result.INCORRECT) && !followsFailure;
			time = elapsedTime;
			actualResponse = input;
			return (lastResult = (result));
		}
		
		/**
		 * This method returns a string representation of a trial for data output purposes.
		 */
		@Override
		public String toString() {
			return new StringBuilder()
					.append(StartScreen.subjectNumber).append(",")
					.append(type.equals(BlockType.PRACTICE_PREDICTABLE) || type.equals(BlockType.EXPERIMENTAL_PREDICTABLE) ? 1 : 2).append(",")
					.append(type.equals(BlockType.PRACTICE_PREDICTABLE) || type.equals(BlockType.PRACTICE_RANDOM) ? 1 : 2).append(",")
					.append(index).append(",")
					.append(result.equals(Result.CORRECT) ? 1 : 2).append(",")
					.append(time).append(",")
					.append(letter).append(number).append(",")
					.append(position.value+1).append(",")
					.append(!switching ? 1 : 2).append(",")
					.append(congruent ? 1 : 2).append(",")
					.append(actualResponse.equals(KeyCode.CANCEL) ? "None" : actualResponse.equals(TaskData.LEFT_KEY) ? TaskData.LEFT_KEY_NAME : TaskData.RIGHT_KEY_NAME).append(",")
					.append(useInError ? 1 : 2).append(",")
					.append(useInResponse ? 1 : 2).append(System.lineSeparator()).toString();
		}
	}
	
	public static class Break extends SubTask {
		public Break(SubTask nextTask) {
			super(nextTask);
		}
	}
	
	public static class Countdown extends SubTask {
		public Countdown(SubTask nextTask) {
			super(nextTask);
		}
	}
	
	public static class Instructions extends SubTask {
		public Instructions(SubTask nextTask) {
			super(nextTask);
		}
	}
	
	public static class Message extends SubTask {
		public String message;
		public Message(String messageContent, SubTask nextTask) {
			super(nextTask);
			message = messageContent;
		}
	}
	
	public static class End extends SubTask {
	}
}
