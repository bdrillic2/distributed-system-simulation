package distributed_system_simulation;

public class MasterToSlaveThread extends Thread {

	private PrintWriter writer;
	private String job;

	public MasterToSlaveThread(PrintWriter writer, String job) {
		this.writer = writer;
		this.job = job;
	}

	public void run() {
		// Send job to slave
		writer.println(job);
	}
}
