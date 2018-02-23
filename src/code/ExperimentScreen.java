package code;

import java.util.ArrayList;
import java.util.Random;

import code.TaskData.Block;
import code.TaskData.Trial;
import code.TaskData.Trial.Position;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

/**
 * This class creates the experiment screen which 
 * conducts the task-switching experiment.
 * @author Graham Home <gmh5970@g.rit.edu>
 *
 */
public class ExperimentScreen extends VBox {
	private static Stage stage;
	private static ExperimentScreen instance;
	private Group gridGroup;
	private double gridOffsetX, gridOffsetY, gridSize;;
	private static final double LINE_WIDTH = 5;
	private static final Color FOREGROUND = Color.WHITE;
	private static final double FONT_SIZE = 75;
	
	HBox box;
	
	private ExperimentScreen() {
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		gridGroup = new Group();
		gridSize = (stage.getHeight()*0.75);
		gridOffsetX = (stage.getWidth()-gridSize)/2;
		gridOffsetY = 50;
		Rectangle grid = new Rectangle(gridOffsetX, gridOffsetY, gridSize, gridSize);
		grid.setStroke(FOREGROUND);
		grid.setStrokeWidth(LINE_WIDTH);
		Line xAxis = new Line(gridOffsetX, gridOffsetY+(gridSize/2), gridOffsetX+(gridSize), gridOffsetY+(gridSize/2));
		xAxis.setStroke(FOREGROUND);
		xAxis.setStrokeWidth(LINE_WIDTH);
		Line yAxis = new Line(gridOffsetX+(gridSize/2), gridOffsetY, gridOffsetX+(gridSize/2), gridOffsetY+gridSize);
		yAxis.setStroke(FOREGROUND);
		yAxis.setStrokeWidth(LINE_WIDTH);
		gridGroup.getChildren().addAll(grid, xAxis, yAxis);
		box = new HBox();
		box.setAlignment(Pos.CENTER_LEFT);
		addIndicatorLabels();
		addControlLabels();
		box.getChildren().add(gridGroup);
		getChildren().addAll(new Rectangle(stage.getWidth(), gridOffsetY), box);
		runExperiment();
	}
	
	private void addIndicatorLabels() {
		Label topLabel = new Label("Letter");
		topLabel.setFont(Font.font("System", FontWeight.BOLD, 30));
		topLabel.setTextFill(FOREGROUND);
		topLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		topLabel.setMaxHeight(60);
		topLabel.setMinHeight(60);
		topLabel.setTextAlignment(TextAlignment.CENTER);
		Label bottomLabel = new Label("Number");
		bottomLabel.setFont(Font.font("System", FontWeight.BOLD, 30));
		bottomLabel.setTextFill(FOREGROUND);
		bottomLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		bottomLabel.setMaxHeight(60);
		bottomLabel.setMinHeight(60);
		bottomLabel.setTextAlignment(TextAlignment.RIGHT);
		Line indicatorLine = new Line(30, gridSize/2, gridOffsetX-60, gridSize/2);
		indicatorLine.setStroke(FOREGROUND);
		indicatorLine.setStrokeWidth(LINE_WIDTH/2);
		box.getChildren().add(new VBox(
				new Rectangle(gridOffsetX, ((gridSize/2)-60)+LINE_WIDTH/4), 
				new HBox(new Line(30, gridSize/2, ((gridOffsetX-60)/2), gridSize/2), topLabel),
				new HBox(new Line(0, gridSize/2, 30, gridSize/2), indicatorLine),
				new HBox(new Line(30, gridSize/2, ((gridOffsetX-60)/2)-15, gridSize/2), bottomLabel)));
	}
	
