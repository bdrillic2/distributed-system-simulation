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
    		System.out.println("In Client");
    		//pass in identity to master
    		requestWriter.println("CLIENT initialized");
    		System.out.println("Identity sent");
        	
        	//pass info to thread to write to master
        	Thread writer = new ClientToMasterThread(requestWriter);
        	writer.start();
        	
        	//create thread to receive confirmation from master as each job is complete
        	Thread reader = new ClientFromMasterThread(responseReader);
        	reader.start();
      
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
