package client;
import java.io.*;
import java.net.*;


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
        	/**
        	 * Clients are going to connect directly to the master and submit jobs of either type. 
        	 * The client’s submission should include the type, and an ID number that will be used to 
        	 * identify the job throughout the system
        	 */
        	//for now basic job information is hard coded in
        	//job type
        	String type = "A";
        	//ID number
        	String id = "1234";
        	
        	//first send job type to master
        	requestWriter.println(type);
        	//then id number
        	requestWriter.println(id);
      
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