	private void addControlLabels() {
		double width = 400;
		VBox controlsBox = new VBox(5);
		controlsBox.setPadding(new Insets(5,10,5,10));
		controlsBox.setBorder(new Border(new BorderStroke(FOREGROUND, BorderStrokeStyle.SOLID, null, new BorderWidths(LINE_WIDTH/2))));
		controlsBox.setMinWidth(width);
		controlsBox.setMaxWidth(width);
		controlsBox.relocate((stage.getWidth()-width)/2, gridOffsetY+gridSize+30);
		String leftKeyLabelText, rightKeyLabelText;
		if (TaskData.VOWEL_KEY == TaskData.LEFT) {
			leftKeyLabelText = " - Vowel or ";
			rightKeyLabelText = " - Consonant or ";
		} else {
			rightKeyLabelText = "Vowel or ";
			leftKeyLabelText = "Consonant or ";
		}
		if (TaskData.ODD_KEY == TaskData.LEFT) {
			leftKeyLabelText += "Odd";
			rightKeyLabelText += "Even";
		} else {
			rightKeyLabelText += "Odd";
			leftKeyLabelText += "Even";
		}
		HBox leftKeyControls = new HBox(5);
		leftKeyControls.setAlignment(Pos.CENTER_LEFT);
		leftKeyControls.setMinWidth(width-10);
		Label leftKey = new Label(String.valueOf(TaskData.LEFT).toUpperCase());
		leftKey.setFont(Font.font("System", FontWeight.BOLD, 30));
		leftKey.setTextFill(FOREGROUND);
		Label leftKeyAction = new Label(leftKeyLabelText);
		leftKeyAction.setMinWidth(Region.USE_PREF_SIZE);
		leftKeyAction.setFont(Font.font("System", FontWeight.NORMAL, 30));
		leftKeyAction.setTextFill(FOREGROUND);
		leftKeyControls.getChildren().addAll(leftKey, leftKeyAction);
		HBox rightKeyControls = new HBox(5);
		rightKeyControls.setAlignment(Pos.CENTER_LEFT);
		rightKeyControls.setMinWidth(width-10);
		Label rightKey = new Label(String.valueOf(TaskData.RIGHT).toUpperCase());
		rightKey.setFont(Font.font("System", FontWeight.BOLD, 30));
		rightKey.setTextFill(FOREGROUND);
		Label rightKeyAction = new Label(rightKeyLabelText);
		rightKeyAction.setMinWidth(Region.USE_PREF_SIZE);
		rightKeyAction.setFont(Font.font("System", FontWeight.NORMAL, 30));
		rightKeyAction.setTextFill(FOREGROUND);
		rightKeyControls.getChildren().addAll(rightKey, rightKeyAction);
		controlsBox.getChildren().addAll(leftKeyControls, rightKeyControls);
		gridGroup.getChildren().add(controlsBox);
	}
	
	private void runExperiment() {
		ArrayList<Block> blocks = TaskData.createExperiment((StartScreen.selectedType == 2));
		Block first = blocks.get(0);
		for (int i=0; i<1; i++) {
			renderTrial(first.trials.get(i));
		}
	}
	
	private void renderTrial(Trial trial) {
		Text text = new Text(String.valueOf(trial.letter) + String.valueOf(trial.number));
		text.setFont(Font.font("System", FontWeight.BOLD, FONT_SIZE));
		text.setFill(FOREGROUND);
		text.setBoundsType(TextBoundsType.VISUAL);
		if (trial.position.equals(Position.UPPER_LEFT) || trial.position.equals(Position.LOWER_LEFT)) {
			text.setX(gridOffsetX+((gridSize-LINE_WIDTH)/4));
		} else {
			text.setX(gridOffsetX+(((gridSize-LINE_WIDTH)/4)*3));
		}
		if (trial.position.equals(Position.UPPER_LEFT) || trial.position.equals(Position.UPPER_RIGHT)) {
			text.setY(gridOffsetY+((gridSize-LINE_WIDTH)/4));
		} else {
			text.setY(gridOffsetY+(((gridSize-LINE_WIDTH)/4)*3));
		}
		gridGroup.getChildren().add(text);
		text.relocate(text.getX()-(text.getLayoutBounds().getWidth()/2), text.getY()-((text.getLayoutBounds().getMaxY()-text.getLayoutBounds().getMinY())/2));
	}

	public static VBox getInstance(Stage primaryStage) {
		stage = primaryStage;
		return (instance == null ? instance = new ExperimentScreen() : instance);
	}
}
