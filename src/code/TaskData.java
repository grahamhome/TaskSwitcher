package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
	 * Number of Trials:
	 * How many number/letter pairs to generate for each block of the experiment.
	 * Feel free to set these to any whole numbers that you want.
	 * 
	 * But beware! Each number in this section MUST be even.
	 * This is necessary to ensure there are an exactly equal number of
	 * congruent and incongruent trials in each block.
	 * Additionally, the number of trials in each predictable block 
	 * must be evenly divisible by 4.
	 * You've been warned!
	 */
	
	// Number of pairs in the predictable practice block
	public static int PRACTICE_PREDICTABLE_TRIALS = 40;
	// Number of pairs in the predictable experimental block
	public static int EXPERIMENTAL_PREDICTABLE_TRIALS = 150;
	// Number of pairs in the random practice block
	public static int PRACTICE_RANDOM_TRIALS = 40;
	// Number of pairs in the random experimental block
	public static int EXPERIMENTAL_RANDOM_TRIALS = 150;
	
	/*
	 * Percentages:
	 * Percentages may be decimal values if you want.
	 */
	
	// Percentage of letters that will be vowels (the other percent will be consonants)
	public static final float PERCENT_VOWEL = 50;
	// Percentage of numbers that will be even (the other percent will be odd)
	public static final float PERCENT_EVEN = 50;
	// Percentage of number/letter pairs that will be congruent (the rest will be incongruent)
	public static final float PERCENT_CONGRUENT = 50;
	
	/*
	 * Times:
	 * All times are in milliseconds and must be whole numbers.
	 */
	
	// Length of time for which number/letter pairs will remain visible if no input is detected
	public static final int NO_INPUT_DURATION = 5000;
	// Length of time between number/letter pair appearances after a correct input is detected
	public static final int CORRECT_INPUT_PAUSE = 150;
	// Length of time between number/letter pair appearances after an incorrect input is detected
	public static final int INCORRECT_INPUT_PAUSE = 1500;
	// Length of time between number/letter pair appearances if no input is detected
	public static final int NO_INPUT_PAUSE = 1500;
	// Length of time to pause between the predictable and random tests
	public static final int BREAK = 2000;
	
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
	
	/*
	 * Input Keys:
	 * Feel free to change each of these to any individual key you can 
	 * press on your keyboard (key combinations are not supported).
	 */
	
	// Left-hand-side key
	public static final char LEFT = 'z';
	// Right-hand-side key
	public static final char RIGHT = '/';
	
	/*
	 * Correct Keys:
	 * Which keys should be pressed in response to each data option.
	 * Feel free to swap these if you want.
	 */
	
	// Vowel key
	public static final char VOWEL_KEY = LEFT;
	// Consonant key
	public static final char CONSONANT_KEY = RIGHT;
	// Odd number key
	public static final char ODD_KEY = LEFT;
	// Even number key
	public static final char EVEN_KEY = RIGHT;
	
	/*
	 * End of Experimental Variables section.
	 * Don't change anything below this line 
	 * if you want the program to work correctly :)
	 */
	
	/**
	 * This is a method to toss a Boolean coin.
	 * @return : true or false
	 */
	public static boolean flip() {
		return (new Random().nextInt(2) == 0);
	}
	
	/**
	 * This is a method to generate the 4 blocks of trials in an experiment.
	 * @param randomFirst : Specifies whether the blocks with randomly-placed trials 
	 * should come first (true) or second (false).
	 * @return : Returns a list of blocks.
	 */
	public static ArrayList<Block> createExperiment(boolean randomFirst) {
		// Create the list to hold the blocks
		ArrayList<Block> blocks = new ArrayList<>();
		// Create the 4 types of blocks
		Block practiceRandom = new Block(Block.Type.PRACTICE_RANDOM);
		Block experimentalRandom = new Block(Block.Type.EXPERIMENTAL_RANDOM);
		Block practicePredictable = new Block(Block.Type.PRACTICE_PREDICTABLE);
		Block experimentalPredictable = new Block(Block.Type.EXPERIMENTAL_PREDICTABLE);
		// Put the blocks into the list according to the specified order
		if (randomFirst) {
			// Add the random trials, then the predictable trials to the list
			blocks.add(practiceRandom);
			blocks.add(experimentalRandom);
			blocks.add(practicePredictable);
			blocks.add(experimentalPredictable);
		} else {
			// Add the predictable trials, then the random trials to the list
			blocks.add(practicePredictable);
			blocks.add(experimentalPredictable);
			blocks.add(practiceRandom);
			blocks.add(experimentalRandom);
		}
		return blocks;
	}
	
	/**
	 * A block is a list of randomly-placed or predictably-placed trials.
	 */
	public static class Block {
		/*
		 *  These are the different types of blocks in this experiment.
		 *  I know this looks confusing, but don't worry about it.
		 *  Everything is fine.
		 */
		public enum Type {
			PRACTICE_PREDICTABLE(PRACTICE_PREDICTABLE_TRIALS),
			EXPERIMENTAL_PREDICTABLE(EXPERIMENTAL_PREDICTABLE_TRIALS),
			PRACTICE_RANDOM(PRACTICE_RANDOM_TRIALS),
			EXPERIMENTAL_RANDOM(EXPERIMENTAL_PREDICTABLE_TRIALS),
			;
			public int numTrials;
			private Type(int n) {
				/*
				 * We need to generate one extra trial in each block, 
				 * since the first trial will not be used when tallying the results.
				 */
				numTrials = n+1;
			}
			
			@Override
			public String toString() {
				return name().toLowerCase().replace("_", ", ");
			}
			
		}
		// The type of this particular block
		public Type type;
		// The list of trials in this block
		public ArrayList<Trial> trials = new ArrayList<>();
		
		/**
		 * This creates a block of the specified type.
		 * Each block contains a number of trials that is one greater than the number that was specified.
		 * This is because the first trial will not be used when tallying the results.
		 * @param type : The type of block to create.
		 */
		public Block(Type blockType) {
			type = blockType;
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
			if (type.equals(Type.PRACTICE_PREDICTABLE) || type.equals(Type.EXPERIMENTAL_PREDICTABLE)) {
				/**
				 * Here we generate the specified number of predictably-placed trials, specifying their position,
				 * their congruence or incongruence, and whether or not they will be counted in the experimental results.
				 */
				for (int i=0; i<type.numTrials; i++) {
					trials.add(new Trial((quadrant++)%4, (i==0 ? flip() : congruentTrialIndices.contains(i)), (i>0)));
				}
			} else {
				// Here we generate the specified number of randomly-placed trials.
				for (int i=0; i<type.numTrials; i++) {
					if (i == 0) {
						quadrant = new Random().nextInt(3);
					} else {
						/*
						 * We need to know the position of the last trial to ensure that this trial will
						 * be task-switching or non-task-switching as specified.
						 */
						quadrant = trials.get(i-1).position.value;
						/*
						 * Here we ensure that this trial is in a different position than the last one.
						 * We also ensure that only trials at the "switch indices" will make the subject
						 * switch between letter-identification and number-identification.
						 */
						if (switchTrialIndices.contains(i)) {
							// Set the trial's quadrant to create a task-switch trial
							if (quadrant < 2) {
								quadrant = (flip() ? 2 : 3);
							} else {
								quadrant = (flip() ? 0 : 1);
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
					trials.add(new Trial(quadrant, (i==0 ? flip() : congruentTrialIndices.contains(i)), (i>0)));
				}
			}
		}
		
		/**
		 * This method returns a string representation of a block for data output purposes.
		 */
		@Override
		public String toString() {
			StringBuilder blockString = new StringBuilder(type.toString());
			blockString.append(System.lineSeparator());
			for (Trial trial : trials) {
				blockString.append(trial.toString());
			}
			return blockString.append(System.lineSeparator()).toString();
		}
	}
	
	/**
	 * A trial is an appearance of a number and letter combination in a specified position.
	 * Each trial is guaranteed not to contain the same number or letter as the 
	 * trial which was generated before it.
	 */
	public static class Trial {
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
		public char correctReponse;
		// Variable indicating whether or not this trial will be used in result calculations.
		public boolean counted;
		/*
		 * If this variable is set to 'true', the trial will be congruent.
		 * This means that its letter and number will both correspond to the
		 * same key press. 
		 * If this variable is set to 'false', the trial will be incongruent.
		 * In other words, its letter and number will correspond to different
		 * key presses.
		 */
		public boolean congruent;
		
		// The letter which was generated most recently (so it won't be used in the next trial to be generated)
		// Starts as '@', which is why that may not be included in the list of letters at the top of this file.
		private static char lastLetter = '@';
		// The number which was generated most recently (so it won't be used in the next trial to be generated)
		// Starts as '@', which may not be included in the list of numbers at the top of this file (not that you would do that anyway).
		private static char lastNumber = '@';
		
		/**
		 * This creates a congruent or incongruent trial with the specified position.
		 * @param quadrant : A number 0-3 where 0 = upper left, 1 = upper right, 2 = lower right, 3 = lower left.
		 * @param isCongruent : True for a congruent trial, false for an incongruent trial.
		 * @param isCounted : True for a trial to be included in results calculation, false for a trial to be excluded.
		 */
		public Trial(int quadrant, boolean isCongruent, boolean isCounted) {
			// Assign the trial's congruence or incongruence as specified.
			congruent = isCongruent;
			// Assign the trial's counted/not counted status as specified
			counted = isCounted;
			// Randomly decide if the trial will contain a vowel or consonant.
			boolean hasVowel = flip();
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
				letter = letters[new Random().nextInt(letters.length)];
			}
			// Choose a number at random from the selected set.
			// Ensure that it is different from the last trial's number.
			while (number == lastNumber) {
				number = numbers[new Random().nextInt(numbers.length)];
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
		
		/**
		 * This method returns a string representation of a trial for data output purposes.
		 */
		@Override
		public String toString() {
			StringBuilder trialString = new StringBuilder();
			return trialString.append(letter).append(number).append(", ").append(position.toString()).append(", ")
					.append(congruent ? "congruent" : "incongruent").append(counted ? "" : ", not counted").append(System.lineSeparator()).toString();
		}
	}
}
