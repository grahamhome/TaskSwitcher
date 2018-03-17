package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javafx.stage.Stage;

public class Config {
	
	public static int practiceTrialCount = 40;
	public static int experimentalTrialCount = 200;
	public static int breakDuration = 120000;
	
	public static void load() {
		try {
			InputStreamReader reader = new InputStreamReader(TaskSwitcher.class.getResourceAsStream("/config.csv"));
			BufferedReader bReader = new BufferedReader(reader);
			String[] vars = bReader.readLine().split(",");
			practiceTrialCount = Integer.parseInt(vars[0]);
			experimentalTrialCount = Integer.parseInt(vars[1]);
			breakDuration = Integer.parseInt(vars[2]);
			bReader.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void save() {
		try {
			PrintWriter outputWriter = new PrintWriter(new File(Config.class.getResource("/config.csv").toString()));
				outputWriter.write(practiceTrialCount + "," + experimentalTrialCount + "," + breakDuration);
			outputWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
