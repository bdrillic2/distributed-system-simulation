package distributed_system_simulation;

import java.io.BufferedReader;

//Thread reads job from client and writes to slave based on load balancing algorithm
public class MasterFromClientThread extends Thread {

	private BufferedReader reader;
	private String job;

	public MasterFromClientThread(BufferedReader reader, String job) {
		this.reader = reader;
		this.job = job;
	}

	@Override
	public void run() {
		// Master reads in job from client - clientSocket1
		job = reader.readLine();
	}

}
