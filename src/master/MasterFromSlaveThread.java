package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MasterFromSlaveThread extends Thread {

	private BufferedReader reader;
	private IntegerWrapper numSlaveJobs;
	private String slaveType;
	private PrintWriter client1Writer;
	private PrintWriter client2Writer;
	
	public MasterFromSlaveThread(String slaveType, BufferedReader reader, IntegerWrapper numSlaveJobs, PrintWriter client1Writer, PrintWriter client2Writer) {
		this.reader = reader;
		this.numSlaveJobs = numSlaveJobs;
		this.slaveType = slaveType;
		this.client1Writer = client1Writer;
		this.client2Writer = client2Writer;
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
			
			//get client number that sent in the job
			String clientNum = splitExpressions[3];
			
			PrintWriter currClientWriter;
			if(clientNum.equals("1")) {
				currClientWriter = client1Writer;
			} else {
				currClientWriter = client2Writer;
			}
			
			// Create MasterToClientThread to alert client that job is completed
			Thread writer = new MasterToClientThread(currClientWriter, id, slaveType);
			writer.start();
			
		}
		System.out.println("SLAVE " + slaveType + " SAYS: " + isComplete);
	}
}
