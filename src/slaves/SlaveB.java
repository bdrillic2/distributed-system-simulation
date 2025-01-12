package slaves;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

class SlaveB {
	public static void main(String[] args) throws InterruptedException {
		
		// Hardcode in IP and Port here
    	args = new String[] {"127.0.0.1", "30121"};
    	
		// will connect here
		if (args.length != 2) {
			System.err.println("Usage: java clientCode <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		// attempts to connect to socket
		try (Socket echoSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
		) {
			System.out.println("In Slave B");

			// Slave reads in job from master
			SlaveFromMasterThread listener = new SlaveFromMasterThread(in, "B", out);
			listener.start();

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}
