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
		Scanner scan = new Scanner(System.in);
		out.println(scan.nextLine());
		scan.close();
		System.out.println("sent log");
		while(socket.isConnected()) {
			try {
				int line = Integer.parseInt(in.readLine());
				switch(line) {
				case -1:
					System.out.println("ERROR");
					socket.close();
					break;
				case 0:
					System.out.println("ACCOUNT EXISTS");
					socket.close();
					break;
				case 1:
					System.out.println("LOGGED IN");
					socket.close();
					break;
				case 2:
					System.out.println("ACCOUNT CREATED");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
}
