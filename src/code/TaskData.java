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
	
	// This is a random number generator
	public static Random randomizer = new Random();
	
	/**
	 * This is a method to toss a Boolean coin.
	 * @return : true or false
	 */
	public static boolean flip() {
		return (randomizer.nextInt(2) == 0);
	}
	
	/**
	 * This is a method to generate the 4 blocks of trials in an experiment.
	 * @return : Returns a list of blocks.
	 */
	public static ArrayList<Block> createExperiment() {
		// Create the list to hold the blocks
		ArrayList<Block> blocks = new ArrayList<>();
		// Create the 4 types of blocks
		Block practiceRandom = new Block(Block.Type.PRACTICE_RANDOM);
		Block experimentalRandom = new Block(Block.Type.EXPERIMENTAL_RANDOM);
		Block practicePredictable = new Block(Block.Type.PRACTICE_PREDICTABLE);
		Block experimentalPredictable = new Block(Block.Type.EXPERIMENTAL_PREDICTABLE);
		// Determine whether predictable or random trials come first
		if (flip()) {
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
				numTrials = n;
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
		 * @param type : The type of block to create
		 */
		public Block(Type blockType) {
			type = blockType;
			int quadrant = 0;
			// Here we generate the specified number of predictably-placed trials.
			if (type.equals(Type.PRACTICE_PREDICTABLE) || type.equals(Type.EXPERIMENTAL_PREDICTABLE)) {
				for (int i=0; i<type.numTrials; i++) {
					trials.add(new Trial((quadrant++)%4, flip()));
					/**
					 * TODO: I may need to change this so that the same number of congruent & incongruent
					 * trials are created, using the same method I use for the random blocks. I am waiting
					 * to hear back about this.
					 */
				}
			} else {
				/*
				 * Here we generate the specified number of randomly-placed trials.
				 * We need to make sure that exactly half of these trials are "incongruent" - that is,
				 * the key that would be pressed for the letter option is different from the key that 
				 * would be pressed for the number option. The other half of the trials must be congruent - 
				 * that is, the same key could be pressed for both the letter and the number option. 
				 * Congruent and incongruent trials must be randomly distributed throughout the trial sequence.
				 * 
				 * To start, we generate a list of the indexes of all the trials which will be in the list.
				 */
				ArrayList<Integer> trialIndices = new ArrayList<Integer>(type.numTrials);
				for (int i=0; i<type.numTrials; i++) {
					trialIndices.add(i);
				}
				// Next, we randomly shuffle this list of indexes.
				Collections.shuffle(trialIndices);
				/*
				 * Then we take a subset of the shuffled indexes that is one half of the size of the total
				 * number of indexes. The random indexes in this new list will be the locations of the 
				 * congruent trials in the trial sequence to be generated. All indexes not in this list will
				 * be the locations of the incongruent trials in the trial sequence.
				 */
				List<Integer> congruentTrialIndices = trialIndices.subList(0, (type.numTrials/2));
				// Now the sequence of trials is generated.
				for (int i=0; i<type.numTrials; i++) {
					quadrant = randomizer.nextInt(3);
					if (i > 0) {
						// Here we ensure that this trial is in a different position than the last one.
						while (quadrant == trials.get(i-1).position.value) {
							quadrant = randomizer.nextInt(3);
						}
					}
					// Here we generate the new trial, specifying both its position and its congruence or incongruence.
					trials.add(new Trial(quadrant, congruentTrialIndices.contains(i)));
				}
			}
		}
		
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
		 */
		public Trial(int quadrant, boolean isCongruent) {
			// Assign the trial's congruence or incongruence as specified.
			congruent = isCongruent;
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
		
		@Override
		public String toString() {
			StringBuilder trialString = new StringBuilder();
			return trialString.append(letter).append(number).append(", ").append(position.toString()).append(System.lineSeparator()).toString();
		}
	}
}
