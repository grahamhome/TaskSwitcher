package code;

import code.ActivityController.Activity;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This is the main class of the TaskSwitcher program. 
 * It contains the main() method which starts the program.
 * @author Graham Home <gmh5970@g.rit.edu>
 *
 */
public class TaskSwitcher extends Application {
	
	private Stage stage;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.hide();
		stage.setScene(new Scene(
				new StackPane(), 
				Screen.getPrimary().getBounds().getWidth(), 
				Screen.getPrimary().getBounds().getHeight())
		);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.setFullScreen(true);
		stage.show();
		ActivityController.start(Activity.START, stage);
	}
}
