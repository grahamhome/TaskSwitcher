package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Config {
	
	public static int practiceTrialCount = 0;
	public static int experimentalTrialCount = 0;
	public static int breakDuration = 0;
	
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
			PrintWriter outputWriter = new PrintWriter(new File(TaskSwitcher.class.getResource("/config.csv").getPath()));
				outputWriter.write(practiceTrialCount + "," + experimentalTrialCount + "," + breakDuration);
			outputWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
