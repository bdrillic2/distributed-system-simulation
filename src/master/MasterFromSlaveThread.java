package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MasterFromSlaveThread extends Thread {

	private BufferedReader reader;
	private IntegerWrapper numSlaveJobs;
	private String slaveType;
	private PrintWriter writeClient;

	public MasterFromSlaveThread(String slaveType, BufferedReader reader, IntegerWrapper numSlaveJobs, PrintWriter writeClient) {
		this.reader = reader;
		this.numSlaveJobs = numSlaveJobs;
		this.slaveType = slaveType;
		this.writeClient = writeClient;
	}

	public void run() {
		System.out.println("In MasterFromSlaveThread");

		
		// Continuously read in confirmations from slave
		boolean keepGoing = true;
		String isComplete = null;
		
		while(keepGoing) {
			try {
				isComplete = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			// decrement the number of jobs the given slave has since one job has completed
			numSlaveJobs.decrement();
			
			//get job id of completed job from isComplete string
			String[] splitExpressions = isComplete.split(" ");
			String id = splitExpressions[1];
			
			// Create MasterToClientThread to alert client that job is completed
			Thread writer = new MasterToClientThread(writeClient, id, slaveType);
			writer.start();
			
		}
		System.out.println("SLAVE " + slaveType + " SAYS: " + isComplete);
	}
}
