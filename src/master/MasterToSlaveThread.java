package master;

import java.io.PrintWriter;

public class MasterToSlaveThread extends Thread {

	private PrintWriter writer;
	private String job;

	public MasterToSlaveThread(PrintWriter writer, String job) {
		this.writer = writer;
		this.job = job;
	}

	public void run() {
		System.out.println("In MasterToClientThread");
		System.out.println("Sending Job " + job + "to slave");

		// Send job to slave
		synchronized(writer) {
			writer.println(job);
		}
	}
}
