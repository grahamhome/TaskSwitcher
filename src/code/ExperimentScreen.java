package code;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import code.TaskData.Instructions;
import code.TaskData.Pause;
import code.TaskData.SubTask;
import code.TaskData.Trial;
import code.TaskData.Trial.Position;
import code.TaskData.Trial.Result;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
	private static Group gridGroup;
	private static double gridOffsetX, gridOffsetY, gridSize;;
	private static final double LINE_WIDTH = 5;
	private static final Color FOREGROUND = Color.WHITE;
	private static final double FONT_SIZE = 75;
	
	private HBox box;
	
	private VisualTrial currentTrial;
	
	private synchronized VisualTrial getCurrentTrial() {
		return currentTrial;
	}
	
	private synchronized void setCurrentTrial(VisualTrial trial) {
		currentTrial = trial;
	}
	
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
		createKeyListener();
		runExperiment();
	}
	
	private void createKeyListener() {
		stage.getScene().setOnKeyPressed(e -> {
			System.out.println("Got a keypress: " + e.getText()); // TODO: remove
			if (e.getCode().equals(TaskData.LEFT_KEY) || e.getCode().equals(TaskData.RIGHT_KEY)) {
				System.out.println("Keypress was valid"); // TODO: remove
				try {
					switch (getCurrentTrial().end(e.getCode())) {
						case CORRECT:
							System.out.println("Keypress was correct"); // TODO: remove
							Thread.sleep(150);
							runTask(getCurrentTrial().trial.next);
							break;
						case INCORRECT:
							System.out.println("Keypress was incorrect"); // TODO: remove
							if (getCurrentTrial().trial.isPractice()) {
								System.out.println("practice trial failed"); // TODO: remove
								// TODO: play sound here
							}
							Thread.sleep(1500);
							runTask(getCurrentTrial().trial.next);
							break;
						case ALREADY_SET:
						case MISSED_DEADLINE:
							break;
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("Keypress was invalid"); // TODO: remove
		});
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
		if (TaskData.VOWEL_KEY == TaskData.LEFT_KEY) {
			leftKeyLabelText = " - Vowel or ";
			rightKeyLabelText = " - Consonant or ";
		} else {
			rightKeyLabelText = "Vowel or ";
			leftKeyLabelText = "Consonant or ";
		}
		if (TaskData.ODD_KEY == TaskData.LEFT_KEY) {
			leftKeyLabelText += "Odd";
			rightKeyLabelText += "Even";
		} else {
			rightKeyLabelText += "Odd";
			leftKeyLabelText += "Even";
		}
		HBox leftKeyControls = new HBox(5);
		leftKeyControls.setAlignment(Pos.CENTER_LEFT);
		leftKeyControls.setMinWidth(width-10);
		Label leftKey = new Label(TaskData.LEFT_KEY_NAME);
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
		Label rightKey = new Label(TaskData.RIGHT_KEY_NAME);
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
		runTask(TaskData.createExperiment((StartScreen.selectedType == 2)));
	}
	
	private void runTask(SubTask task) {
		System.out.println("Starting a task"); // TODO: remove
		if (task == null) {
			System.out.println("Experiment is over"); // TODO: remove
			// TODO: End experiment here
		} else if (task instanceof Trial) {
			System.out.println("Sending new trial to render function"); // TODO: remove
			renderTrial((Trial)task);
		} else if (task instanceof Instructions) {
			System.out.println("Showing instructions"); // TODO: remove
			// TODO: show instructions screen here
			runTask(task.next);
		} else if (task instanceof Pause) {
			System.out.println("Pausing"); // TODO: remove
			// TODO: show pause screen here
			runTask(task.next);
		}
	}
	
	public void renderTrial(Trial trial) {
		/**
		 * We need an instance of the trial (and trial view) that will not change if the key listener
		 * switches out the current trial. That's why this reference is used in the
		 * service below rather than the thread-safe method calls.
		 */
		VisualTrial activeTrial = new VisualTrial(trial);
		setCurrentTrial(activeTrial);
		activeTrial.start();
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(new Runnable() {
			@Override
			public void run() {
				if (!activeTrial.ended.get()) {
					System.out.println("trial timed out");
					System.out.println("starting next task from service");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							activeTrial.end(null);
							runTask(activeTrial.trial.next);
						}
					});
				} else {
					System.out.println("Trial did not time out");
				}
			}
		}, TaskData.NO_INPUT_DURATION, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * This class stores a Trial object as well as its visual representation.
	 */
	public static class VisualTrial {
		
		// The potential results of attempting to end a trial
		public enum EndAttemptResult {
			MISSED_DEADLINE,
			ALREADY_SET,
			CORRECT,
			INCORRECT,
			;
		}
		
		// The Trial object.
		private Trial trial;
		// The object used to visually depict the Trial object.
		private Text trialView;
		
		// The time at which the trial appeared
		private long startTime = 0;
		// Indicates whether or not the trial has ended
		private AtomicBoolean ended = new AtomicBoolean(false);
		
		public VisualTrial(Trial trial) {
			this.trial = trial;
			createView();
		}
		
		private void createView() {
			trialView = new Text(String.valueOf(trial.letter) + String.valueOf(trial.number));
			trialView.setFont(Font.font("System", FontWeight.BOLD, FONT_SIZE));
			trialView.setFill(FOREGROUND);
			trialView.setBoundsType(TextBoundsType.VISUAL);
			if (trial.position.equals(Position.UPPER_LEFT) || trial.position.equals(Position.LOWER_LEFT)) {
				trialView.setX(gridOffsetX+((gridSize-LINE_WIDTH)/4));
			} else {
				trialView.setX(gridOffsetX+(((gridSize-LINE_WIDTH)/4)*3));
			}
			if (trial.position.equals(Position.UPPER_LEFT) || trial.position.equals(Position.UPPER_RIGHT)) {
				trialView.setY(gridOffsetY+((gridSize-LINE_WIDTH)/4));
			} else {
				trialView.setY(gridOffsetY+(((gridSize-LINE_WIDTH)/4)*3));
			}
			trialView.setVisible(false);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gridGroup.getChildren().add(trialView);
					trialView.relocate(trialView.getX()-(trialView.getLayoutBounds().getWidth()/2), trialView.getY()-((trialView.getLayoutBounds().getMaxY()-trialView.getLayoutBounds().getMinY())/2));
				}
			});
		}
		
		public synchronized void start() {
			startTime = System.currentTimeMillis();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					trialView.setVisible(true);
				}
			});
		}
		
		public synchronized EndAttemptResult end(KeyCode input) {
			long currentTime = System.currentTimeMillis();
			if (ended.get()) {
				return EndAttemptResult.ALREADY_SET;
			}
			ended.set(true);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					gridGroup.getChildren().remove(trialView);
				}
			});
			long elapsedTime = startTime - currentTime;
			if (elapsedTime > TaskData.INPUT_DEADLINE) {
				trial.result = Result.MISSED_DEADLINE;
				return EndAttemptResult.MISSED_DEADLINE;
			}
			trial.time = elapsedTime;
			if (input != null) {
				if (input.equals(trial.correctReponse)) {
					trial.result = Result.CORRECT;
					return EndAttemptResult.CORRECT;
				} else {
					trial.result = Result.INCORRECT;
					return EndAttemptResult.INCORRECT;
				}
			} else {
				return EndAttemptResult.MISSED_DEADLINE;
			}
			
		}
	}

	public static VBox getInstance(Stage primaryStage) {
		stage = primaryStage;
		return (instance == null ? instance = new ExperimentScreen() : instance);
	}
}
