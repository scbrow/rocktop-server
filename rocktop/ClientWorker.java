package rocktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWorker implements Runnable {
	private Socket client;
	RockProtocol prot = new RockProtocol();
	// Constructor
	ClientWorker(Socket client) {
		this.client = client;
	}

	public void run() {
		String line;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}

		while (client.isConnected()) {
			try {
				line = in.readLine();
				out.println(prot.process(line));
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
		}
	}
}
