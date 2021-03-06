package code;

import javafx.application.Platform;
import javafx.stage.Stage;

public class ActivityController {
	
	public enum Activity {
		START,
		INSTRUCTIONS,
		EXPERIMENT,
		MESSAGE,
		PAUSE,
		COUNTDOWN,
		;
	}
	
	// The message which is currently being displayed (if any)
	public static String message;
	
	public static void start(Activity activity, Stage stage) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch (activity) {
				case START:
					stage.getScene().setRoot(StartScreen.getInstance(stage));
					break;
				case INSTRUCTIONS:
					stage.getScene().setRoot(InstructionsScreen.getInstance(stage));
					break;
				case EXPERIMENT:
					stage.getScene().setRoot(ExperimentScreen.getInstance(stage));
					ExperimentScreen.getInstance(stage).startExperiment();
					break;
				case MESSAGE:
					stage.getScene().setRoot(new MessageScreen(stage, message));
					break;
				case PAUSE:
					stage.getScene().setRoot(new BreakScreen(stage));
					break;
				case COUNTDOWN:
					stage.getScene().setRoot(new CountdownScreen(stage));
					break;
				}
			}
		});
	}
}
