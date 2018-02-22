package code;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import code.TaskData.Block;

/**
 * This is the main class of the TaskSwitcher program. 
 * It contains the main() method which starts the program.
 * @author Graham Home <gmh5970@g.rit.edu>
 *
 */
public class TaskSwitcher {

	public static void main(String[] args) {
		test();
	}
	
	private static void test() {
		try {
			PrintWriter outputWriter = new PrintWriter("C:/Users/Graham/TaskSwitcherTestOutputLong.txt", "UTF-8");
			for (Block block : TaskData.createExperiment(true)) {
				outputWriter.write(block.toString());
			}
			outputWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
