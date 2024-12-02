package src.master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Master {

    // Counters to keep track of the current number of jobs for each slave
    private static int slaveAJobs = 0;
    private static int slaveBJobs = 0;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Master <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
            // Create a new socket for server
            ServerSocket serverSocket = new ServerSocket(portNumber)
        ) {
            System.out.println("Master server started on port " + portNumber);

            // Thread pool for managing client and slave threads
            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                // Accept client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());
                executorService.execute(() -> handleClient(clientSocket)); // New threading logic
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            // Gives access to input stream - server reads from client - inputStream allows socket to receive data
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Gives access to output stream - server writes to client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String job;
            while ((job = in.readLine()) != null) {
                System.out.println("Received job: " + job);

                // Master determines job type (first character) and ID number (second word)
                String jobType = job.substring(0, job.indexOf(" "));
                String jobId = job.split(" ")[1];

                // Synchronized block ensures shared resources are safely accessed
                synchronized (Master.class) {
                    String chosenSlave = assignJob(jobType, job, out);
                    System.out.println("Assigned job " + jobId + " to slave " + chosenSlave);
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private static String assignJob(String jobType, String job, PrintWriter clientOut) {
        Socket slaveASocket = null;
        Socket slaveBSocket = null;

        PrintWriter outSlaveA = null;
        PrintWriter outSlaveB = null;

        String chosenSlave = null;
        try {
            // Master determines which slave to assign job to
            if (jobType.equalsIgnoreCase("A")) {
                if (slaveAJobs < 5 || slaveBJobs >= 5) {
                    outSlaveA.println(job); // Send to slave A
                    slaveAJobs++;
                    chosenSlave = "A";
                } else {
                    outSlaveB.println(job); // Send to slave B
                    slaveBJobs++;
                    chosenSlave = "B";
                }
            } else if (jobType.equalsIgnoreCase("B")) {
                if (slaveBJobs < 5 || slaveAJobs >= 5) {
                    outSlaveB.println(job); // Send to slave B
                    slaveBJobs++;
                    chosenSlave = "B";
                } else {
                    outSlaveA.println(job); // Send to slave A
                    slaveAJobs++;
                    chosenSlave = "A";
                }
            }

            // The Master reads in job completion confirmation from slave
            if ("A".equals(chosenSlave)) {
                slaveAJobs--;
            } else {
                slaveBJobs--;
            }

            // The Master alerts client that job is completed
            clientOut.println("Job completed!");
        } catch (Exception e) {
            System.err.println("Error assigning job: " + e.getMessage());
        }
        return chosenSlave;
    }
}
