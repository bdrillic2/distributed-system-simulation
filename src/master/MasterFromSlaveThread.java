package master;

import java.io.BufferedReader;
import java.io.IOException;

public class MasterFromSlaveThread extends Thread {

	private BufferedReader reader;

	public MasterFromSlaveThread(BufferedReader reader) {
		this.reader = reader;
	}

	public void run() {
		// Read in confirmation from slave
		String isComplete = null;
		try {
			isComplete = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(isComplete);
	}
}
