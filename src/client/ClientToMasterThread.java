package client;
import java.io.*;

public class ClientToMasterThread extends Thread{
	
	private PrintWriter requestWriter;
	private String type;
	private int id;
	
	public ClientToMasterThread(PrintWriter requestWriter, String type, int id) {
		this.requestWriter = requestWriter;
		this.type = type;
		this.id = id;
	}
	
	public void run() {
		//send job type and id to master
		synchronized(requestWriter) {
			requestWriter.println(type + " " + id);
		}
    
		//print confirmation message to console
		System.out.println("Job ID: " + id + " Type: " + type + " sent to master server");
	}
}
