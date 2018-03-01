package code;

import code.ActivityController.Activity;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * This class creates a screen which appears between trial blocks 
 * to display a message to the subject.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class MessageScreen extends HBox {
	
	public MessageScreen(Stage stage, String messageText) {
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		WebView messageView = new WebView();
		messageView.getEngine().loadContent(messageText);
		VBox layoutBox = new VBox(20, messageView);
		layoutBox.setPadding(new Insets(0,10,10,10));
		layoutBox.setAlignment(Pos.CENTER);
		layoutBox.setMaxHeight(300);
		layoutBox.setMinHeight(300);
		layoutBox.setMaxWidth(600);
		layoutBox.setMinWidth(600);
		layoutBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		layoutBox.relocate((stage.getWidth()-600)/2, (stage.getHeight()-300)/2);
		stage.getScene().setOnKeyPressed((e) -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				ActivityController.start(Activity.EXPERIMENT, stage);
			}
		});
		getChildren().add(layoutBox);
	}
}
