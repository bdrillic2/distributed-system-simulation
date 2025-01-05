package distributed_system_simulation;

import java.util.concurrent.TimeUnit;

public class SlaveFromMasterThread extends Thread {

	private BufferedReader in;

	public SlaveFromMasterThread(BufferedReader in) {
		this.in = in;
	}

	// Slave reads in job from master
	public void run() {
		String job = in.readLine();
		if (job.equals("B")) {
			TimeUnit.SECONDS.sleep(2);
		} else if (job.equals("A")) {
			TimeUnit.SECONDS.sleep(10);
		} else {
			throw new UnknownJobException();
		}
	}
}
