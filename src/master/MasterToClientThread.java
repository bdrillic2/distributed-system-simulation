package master;
import java.io.*;

//Thread writes confirmation to client
public class MasterToClientThread extends Thread{

	private PrintWriter writer;
	private String id;
	private String chosenSlave;
	
	public MasterToClientThread(PrintWriter writer, String id, String chosenSlave) {
		this.writer = writer;
		this.id = id;
		this.chosenSlave = chosenSlave;
	}
	
	public void run() {
		System.out.println("In MasterToClientThread");
		// Print to console
		System.out.println("Job " + id + " is complete on Slave " + chosenSlave);
		
		// Print to PrintWriter
        writer.println("Job " + id + " is complete on Slave " + chosenSlave);
	}
}
