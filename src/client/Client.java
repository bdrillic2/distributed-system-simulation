package client;
import java.io.*;
import java.net.*;
import java.util.Random;


public class Client {
    public static void main(String[] args) throws IOException {
        
		// Hardcode in IP and Port here if required
    	args = new String[] {"127.0.0.1", "30121"};
    	
        if (args.length != 2) {
            System.err.println(
                "Usage: java Client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket clientSocket = new Socket(hostName, portNumber);
            PrintWriter requestWriter = // stream to write text requests to server
                new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader responseReader= // stream to read text response from server
                new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())); 
            BufferedReader stdIn = // standard input stream to get user's requests
                new BufferedReader(
                    new InputStreamReader(System.in))
        ) {
        	//initialize boolean variable to check whether to continue generating jobs
        	boolean keepGoing = true;
        	//initialize Random object
        	Random rand = new Random();
        	
        	while(keepGoing) {
        		//randomly generate id for a job
        		int id = rand.nextInt();
        		
        		//randomly generate if job is type A or B
        		String type = rand.nextInt(2) == 0 ? "A" : "B";
        		
        		//pass info to thread to write to master
        		Thread writer = new ClientToMasterThread(requestWriter, type, id);
        		writer.start();
        		
        		//create thread to receive confirmation from master that job is complete
        		Thread reader = new ClientFromMasterThread(responseReader, type, id);
        		reader.start();
        	}
      
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);

        } 
    }
}
