package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

//Thread reads in job from client and adds it to queue to be processed
public class MasterFromClientThread extends Thread {

	private BufferedReader reader;
	private Queue<String> jobQueue;
	
	public MasterFromClientThread(BufferedReader reader, Queue<String> jobQueue) {
		this.reader = reader;
		this.jobQueue = jobQueue;
	}

	@Override
	public void run() {
		System.out.println("In MasterFromClientThread");

		boolean keepGoing = true;
		// Master continuously reads in jobs from client and adds it to job queue
		while(keepGoing) {
			try {
				String job = reader.readLine();
				System.out.println("Job " + job + " received");
				synchronized(jobQueue) {
					jobQueue.add(job);
				}
				System.out.println("Job " + job + " added to queue");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
