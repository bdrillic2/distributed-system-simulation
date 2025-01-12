package slaves;

import java.io.PrintWriter;

public class SlaveToMasterThread extends Thread{

	private PrintWriter writer;
	private String slaveType;
	private String id;
	
	public SlaveToMasterThread(PrintWriter writer, String slaveType, String id) {
		this.writer = writer;
		this.slaveType = slaveType;
		this.id = id;
	}
	
	public void run() {
		System.out.println("In SlaveToMasterThread");
		System.out.println("Job " + id + " completed by slave " + slaveType);

		writer.println("Job " + id + " completed by slave " + slaveType);
	}
}
