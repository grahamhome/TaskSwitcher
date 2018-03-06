package code;

/**
 * This class contains all the (lengthy) strings of the program.
 * @author Graham
 *
 */
public class Strings {
	
	private static final String STYLE = "<p style='font-family:System;font-size:18pt'>";
	private static final String END = "</p>";
	
	public static final String INSTRUCTIONS = "<center>" + STYLE + "<b>Instructions</b>" + END + "</center><br>" + STYLE + "In this experiment you will be presented with a pair of characters consisting of a letter and a digit, for example G5.<br>The location of the character pair inside of a 2 x 2 matrix will determine whether you complete the letter or digit classification task.<br>If the character pair appears above the horizontal line (in one of the top two boxes), complete the letter task. If the character pair appears below the horizontal line (in one of the bottom two boxes), complete the digit task.<br><br>For the letter task, press the Z key if the letter is a vowel or the / key if the letter is a consonant.<br>For the digit task, press the Z key if the digit is odd or the / key if the digit is even.<br><br>The letter and digit will be randomly selected from the set below.<br><br>" + END + "<center>" + STYLE + "<b>Letters</b><br><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vowels&nbsp;&nbsp;&nbsp;&nbsp;Consonants</b><br>A, E, I, U&nbsp;&nbsp;&nbsp;&nbsp;G, K, M, R<br><br><b>Digits</b><br><b>Odd&nbsp;&nbsp;&nbsp;&nbsp;Even</b><br>3, 5, 7, 9&nbsp;&nbsp;&nbsp;&nbsp;2, 4, 6, 8" + END + "</center><br><br>" + STYLE + "Please respond as quickly as possible while making as few errors as possible. A short beep will sound when an error is made.<br>Examples of the task are provided below. Please ask the researcher if you have any questions about the task. You may press the Enter key when you are ready to continue." + END;
	
	public static final String PRACTICE_PREDICTABLE_STARTING = STYLE + "In this section of the experiment, the character pair <u>will always begin in the top left square and rotate in a clockwise order.</u><br><br>You will be given a short series of practice trials.<br><br>Press the Enter key when you are ready to begin." + END; 
	
	public static final String PRACTICE_RANDOM_STARTING = STYLE + "In this section of the experiment, the character pair <u>will always appear in a random square.</u><br><br>You will be given a short series of practice trials.<br><br>Press the Enter key when you are ready to begin." + END;
	
	public static final String PRACTICE_ENDED_EXPERIMENT_STARTING = STYLE + "You have reached the end of the practice trials. Please ask the researcher now if you have any questions.<br><br>Press the Enter key to continue to the next series of trials." + END;
	
	public static final String EXPERIMENT_ENDED = STYLE + "You have reached the end of the experiment.<br><br>Thank you very much for your participation.<br><br>Please let the researcher know that you are done.<br><br>Press the Enter key to leave the experiment.";
	
	public static final String BREAK_MESSAGE = "Please refrain from other activities during this break.";
}
