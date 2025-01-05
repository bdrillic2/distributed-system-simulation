package distributed_system_simulation;

import java.io.BufferedReader;

public class MasterFromSlaveThread extends Thread {

	private BufferedReader reader;

	public MasterFromSlaveThread(BufferedReader reader) {
		this.reader = reader;
	}

	public void run() {
		// Read in confirmation from slave
		String isComplete = reader.readLine();
		System.out.println(isComplete);
	}
}
