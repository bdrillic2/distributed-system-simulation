package client;
import java.io.*;
//reads in confirmation from master
public class ClientFromMasterThread extends Thread {

	private BufferedReader reader;
	
	public ClientFromMasterThread(BufferedReader reader) {
		this.reader = reader;
	}
	
	public void run() {
		
		System.out.println("In ClientFromMasterThread");
		
		//initialize boolean to true that while loop should continue executing
		boolean keepGoing = true;
		
		//read in each message from server that job is complete as it comes in
		String response = null;
		
        try {
        	while(keepGoing) {
        		synchronized(reader) {
        			response = reader.readLine();
        			
        			//display message to console
        			System.out.println("SERVER SAYS: " + response);
        		}
        		
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
