package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;

	public static void main(String args[]) {
		connect();
		inputs();
	}

	public static void connect() {
		try {
			socket = new Socket("localhost", 4321);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected.");
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: localhost");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
	}
	public static void inputs() {
		Scanner scan = new Scanner(System.in);
		out.println(scan.nextLine());
		scan.close();
		System.out.println("Logging in...");
		register();
	}
	public static void register() {
		while(!socket.isClosed()) {
			try {
				int line = Integer.parseInt(in.readLine());
				switch(line) {
				case -1:
					System.out.println("ERROR");
					inputs();
					//socket.close();
					break;
				case 0:
					System.out.println("ACCOUNT EXISTS");
					inputs();
					//socket.close();
					break;
				case 1:
					System.out.println("LOGGED IN");
					inputs();
					//socket.close();
					break;
				case 2:
					System.out.println("ACCOUNT CREATED");
					inputs();
					//socket.close();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
