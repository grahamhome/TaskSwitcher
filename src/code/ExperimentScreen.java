package code;

import java.util.ArrayList;
import java.util.Random;

import code.TaskData.Block;
import code.TaskData.Trial;
import code.TaskData.Trial.Position;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

/**
 * This class creates the experiment screen which 
 * conducts the task-switching experiment.
 * @author Graham Home <gmh5970@g.rit.edu>
 *
 */
public class ExperimentScreen extends Group {
	private static Stage stage;
	private static ExperimentScreen instance;
	private double gridOffsetX, gridOffsetY, gridSize, axisWidth;
	private static final double LINE_WIDTH = 5;
	private static final Color FOREGROUND = Color.WHITE;
	private static final double FONT_SIZE = 75;
	
	private ExperimentScreen() {
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
		getChildren().addAll(grid, xAxis, yAxis);
		runExperiment();
	}
	
	private void runExperiment() {
		ArrayList<Block> blocks = TaskData.createExperiment(new Random().nextBoolean());
		Block first = blocks.get(0);
		for (int i=0; i<4; i++) {
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
		getChildren().add(text);
		text.relocate(text.getX()-(text.getLayoutBounds().getWidth()/2), text.getY()-((text.getLayoutBounds().getMaxY()-text.getLayoutBounds().getMinY())/2));
	}

	public static Group getInstance(Stage primaryStage) {
		stage = primaryStage;
		return (instance == null ? instance = new ExperimentScreen() : instance);
	}
}
