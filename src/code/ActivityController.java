package code;

import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ActivityController {
	
	public enum Activity {
		START,
		INSTRUCTIONS,
		EXPERIMENT,
		EXIT,
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
