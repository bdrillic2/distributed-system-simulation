package client;
import java.io.*;
import java.util.Random;

public class ClientToMasterThread extends Thread{
	
	private PrintWriter requestWriter;
	private String type;
	private int id;
	private int clientNum;
	
	public ClientToMasterThread(PrintWriter requestWriter, int clientNum) {
		this.requestWriter = requestWriter;
		this.clientNum = clientNum;

	}
	
	public void run() {
		
		System.out.println("In ClientToMasterThread");

    	
    	//initialize boolean variable to check whether to continue generating jobs
    	boolean keepGoing = true;
    	int  stop = 0;
    	//initialize Random object
    	Random rand = new Random();
    	
    	//unless we add user input option to set keepGoing to false and exit program, 
    	//for now client will continue generating jobs infinitely in this while loop until we 
    	//kill the program
    	while(stop < 30) {
    		//randomly generate id for a job
    		id = rand.nextInt();
    		
    		//randomly generate if job is type A or B
    		type = rand.nextInt(2) == 0 ? "A" : "B";
    		
    		//send job type and id to master
    		requestWriter.println(type + " " + id + " client " + clientNum);
    		
    		//print confirmation message to console
    		System.out.println("Job ID: " + id + " Type: " + type + " from client " + clientNum + " sent to master server");
    		
    		stop++;
    	}
	}
}
