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
		}

		while (client.isConnected()) {
			try {
				line = in.readLine();
				if (line != null) {
					if (line.length() - line.replaceAll(",", "").length() != 0) {
						out.println(prot.process(line, client.getInetAddress().getHostAddress()));
					} else {
						out.println(prot.hash(line));
					}
				}
			} catch (IOException e) {
			}
		}
	}
}
