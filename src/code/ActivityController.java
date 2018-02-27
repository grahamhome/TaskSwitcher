package code;

import javafx.stage.Stage;

public class ActivityController {
	
	public enum Activity {
		START,
		EXPERIMENT,
		;
	}
	
	public static void start(Activity activity, Stage stage) {
		switch (activity) {
		case START:
			stage.getScene().setRoot(StartScreen.getInstance(stage));
			break;
		case EXPERIMENT:
			stage.getScene().setRoot(ExperimentScreen.getInstance(stage));
		}
	}
}
