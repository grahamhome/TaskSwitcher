package code;

import code.ActivityController.Activity;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
	public static String subjectNumber;
	
	private StartScreen() {
		//Config.load();
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		Label title = new Label("Configuration");
		title.setFont(Font.font("System", FontWeight.BOLD, 20));
		title.setMinWidth(Region.USE_PREF_SIZE);
		title.setAlignment(Pos.CENTER_LEFT);
		HBox titleBox = new HBox(title);
		titleBox.setMaxHeight(Region.USE_PREF_SIZE);
		titleBox.setAlignment(Pos.CENTER);
		Label numberLabel = new Label("Participant Number:");
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
		Label practiceTrialCountLabel = new Label("Practice trials\n(multiple of 4):");
		practiceTrialCountLabel.setMinWidth(Region.USE_PREF_SIZE);
		practiceTrialCountLabel.setAlignment(Pos.CENTER_LEFT);
		TextField practiceTrialCountField = new TextField();
		practiceTrialCountField.setMinWidth(50);
		practiceTrialCountField.setMaxWidth(50);
		practiceTrialCountField.setText(String.valueOf(Config.practiceTrialCount));
		HBox practiceTrialCountBox = new HBox(10, practiceTrialCountLabel, practiceTrialCountField);
		practiceTrialCountBox.setMaxHeight(Region.USE_PREF_SIZE);
		practiceTrialCountBox.setAlignment(Pos.CENTER_LEFT);
		Label experimentTrialCountLabel = new Label("Experiment trials\n(multiple of 4):");
		experimentTrialCountLabel.setMinWidth(Region.USE_PREF_SIZE);
		experimentTrialCountLabel.setAlignment(Pos.CENTER_LEFT);
		TextField experimentTrialCountField = new TextField();
		experimentTrialCountField.setMinWidth(50);
		experimentTrialCountField.setMaxWidth(50);
		experimentTrialCountField.setText(String.valueOf(Config.experimentalTrialCount));
		HBox experimentTrialCountBox = new HBox(10, experimentTrialCountLabel, experimentTrialCountField);
		experimentTrialCountBox.setMaxHeight(Region.USE_PREF_SIZE);
		experimentTrialCountBox.setAlignment(Pos.CENTER_LEFT);
		Label breakLabel = new Label("Pause duration\n(mins) (sec):");
		breakLabel.setMinWidth(Region.USE_PREF_SIZE);
		breakLabel.setAlignment(Pos.CENTER_LEFT);
		TextField breakMinutesField = new TextField();
		breakMinutesField.setMinWidth(40);
		breakMinutesField.setMaxWidth(40);
		breakMinutesField.setText(String.valueOf(Config.breakDuration/60000));
		TextField breakSecondsField = new TextField();
		breakSecondsField.setMinWidth(40);
		breakSecondsField.setMaxWidth(40);
		breakSecondsField.setText(String.valueOf((Config.breakDuration%60000)/1000));
		HBox breakDurationBox = new HBox(10, breakLabel, breakMinutesField, breakSecondsField);
		breakDurationBox.setMaxHeight(Region.USE_PREF_SIZE);
		breakDurationBox.setAlignment(Pos.CENTER_LEFT);
		Button enterButton = new Button("Begin Experiment");
		enterButton.setMinWidth(Region.USE_PREF_SIZE);
		HBox buttonBox = new HBox(enterButton);
		buttonBox.setMaxHeight(Region.USE_PREF_SIZE);
		buttonBox.setAlignment(Pos.CENTER);
		VBox layoutBox = new VBox(20, titleBox, numberBox, typeBox, practiceTrialCountBox, experimentTrialCountBox, breakDurationBox, buttonBox);
		layoutBox.setPadding(new Insets(0,10,10,10));
		layoutBox.setAlignment(Pos.CENTER);
		layoutBox.setMaxHeight(400);
		layoutBox.setMinHeight(400);
		layoutBox.setMaxWidth(270);
		layoutBox.setMinWidth(270);
		layoutBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		layoutBox.relocate((stage.getWidth()-270)/2, (stage.getHeight()-400)/2);
		enterButton.setOnMouseClicked((e) -> {
			subjectNumber = numberField.getText();
			selectedType = Integer.parseInt(experimentType.getSelectedToggle().getUserData().toString());
			Config.practiceTrialCount = Integer.parseInt(practiceTrialCountField.getText());
			Config.experimentalTrialCount = Integer.parseInt(experimentTrialCountField.getText());
			Config.breakDuration = (Integer.parseInt(breakMinutesField.getText())*60000) + (Integer.parseInt(breakSecondsField.getText())*1000);
			//Config.save();
			// We won't be needing this anymore...
			stage.getScene().setCursor(Cursor.NONE);
			ActivityController.start(Activity.EXPERIMENT, stage);
			
		});
		getChildren().add(layoutBox);
	}

	public static HBox getInstance(Stage primaryStage) {
		stage = primaryStage;
		selectedType = 1;
		return (instance == null ? instance = new StartScreen() : instance);
	}

}
