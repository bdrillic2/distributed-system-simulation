package distributed_system_simulation;

import java.io.*;

//reads in confirmation from master
public class ClientFromMasterThread extends Thread {

	private BufferedReader reader;
	private String type;
	private int id;

	public ClientFromMasterThread(BufferedReader reader, String type, int id) {
		this.reader = reader;
		this.type = type;
		this.id = id;
	}

	public void run() {
		String confirmation = reader.readLine();
		if (confirmation != null) {
			System.out.println("Received confirmation from master: " + confirmation);
		} else {
			System.err.println("No response from master for job: " + type + " " + id);
		}
	}
}
