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

		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket1 = serverSocket.accept();
				Socket clientSocket2 = serverSocket.accept();
				Socket clientSocket3 = serverSocket.accept();
				// Writes to client
				PrintWriter out1 = createWriter(clientSocket1);
				// Writes to slave A
				PrintWriter out2 = createWriter(clientSocket2);
				// Writes to slave B
				PrintWriter out3 = createWriter(clientSocket3);
				// Reads from client
				BufferedReader in1 = createReader(clientSocket1);
				// Reads from Slave A
				BufferedReader in2 = createReader(clientSocket2);
				// Reads from Slave B
				BufferedReader in3 = createReader(clientSocket3);) {
			// Counters to keep track of amount of current jobs for each slave
			int slaveAJobs = 0;
			int slaveBJobs = 0;
			String job;
			String chosenSlave = null;
			String jobType;
			String ID;

			PrintWriter chosenWriter;

			do {
				// reads in job from client
				Thread listener1 = new MasterFromClientThread(in1, job);
				listener1.start();
				if (job == null || job.trim().isEmpty()) {
					System.err.println("Invalid job received from client. Ending connection.");
					break;
				}

				// Master determines job type (first character) and ID number (second word)
				jobType = job.substring(0, job.indexOf(" "));
				ID = job.split(" ")[1];

				// Master determines which slave to assign job to
				// If the slave optimized for job has 5 current jobs, and other slave has less
				// than 5, send to not optimized slave. If both hav 5+, send to optimized slave.
				if (jobType.equalsIgnoreCase("A")) {
					if (slaveAJobs < 5) {
						chosenWriter = out2;
						chosenSlave = "A";
						slaveAJobs++; // Increment job counter for Slave A
					} else if (slaveBJobs < 5) {
						chosenWriter = out3;
						chosenSlave = "B";
						slaveBJobs++; // Increment job counter for Slave B
					} else {
						chosenWriter = out2;
						chosenSlave = "A";
						slaveAJobs++; // Increment job counter for Slave A
					}
				} else if (jobType.equalsIgnoreCase("B")) {
					if (slaveBJobs < 5) {
						chosenWriter = out3;
						chosenSlave = "B";
						slaveBJobs++; // Increment job counter for Slave B
					} else if (slaveAJobs < 5) {
						chosenWriter = out2;
						chosenSlave = "A";
						slaveAJobs++; // Increment job counter for Slave A
					} else {
						chosenWriter = out3;
						chosenSlave = "B";
						slaveBJobs++; // Increment job counter for Slave B
					}
				}

				// Send job to slave
				MasterToSlaveThread assignment = new MasterToSlaveThread(chosenWriter, job);
				assignment.start();

				BufferedReader chosenReader;

				// Master reads in job completion confirmation from slave
				if ("A".equals(chosenSlave)) {
					chosenReader = in2;
					slaveAJobs--; // Decrement job counter for Slave A
				} else {
					chosenReader = in3;
					slaveBJobs--; // Decrement job counter for Slave B
				}

				MasterFromSlaveThread confirmation = new MasterFromSlaveThread(chosenReader);
				confirmation.start();

				// Master alerts client that job is completed
				Thread writer = new MasterToClientThread(out1, id, chosenSlave);
				writer.start();
			} while (job != null);
		}
	}

	private static PrintWriter createWriter(Socket socket) throws IOException {
		return new PrintWriter(socket.getOutputStream(), true);
	}

	private static BufferedReader createReader(Socket socket) throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

}
