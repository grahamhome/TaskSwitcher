package code;

/**
 * This class contains all the (lengthy) strings of the program.
 * @author Graham
 *
 */
public class Strings {
	
	private static final String STYLE = "<p style='font-family:System;font-size:18pt'>";
	private static final String END = "</p>";
	
	public static final String PRACTICE_PREDICTABLE_STARTING = STYLE + "In this section of the experiment, the character pair <u>will always begin in the top left square and rotate in a clockwise order.</u><br><br>You will be given a short series of practice trials.<br><br>Press the Enter key when you are ready to begin." + END; 
	
	public static final String PRACTICE_RANDOM_STARTING = STYLE + "In this section of the experiment, the character pair <u>will always appear in a random square.</u><br><br>You will be given a short series of practice trials.<br><br>Press the Enter key when you are ready to begin." + END;
	
	public static final String PRACTICE_ENDED_EXPERIMENT_STARTING = STYLE + "You have reached the end of the practice trials. Please ask the researcher now if you have any questions.<br><br>Press the Enter key to continue to the next series of trials." + END;
	
	public static final String EXPERIMENT_ENDED = STYLE + "You have reached the end of the experiment.<br><br>Thank you very much for your participation.<br><br>Please let the researcher know that you are done.<br><br>Press the Enter key to leave the experiment.";
	
	public static final String BREAK_MESSAGE = "Please refrain from other activities during this break.";
}
