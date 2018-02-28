package code;

import code.ActivityController.Activity;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * This class creates a screen which appears between trial blocks 
 * to display a message to the subject.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class PauseScreen extends HBox {
	
	private Text pauseTimer = new Text(TaskData.BREAK/60000 + ":" + (TaskData.BREAK%60000 < 10 ? "0" + TaskData.BREAK%60000 : TaskData.BREAK%60000));
	Stage stage;
	
	public PauseScreen(Stage stage) {
		this.stage = stage;
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		pauseTimer.setFont(Font.font("System", FontWeight.NORMAL, 60));
		pauseTimer.setFill(ExperimentScreen.FOREGROUND);
		Text pauseMessage = new Text(Strings.PAUSE_MESSAGE);
		pauseMessage.setFont(Font.font("System", FontWeight.NORMAL, 30));
		pauseMessage.setFill(ExperimentScreen.FOREGROUND);
		pauseMessage.setBoundsType(TextBoundsType.VISUAL);
		VBox layoutBox = new VBox(20, pauseTimer, pauseMessage);
		layoutBox.setPadding(new Insets(0,10,10,10));
		layoutBox.setAlignment(Pos.CENTER);
		layoutBox.setMaxHeight(300);
		layoutBox.setMinHeight(300);
		layoutBox.setMaxWidth(600);
		layoutBox.setMinWidth(600);
		layoutBox.relocate((stage.getWidth()-600)/2, (stage.getHeight()-300)/2);
		stage.getScene().setOnKeyPressed((e) -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				ActivityController.start(Activity.EXPERIMENT, stage);
			}
		});
		getChildren().add(layoutBox);
		new Timer().start();
	}
	
	private class Timer extends AnimationTimer {
		// Remaining time in seconds
		private long remainingTime = TaskData.BREAK/1000;
		// The last time the on-screen timer was updated
		private long lastUpdateTime = 0; // TODO: This must be initialized to "now" the first time handle() is called

		@Override
		public void handle(long now) {
			if (remainingTime == 0) {
				this.stop();
				ActivityController.start(Activity.EXPERIMENT, stage); 
			} else if (lastUpdateTime == 0) {
				lastUpdateTime = now;
			} else if ((now-lastUpdateTime)/1000000000 >= 1) {
				lastUpdateTime = now;
				remainingTime--;
				pauseTimer.setText(remainingTime/60 + ":" + (remainingTime%60 < 10 ? "0" + remainingTime%60 : remainingTime%60));
			}
		}
	}
}
