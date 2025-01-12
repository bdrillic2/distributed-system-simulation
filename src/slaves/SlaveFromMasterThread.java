package slaves;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class SlaveFromMasterThread extends Thread {

	private BufferedReader in;
	private String slaveType;
	private PrintWriter out;

	public SlaveFromMasterThread(BufferedReader in, String slaveType, PrintWriter out) {
		this.in = in;
		this.slaveType = slaveType;
		this.out = out;
	}

	// Slave reads in jobs from master
	public void run() {
		System.out.println("In SlaveFromMasterThread");
		
		String job = null;
		boolean keepGoing = true;
		
		while(keepGoing) {
			try {
				job = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// pull job type and id from job info string
			String jobType = String.valueOf(job.charAt(0));
			String id = job.substring(2);
			
			try {
				if(!jobType.equals("A") && !jobType.equals("B")) {
					throw new UnknownJobException();
				}
				if(jobType.equals(slaveType)) {
					TimeUnit.SECONDS.sleep(2);
				} else{
					TimeUnit.SECONDS.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// pass id and slaveType to master to indicate completion of job
			SlaveToMasterThread completion = new SlaveToMasterThread(out, slaveType, id);
			completion.start();
			
			System.out.println("Job " + id + " complete by Slave " + slaveType);
		}
	}
}
