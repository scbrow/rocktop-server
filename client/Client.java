package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Client {
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static Scanner scan = null;

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
		} catch (IOException e) {
			System.out.println("No I/O");
		}
	}

	public static void inputs() {
		String result = null;
		scan = new Scanner(System.in);
		String[] input = scan.nextLine().split(",");
		if (input.length != 3) {
			System.out.println("incorrect input");
			return;
		}
		Random random = new Random();
		String ssalt = getSalt(input[1]);
		byte[] salt = null;
		Base64.Decoder dec = Base64.getDecoder();
		Base64.Encoder enc = Base64.getEncoder();
		boolean exists = true;
		if (ssalt.isEmpty()) {
			salt = new byte[16];
			random.nextBytes(salt);
			exists = false;
		} else {
			salt = dec.decode(ssalt);
		}
		byte[] hash = encrypt(salt, input[2]);
		if (exists) {
			result = input[0].concat("," + input[1] + "," + enc.encodeToString(hash));
		} else {
			result = input[0].concat("," + input[1] + "," + enc.encodeToString(hash) + "," + enc.encodeToString(salt));
		}
		out.println(result);
		System.out.println("Communicating...");
		register();
	}

	public static void register() {
		while (!socket.isClosed()) {
			try {
				int line = Integer.parseInt(in.readLine());
				switch (line) {
				case -1:
					System.out.println("ERROR");
					//socket.close();
					break;
				case 0:
					System.out.println("ACCOUNT EXISTS");
					//socket.close();
					break;
				case 1:
					System.out.println("LOGGED IN");
					//socket.close();
					break;
				case 2:
					System.out.println("ACCOUNT CREATED");
					//socket.close();
					break;
				case 3:
					System.out.println("INVALID USERNAME/PASSWORD");
					//socket.close();
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputs();
		}
	}

	public static String getSalt(String user) {
		String salt = null;
		out.println(user);
		boolean truth = true;
		while (truth) {
			try {
				salt = in.readLine();
				if (salt != null)
					truth = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return salt;
	}

	public static byte[] encrypt(byte[] salt, String input) {
		byte[] result = null;
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(input.toCharArray(), salt, 65536, 128);
			byte[] hash = f.generateSecret(spec).getEncoded();
			result = hash;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
