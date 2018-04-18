package rocktop;

import java.io.IOException;
import java.net.ServerSocket;

public class rserver {
	private static ServerSocket server = null;

	public static void main(String[] args) {
		listenSocket();
	}

	public static void listenSocket() {
		try {
			server = new ServerSocket(4321);
		} catch (IOException e) {
			System.out.println("Could not listen on port 4321");
			System.exit(-1);
		}
		while (true) {
			ClientWorker w;
			try {
				// server.accept returns a client connection
				w = new ClientWorker(server.accept());
				Thread t = new Thread(w);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		}
	}

	protected void finalize() {
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}

}