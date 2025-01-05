package client;
import java.io.*;
//reads in confirmation from master
public class ClientFromMasterThread extends Thread {

	private BufferedReader reader;
	private String type;
	private int id;
	
	public ClientFromMasterThread(BufferedReader reader, String type, int id) {
		this.reader = reader;
		this.type = type;
		this.id = id;
	}
	
	public void run() {
		//read in message from server that job is complete
		String response = null;
		
		try {
			synchronized(reader) {
				response = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//display message to console
		System.out.println("SERVER SAYS: " + response);
		
	}
}
