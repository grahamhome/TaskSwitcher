package code;

import java.net.URISyntaxException;

import code.ActivityController.Activity;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * This class creates a screen which appears between trial blocks 
 * to display a message to the subject.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class InstructionsScreen extends HBox {
	
	private static Stage stage;
	private static InstructionsScreen instance;
	private InstructionsScreen() throws URISyntaxException {
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		WebView messageView = new WebView();
		messageView.getEngine().loadContent(Strings.INSTRUCTIONS + "<img style='width:100%; height:auto' src='" + this.getClass().getClassLoader().getResource("instructions.png").toURI().toString() + "'/>");
		messageView.setMaxHeight(stage.getHeight()-100);
		messageView.setMinHeight(stage.getHeight()-100);
		VBox layoutBox = new VBox(20, messageView);
		layoutBox.setAlignment(Pos.CENTER);
		layoutBox.setMaxHeight(stage.getHeight()-100);
		layoutBox.setMinHeight(stage.getHeight()-100);
		layoutBox.setMaxWidth((stage.getHeight()-100)*0.77);
		layoutBox.setMinWidth((stage.getHeight()-100)*0.77);
		layoutBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		layoutBox.relocate((stage.getWidth()-((stage.getHeight()-100)*0.77))/2, 50);
		stage.getScene().setOnKeyPressed((e) -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				ActivityController.start(Activity.EXPERIMENT, stage);
			}
		});
		getChildren().add(layoutBox);
	}
	
	public static HBox getInstance(Stage primaryStage) {
		stage = primaryStage;
		try {
			return (instance == null ? instance = new InstructionsScreen() : instance);
		} catch (Exception e) {
			return null;
		}
	}
}
