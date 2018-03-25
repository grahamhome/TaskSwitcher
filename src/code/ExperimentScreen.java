package code;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import code.ActivityController.Activity;
import code.TaskData.End;
import code.TaskData.Instructions;
import code.TaskData.Message;
import code.TaskData.Break;
import code.TaskData.Countdown;
import code.TaskData.SubTask;
import code.TaskData.Trial;
import code.TaskData.Trial.Position;
import code.TaskData.Trial.Result;
import javafx.animation.AnimationTimer;
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
	public static final Color FOREGROUND = Color.WHITE;
	private static final double FONT_SIZE = 100;
	
	private static HBox box;
	
	private static boolean hasStarted = false;
	
	private static VisualTrial currentVisualTrial = null;
	
	private static synchronized VisualTrial getCurrentVisualTrial() {
		return currentVisualTrial;
	}
	
	private static synchronized void setCurrentVisualTrial(VisualTrial trial) {
		currentVisualTrial = trial;
	}
	
	private static AtomicBoolean listening = new AtomicBoolean(false);
	private static TrialController trialController = new TrialController();
	
	private static Clip clip;
	
	private ExperimentScreen() {
		setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		setAlignment(Pos.CENTER);
		gridGroup = new Group();
		gridSize = (stage.getHeight()*0.75);
		gridOffsetX = (stage.getWidth()-gridSize)/2;
		gridOffsetY = 0;
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
		box.getChildren().add(gridGroup);
		addControlLabels();
		setVisible(false);
		getChildren().addAll(new Rectangle(stage.getWidth(), gridOffsetY), box);
		//new MediaPlayer(new Media(ExperimentScreen.class.getResource("/error.wav").toExternalForm())).play();
		URL url = this.getClass().getClassLoader().getResource("error.wav");
        
        // Get a sound clip resource.
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
	        clip.open(audioIn);
	        // Set clip to auto-rewind after each playback
	        clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType().equals(LineEvent.Type.STOP)) {
						clip.setMicrosecondPosition(0);
					}
				}
			});
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	private void startInputListener() {
		stage.getScene().setOnKeyPressed(e -> {
			long currentTime = System.currentTimeMillis();
			boolean wasListening;
			synchronized (listening) {
				if ((wasListening = listening.getAndSet(false)) && (e.getCode().equals(TaskData.LEFT_KEY) || e.getCode().equals(TaskData.RIGHT_KEY))) {
					try {
						switch (getCurrentVisualTrial().trial.end(e.getCode(), currentTime-getCurrentVisualTrial().startTime)) {
							case CORRECT:
								gridGroup.getChildren().remove(getCurrentVisualTrial().trialView);
								ScheduledExecutorService service1 = Executors.newSingleThreadScheduledExecutor();
								service1.schedule(new Runnable() {
									@Override
									public void run() {
										runNextTask();
									}
								}, TaskData.CORRECT_INPUT_PAUSE, TimeUnit.MILLISECONDS);
								break;
							case INCORRECT:
								clip.start();
								gridGroup.getChildren().remove(getCurrentVisualTrial().trialView);
								ScheduledExecutorService service2 = Executors.newSingleThreadScheduledExecutor();
								service2.schedule(new Runnable() {
									@Override
									public void run() {
										runNextTask();
									}
								}, TaskData.INCORRECT_INPUT_PAUSE, TimeUnit.MILLISECONDS);
								break;
							default:
								break;
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					if (wasListening) { listening.set(true); }
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
		double width = gridOffsetX-120;
		double height = 160;
		VBox controlsBox = new VBox(5);
		controlsBox.setPadding(new Insets(5,10,5,10));
		controlsBox.setBorder(new Border(new BorderStroke(FOREGROUND, BorderStrokeStyle.SOLID, null, new BorderWidths(LINE_WIDTH/2))));
		controlsBox.setMinWidth(width);
		controlsBox.setMaxWidth(width);
		controlsBox.setMinHeight(height);
		controlsBox.setMaxHeight(height);
		controlsBox.setAlignment(Pos.CENTER);
		Label title = new Label("Response Keys");
		title.setFont(Font.font("System", FontWeight.BOLD, 30));
		title.setTextFill(FOREGROUND);
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
		leftKeyControls.setMinWidth(width-20);
		leftKeyControls.setMaxWidth(width-20);
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
		rightKeyControls.setMinWidth(width-20);
		rightKeyControls.setMaxWidth(width-20);
		Label rightKey = new Label(TaskData.RIGHT_KEY_NAME);
		rightKey.setFont(Font.font("System", FontWeight.BOLD, 30));
		rightKey.setTextFill(FOREGROUND);
		Label rightKeyAction = new Label(rightKeyLabelText);
		rightKeyAction.setMinWidth(Region.USE_PREF_SIZE);
		rightKeyAction.setFont(Font.font("System", FontWeight.NORMAL, 30));
		rightKeyAction.setTextFill(FOREGROUND);
		rightKeyControls.getChildren().addAll(rightKey, rightKeyAction);
		controlsBox.getChildren().addAll(title, leftKeyControls, rightKeyControls);
		box.getChildren().add(new VBox(new Rectangle(width, (gridSize-height)/2), new HBox(new Rectangle(60, height), controlsBox)));
	}
	
	// Starts an experiment from the beginning or the current location (if previously paused)
	public void startExperiment() {
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
	
	private void runNextTask() {
		SubTask task = TaskData.next();
		if (task instanceof Trial) {
			renderTrial((Trial)task);
		} else if (task instanceof Message) {
			pauseExperiment();
			ActivityController.message = ((Message)task).message;
			ActivityController.start(Activity.MESSAGE, stage);
		} else if (task instanceof Instructions) {
			ActivityController.start(Activity.INSTRUCTIONS, stage);
		} else if (task instanceof Break) {
			pauseExperiment();
			ActivityController.start(Activity.PAUSE, stage);
		} else if (task instanceof Countdown) {
			pauseExperiment();
			ActivityController.start(Activity.COUNTDOWN, stage);
		} else if (task instanceof End) {
			ResultsReporter.report();
			System.exit(0);
		}
	}
	
	public void renderTrial(Trial trial) {
		/**
		 * We need an instance of the trial (and trial view) that will not change if the key listener
		 * switches out the current trial. That's why this reference is used in the
		 * service below rather than the thread-safe method calls.
		 */
		if(!isVisible()) { setVisible(true); }
		VisualTrial activeTrial = new VisualTrial(trial);
		activeTrial.start();
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.schedule(new Runnable() {
			@Override
			public void run() {
				Result result = activeTrial.trial.end(KeyCode.CANCEL, -1);
				if (result.equals(Result.NO_INPUT) || result.equals(Result.MISSED_DEADLINE)) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							clip.start();
							gridGroup.getChildren().remove(activeTrial.trialView);
							ScheduledExecutorService service2 = Executors.newSingleThreadScheduledExecutor();
							service2.schedule(new Runnable() {
								@Override
								public void run() {
									runNextTask();
								}
							}, TaskData.INCORRECT_INPUT_PAUSE, TimeUnit.MILLISECONDS);
						}
					});
				}
			}
		}, TaskData.NO_INPUT_DURATION, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * This class stores a Trial object as well as its visual representation.
	 */
	public static class VisualTrial {
		
		// The Trial object.
		private Trial trial;
		// The object used to visually depict the Trial object.
		private Text trialView;
		
		// The time at which the trial appeared
		private long startTime = 0;
		
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
			setCurrentVisualTrial(VisualTrial.this);
			trialController.showTrial.set(true);
		}
	}

	public static ExperimentScreen getInstance(Stage primaryStage) {
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
				getCurrentVisualTrial().trialView.setVisible(true);
				getCurrentVisualTrial().startTime = System.currentTimeMillis();
				needListener = true;
			} else if (needListener) {
				listening.set(true);
				needListener = false;
			}
		}
	}
}
