package code;

import java.util.ArrayList;
import java.util.Collections;
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
	 * "expected" and "unexpected" task switches in each block.
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
	 * Feel free to change each of these to any single key you can 
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
					trials.add(new Trial((quadrant++)%4));
				}
			} else {
				/*
				 * Here we generate the specified number of randomly-placed trials.
				 * We need to make sure that exactly half of these trials are "unexpected" - that is, the trial
				 * will not appear on the same side of the x-axis as the previous trial. (TODO: unless it's that the correct
				 * button press for the trial is not the same as the correct button press for the previous trial.)
				 * This process is a little complex, but I've described it below as best as I can.
				 * 
				 * To start, we generate a list of the indexes of all the trials which will be in the list, except
				 * for the first index. The first trial is inherently unexpected: since there is no previous trial, 
				 * the subject cannot possibly anticipate what is coming next.
				 */
				ArrayList<Integer> checkpoints = new ArrayList<Integer>(type.numTrials);
				for (int i=1; i<type.numTrials; i++) {
					checkpoints.add(i);
				}
				// Next, we randomly shuffle this list of indexes.
				Collections.shuffle(checkpoints);
				/*
				 * Then we take a subset of the shuffled indexes that has a size one less than half the total
				 * number of indexes. These will be the indexes of our "unexpected" trials. Since the first
				 * trial is always unexpected, you can undoubtedly see that there will be an equal number of 
				 * expected and unexpected trials. (Or you can just take my word for it.)
				 */
				checkpoints = (ArrayList<Integer>) checkpoints.subList(0, (type.numTrials/2)-1);
				/*
				 * Now we build our list of trials. For each trial which we have designated as "unexpected",
				 * we must ensure that it does not appear on the same half of the quadrant at the trial before it.
				 */
				for (int i=0; i<type.numTrials; i++) {
					quadrant = randomizer.nextInt(3);
					if (i > 0) {
						int prevQuadrant = trials.get(i-1).position.value;
						/*
						 * TODO: This loop will need to be changed if it turns out the point of "unexpected" 
						 * trials is that the same button will not be pressed twice in a row.
						 */
						while (quadrant == prevQuadrant || (checkpoints.contains(i) && (quadrant < 2 && prevQuadrant < 2) || (quadrant >=2 && prevQuadrant >= 2))) {
							quadrant = randomizer.nextInt(3);
						}
					}
					trials.add(new Trial(quadrant));
				}
			}
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
		}
		// The position of this particular trial
		public Position position;
		// The letter which appears during this trial
		public char letter;
		// The number which appears during this trial
		public char number;
		// The correct response to this trial
		public char correctReponse;
		
		// The letter which was generated most recently (so it won't be used in the next trial to be generated)
		// Starts as '@', which is why that may not be included in the list of letters at the top of this file.
		private static char lastLetter = '@';
		// The number which was generated most recently (so it won't be used in the next trial to be generated)
		// Starts as '@', which may not be included in the list of numbers at the top of this file (not that you would do that anyway).
		private static char lastNumber = '@';
		
		/**
		 * This creates a trial with the specified position.
		 * @param quadrant : A number 0-3 where 0 = upper left, 1 = upper right, 2 = lower right, 3 = lower left
		 */
		public Trial(int quadrant) {
			// Randomly decide if the trial will contain a vowel or consonant.
			boolean hasVowel = flip();
			char[] letters = (hasVowel ? VOWELS : CONSONANTS);
			// Randomly decide if the trial will contain an odd or even number.
			boolean hasOdd = flip();
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
	}
}
