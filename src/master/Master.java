package master;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Master {

	public static void main(String[] args) throws IOException {

		// Hard code in port number if necessary:
		args = new String[] { "30121" };
    	
		if (args.length != 1) {
			System.err.println("Usage: java clientCode <host name> <port number>");
			System.exit(1);
		}
		int portNumber = Integer.parseInt(args[0]);

		try (
				ServerSocket serverSocket = new ServerSocket(portNumber);
				// Establish connections with client and slaves
				Socket clientSocket = serverSocket.accept();
				Socket slaveSocket1 = serverSocket.accept();
				Socket slaveSocket2 = serverSocket.accept();
				
				// Set up BufferedReaders
				BufferedReader readClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader readSlaveA = new BufferedReader(new InputStreamReader(slaveSocket1.getInputStream()));
				BufferedReader readSlaveB = new BufferedReader(new InputStreamReader(slaveSocket2.getInputStream()));
				
				// Writes to client
				PrintWriter writeClient = new PrintWriter(clientSocket.getOutputStream(), true);
				// Writes to slave A
				PrintWriter writeSlaveA = new PrintWriter(slaveSocket1.getOutputStream(), true);
				// Writes to slave B
				PrintWriter writeSlaveB = new PrintWriter(slaveSocket2.getOutputStream(), true);
			) {
    		
			System.out.println("In Master - connections with clients set up");

			Queue<String> jobQueue = new LinkedList<>();
			
			// initialize thread that reads in job from client and adds them to job queue
			Thread jobListener = new MasterFromClientThread(readClient, jobQueue);
			jobListener.start();
			
			// Strings to hold job in front of queue and job's info and determine which slave to send it to
			String job;
		    char jobType;
			String ID;
			
			// Use custom IntegerWrappers to keep track of amount of current jobs for each slave
			// ensuring that the number can be updated in a thread safe way within the thread objects themselves
			IntegerWrapper slaveAJobs = new IntegerWrapper(0);
			IntegerWrapper slaveBJobs = new IntegerWrapper(0);

			// Initialize chosenWriter arbitrarily to workaround Eclipse technical requirements
			PrintWriter chosenWriter = writeSlaveA;
			
			// Initialize MasterFromSlaveThread threads to listen out for job confirmations from slaves
			MasterFromSlaveThread slaveAConfirmation = new MasterFromSlaveThread("A", readSlaveA, slaveAJobs, writeClient);
			slaveAConfirmation.start();
			MasterFromSlaveThread slaveBConfirmation = new MasterFromSlaveThread("B", readSlaveB, slaveBJobs, writeClient);
			slaveBConfirmation.start();

			do {
				// remove first job from queue using synchronized to ensure no data corruption
				synchronized(jobQueue) {
					job = jobQueue.poll();
				}
				
				// Master determines job type (first character) and ID number (second word)
				jobType = job.charAt(0);
				ID = job.substring(2);
				
				String slaveType = null;
				
				// Master determines which slave to assign job to
				// If the slave optimized for job has 5 current jobs, and other slave has less
				// than 5, send to not optimized slave. If both have 5+, send to optimized slave.
				switch(jobType) {
				case 'A':
					if (slaveAJobs.getValue() > 5 && slaveBJobs.getValue() < 5) {
						chosenWriter = writeSlaveB;
						slaveType = "B";
						slaveBJobs.increment(); // Increment job counter for Slave B
					} else {
						chosenWriter = writeSlaveA;
						slaveType = "A";
						slaveAJobs.increment(); // Increment job counter for Slave A
					}
					break;
				case 'B':
					if (slaveBJobs.getValue() > 5 && slaveAJobs.getValue() < 5) {
						chosenWriter = writeSlaveA;
						slaveType = "A";
						slaveAJobs.increment(); // Increment job counter for Slave A
					} else {
						chosenWriter = writeSlaveB;
						slaveType = "B";
						slaveBJobs.increment(); // Increment job counter for Slave B
					}
					break;
				}

				// Send job to slave
				MasterToSlaveThread assignment = new MasterToSlaveThread(chosenWriter, job, slaveType);
				assignment.start();

			} while(!jobQueue.isEmpty());
		}
	}

}
