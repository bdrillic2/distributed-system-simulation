package distributed_system_simulation;

public class SlaveToMasterThread extends Thread{

	private PrintWriter writer;
	
	public SlaveToMasterThread(PrintWriter writer) {
		this.writer = writer;
	}
	
	public void run() {
		writer.println("Job is complete");
	}
}
