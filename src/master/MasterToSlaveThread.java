package master;

import java.io.PrintWriter;

public class MasterToSlaveThread extends Thread {

	private PrintWriter writer;
	private String job;
	private String slaveType;

	public MasterToSlaveThread(PrintWriter writer, String job, String slaveType) {
		this.writer = writer;
		this.job = job;
		this.slaveType = slaveType;
	}

	public void run() {
		System.out.println("In MasterToSlaveThread");
		System.out.println("Sending Job " + job + " to slave " + slaveType);

		// Send job to slave
		synchronized(writer) {
			writer.println(job);
		}
	}
}
