package code;

import javafx.stage.Stage;

public class ActivityController {
	
	public enum Activity {
		START,
		EXPERIMENT,
		MESSAGE,
		;
	}
	
	// The message which is currently being displayed (if any)
	public static String message;
	
	public static void start(Activity activity, Stage stage) {
		switch (activity) {
		case START:
			stage.getScene().setRoot(StartScreen.getInstance(stage));
			break;
		case EXPERIMENT:
			stage.getScene().setRoot(ExperimentScreen.getInstance(stage));
			ExperimentScreen.startExperiment();
			break;
		case MESSAGE:
			stage.getScene().setRoot(new MessageScreen(stage, message));
			break;
		}
	}
}
