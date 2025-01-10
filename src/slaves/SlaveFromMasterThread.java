package slaves;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SlaveFromMasterThread extends Thread {

	private BufferedReader in;

	public SlaveFromMasterThread(BufferedReader in) {
		this.in = in;
	}

	// Slave reads in job from master
	public void run() {
		String job = null;
		try {
			job = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (job.equals("B")) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (job.equals("A")) {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			throw new UnknownJobException();
		}
	}
}
