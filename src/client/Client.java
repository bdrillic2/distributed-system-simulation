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
        	/**
        	 * Clients are going to connect directly to the master and submit jobs of either type. 
        	 * The client’s submission should include the type, and an ID number that will be used to 
        	 * identify the job throughout the system
        	 */
        	//started on logic for jobs, left ID threading as if only one client...
            Random rand = new Random();
            String type;
            String id;

            //can add another random element for i if we want to randomize number of jobs.
            for(int i = 0; i < 20; i++){
                rand.nextInt(2);
                if(rand == 0){
                    //job type
                    type = "A";
                    }
                else{
                    type = "B";
                }
                    //id number
        	        String id = Integer.toString(Integer.parseInt(id)+1)
                }
                //first send job type to master
        	    requestWriter.println(type);
        	    //then id number
        	    requestWriter.println(id);
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
