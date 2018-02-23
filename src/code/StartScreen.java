package code;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import code.ActivityController.Activity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This class creates the program configuration screen which allows the experimenter 
 * to enter the subject number and select the experiment type.
 * @author Graham Home <gmh5970@g.rit.edu>
 */
public class StartScreen extends HBox {
	private static Stage stage;
	private static StartScreen instance;
	public static int selectedType;
	
	private StartScreen() {
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		Label title = new Label("Configuration");
		title.setFont(Font.font("System", FontWeight.BOLD, 20));
		title.setMinWidth(Region.USE_PREF_SIZE);
		title.setAlignment(Pos.CENTER_LEFT);
		HBox titleBox = new HBox(title);
		titleBox.setMaxHeight(Region.USE_PREF_SIZE);
		titleBox.setAlignment(Pos.CENTER);
		Label numberLabel = new Label("Subject Number:");
		numberLabel.setMinWidth(Region.USE_PREF_SIZE);
		numberLabel.setAlignment(Pos.CENTER_LEFT);
		TextField numberField = new TextField();
		numberField.setMinWidth(100);
		numberField.setMaxWidth(100);
		HBox numberBox = new HBox(10, numberLabel, numberField);
		numberBox.setMaxHeight(Region.USE_PREF_SIZE);
		numberBox.setAlignment(Pos.CENTER_LEFT);
		Label typeLabel = new Label("Experiment Type:");
		typeLabel.setMinWidth(Region.USE_PREF_SIZE);
		typeLabel.setAlignment(Pos.CENTER);
		ToggleGroup experimentType = new ToggleGroup();
		RadioButton type1 = new RadioButton("1");
		type1.setUserData(1);
		type1.setToggleGroup(experimentType);
		type1.setSelected(true);
		RadioButton type2 = new RadioButton("2");
		type2.setUserData(2);
		type2.setToggleGroup(experimentType);
		HBox typeBox = new HBox(10, typeLabel, type1, type2);
		Button enterButton = new Button("Begin Experiment");
		enterButton.setMinWidth(Region.USE_PREF_SIZE);
		HBox buttonBox = new HBox(enterButton);
		buttonBox.setMaxHeight(Region.USE_PREF_SIZE);
		buttonBox.setAlignment(Pos.CENTER);
		VBox layoutBox = new VBox(20, titleBox, numberBox, typeBox, buttonBox);
		layoutBox.setPadding(new Insets(0,10,10,10));
		layoutBox.setAlignment(Pos.CENTER);
		layoutBox.setMaxHeight(200);
		layoutBox.setMinHeight(200);
		layoutBox.setMaxWidth(250);
		layoutBox.setMinWidth(250);
		layoutBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		layoutBox.relocate((stage.getWidth()-250)/2, (stage.getHeight()-250)/2);
		enterButton.setOnMouseReleased((e) -> {
			selectedType = Integer.parseInt(experimentType.getSelectedToggle().getUserData().toString());
			ActivityController.start(Activity.EXPERIMENT, stage);
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.schedule(new Runnable() {
				
				@Override
				public void run() {
					System.exit(0);
					
				}
			}, 10000, TimeUnit.MILLISECONDS);
			
		});
		getChildren().add(layoutBox);
	};

	public static HBox getInstance(Stage primaryStage) {
		stage = primaryStage;
		selectedType = 1;
		return (instance == null ? instance = new StartScreen() : instance);
	}

}
