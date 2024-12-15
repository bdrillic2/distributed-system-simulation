package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {
	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 2) {
			System.err.println("Usage: java clientCode <host name> <port number>");
			System.exit(1);
		}
		int portNumber = Integer.parseInt(args[0]);
		
		try (
			    //Create a new socket for server
			     ServerSocket serverSocket =
			         new ServerSocket(Integer.parseInt(args[0]));
			     //Create clientSocket - server gets input from client
			      Socket clientSocket1 = serverSocket.accept();
				  Socket clientSocket2 = serverSocket.accept();
				  Socket clientSocket3 = serverSocket.accept();
			        //Gives access to output stream - server writes to other side in socket
			       PrintWriter out1 =
			          new PrintWriter(clientSocket1.getOutputStream(), true);
				PrintWriter out2 =
				          new PrintWriter(clientSocket2.getOutputStream(), true);
				PrintWriter out3 =
				          new PrintWriter(clientSocket3.getOutputStream(), true);
			        //Gives access to input stream - server reads from client - inputStream allows socket to receive data
			       BufferedReader in1 = new BufferedReader(
			           new InputStreamReader(clientSocket1.getInputStream()));
				BufferedReader in2 = new BufferedReader(
				           new InputStreamReader(clientSocket2.getInputStream()));
				BufferedReader in3 = new BufferedReader(
				           new InputStreamReader(clientSocket3.getInputStream()));
			       ) {
			//Counters to keep track of amount of current jobs for each slave
			int slaveAJobs = 0;
			int slaveBJobs = 0;
			String job;
			
			do {
			//Master reads in job from client - clientSocket1
			job = in1.readLine();
			
			//Master determines job type (first character) and ID number (second word)
			String jobType = job.substring(0, job.indexOf(" "));
			String ID = job.split(" ")[1];
			
			//Master determines which slave to assign job to
			//If the slave optimized for job has 5 current jobs, and other slave has less than 5, send to not optimized slave.
			String chosenSlave = null;
			if (jobType.equalsIgnoreCase("A")) {
                         if (slaveAJobs < 5) {
                         out2.println(job);
                          chosenSlave = "A";
                         slaveAJobs++; // Increment job counter for Slave A
                         } else if (slaveBJobs < 5) {
                            out3.println(job);
                        chosenSlave = "B";
                        slaveBJobs++; // Increment job counter for Slave B
                          } else {
                          out2.println(job);
                    chosenSlave = "A";
                        slaveAJobs++; // Increment job counter for Slave A
                  }
                 } else if (jobType.equalsIgnoreCase("B")) {
                  if (slaveBJobs < 5) {
                   out3.println(job);
                   chosenSlave = "B";
                   slaveBJobs++; // Increment job counter for Slave B
                  } else if (slaveAJobs < 5) {
                       out2.println(job);
                     chosenSlave = "A";
                          slaveAJobs++; // Increment job counter for Slave A
                   } else {
                   out3.println(job);
                          chosenSlave = "B";
                               slaveBJobs++; // Increment job counter for Slave B
                                 }
                              }

                // Master reads in job completion confirmation from slave
              String isComplete;
           if (chosenSlave.equals("A")) {
               isComplete = in2.readLine();
             slaveAJobs--; // Decrement job counter for Slave A
           } else {
              isComplete = in3.readLine();
              slaveBJobs--; // Decrement job counter for Slave B
             }

            // Master alerts client that job is completed
              out1.println("Job " + ID + " is complete on Slave " + chosenSlave);

			
			}
			while(job != null);
		}
	}
}
