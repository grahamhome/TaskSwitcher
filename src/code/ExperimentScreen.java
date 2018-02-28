package code;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import code.ActivityController.Activity;
import code.TaskData.End;
import code.TaskData.Instructions;
import code.TaskData.Message;
import code.TaskData.Pause;
import code.TaskData.SubTask;
import code.TaskData.Trial;
import code.TaskData.Trial.Position;
import code.TaskData.Trial.Result;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	public static final Color FOREGROUND = Color.WHITE;
	private static final double FONT_SIZE = 75;
	
	private static HBox box;
	
	private static boolean hasStarted = false;
	
	private static VisualTrial currentVisualTrial;
	
	private static int currentTrialIndex = 0; // TODO: remove
	
	private static synchronized VisualTrial getCurrentVisualTrial() {
		return currentVisualTrial;
	}
	
	private static synchronized void setCurrentVisualTrial(VisualTrial trial) {
		currentTrialIndex++; // TODO: remove
		currentVisualTrial = trial;
	}
	
	private static AtomicBoolean listening = new AtomicBoolean(false);
	private static TrialController trialController = new TrialController();
	
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
	}
	
	private static void startInputListener() {
		stage.getScene().setOnKeyPressed(e -> {
			boolean wasListening;
			synchronized (listening) {
				if ((wasListening = listening.getAndSet(false)) && (e.getCode().equals(TaskData.LEFT_KEY) || e.getCode().equals(TaskData.RIGHT_KEY))) {
					System.out.println("Keypress for trial " + currentTrialIndex + " was valid"); // TODO: remove
					try {
						switch (getCurrentVisualTrial().end(e.getCode())) {
							case CORRECT:
								System.out.println("Keypress was correct"); // TODO: remove
								Thread.sleep(150);
								runNextTask();
								break;
							case INCORRECT_PRACTICE:
								System.out.println("practice trial failed"); // TODO: remove
								// TODO: play sound here
							case INCORRECT_EXPERIMENTAL:
								System.out.println("Keypress was incorrect"); // TODO: remove
								Thread.sleep(1500);
								runNextTask();
								break;
							case ALREADY_SET:
							case MISSED_DEADLINE:
								break;
						}
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				} else {
					if (wasListening) { listening.set(true); }
					System.out.println("Keypress for trial " + currentTrialIndex + " was invalid"); // TODO: remove
				}
			}
		});
	}
	
	private static void stopInputListener() {
		listening.set(false);
		stage.getScene().setOnKeyPressed(null);
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
	
	// Starts an experiment from the beginning or the current location (if previously paused)
	public static void startExperiment() {
		if (!hasStarted) {
			TaskData.createExperiment((StartScreen.selectedType == 2));
			hasStarted = true;
		}
		trialController.start();
		startInputListener();
		runNextTask();
	}
	
	// Pauses an experiment
	public static void pauseExperiment() {
		stopInputListener();
		trialController.stop();
	}
	
	private static void runNextTask() {
		System.out.println("Starting a task"); // TODO: remove
		SubTask task = TaskData.next();
		System.out.println(task);
		if (task instanceof Trial) {
			System.out.println("Sending new trial to render function"); // TODO: remove
			renderTrial((Trial)task);
		} else if (task instanceof Message) {
			System.out.println("Showing message"); // TODO: remove
			pauseExperiment();
			ActivityController.message = ((Message)task).message;
			ActivityController.start(Activity.MESSAGE, stage);
		} else if (task instanceof Instructions) {
			System.out.println("Showing instructions"); // TODO: remove
			// TODO: show instructions screen here
			runNextTask();
		} else if (task instanceof Pause) {
			System.out.println("Pausing"); // TODO: remove
			pauseExperiment();
			ActivityController.start(Activity.PAUSE, stage);
		} else if (task instanceof End) {
			System.out.println("Experiment is over"); // TODO: remove
			System.exit(0);
		}
	}
	
	public static void renderTrial(Trial trial) {
		/**
		 * We need an instance of the trial (and trial view) that will not change if the key listener
		 * switches out the current trial. That's why this reference is used in the
		 * service below rather than the thread-safe method calls.
		 */
		VisualTrial activeTrial = new VisualTrial(trial);
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
							runNextTask();
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
			INCORRECT_EXPERIMENTAL,
			INCORRECT_PRACTICE,
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
					setCurrentVisualTrial(VisualTrial.this);
					trialController.showTrial.set(true);
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
					return (trial.isPractice() ? EndAttemptResult.INCORRECT_PRACTICE : EndAttemptResult.INCORRECT_EXPERIMENTAL);
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
	
	/**
	 * A custom AnimationTimer which is responsible for showing trials
	 * and enabling the keypress listener.
	 *
	 */
	private static class TrialController extends AnimationTimer {
		private boolean needListener = false;
		
		
		// Switches to show and hide the current trial
		private AtomicBoolean showTrial = new AtomicBoolean(false);

		@Override
		public void handle(long now) {
			if (showTrial.getAndSet(false)) {
				System.out.println("Showing trial " + currentTrialIndex);
				getCurrentVisualTrial().trialView.setVisible(true);
				needListener = true;
			} else if (needListener) {
				listening.set(true);
				needListener = false;
			}
		}
	}
}
