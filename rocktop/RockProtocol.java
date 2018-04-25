package rocktop;

import java.sql.*;

public class RockProtocol {
	Connection conn = null;

	public int process(String str, String ip) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rocktop?autoReconnect=true&useSSL=false",
					"root", "root");
		} catch (ClassNotFoundException e) {
			System.out.println("classnotfound");
		} catch (SQLException e) {
			System.out.println("sql errro1");
		}
		String[] input = str.split(",");
			switch (input[0]) {
			case "login":
				try {
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + input[1] + "'");
					if (rs.next()) {
						if (rs.getObject(2).toString().equals(input[2])) {
							System.out.println("Login: success from " + ip);
							return 1; // logged in
						}
					}
					System.out.println("Login: failure from " + ip);
					return 3;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "create":
				try {
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + input[1] + "'");
					if (rs.next()) {
						System.out.println("Create: failure from " + ip);
						return 0; // account exists
					}
					PreparedStatement pstmt = conn.prepareStatement("INSERT INTO acc (user, pass, salt) VALUES(\""
							+ input[1] + "\",\"" + input[2] + "\",\"" + input[3] + "\")");
					if (!pstmt.execute()) {
						System.out.println("Create: success from " + ip);
						return 2; // created
					}
				} catch (SQLException e) {
					System.out.println("sql error2");
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Invalid request");
				break;
			}
		return -1; // failed
	}

	public String hash(String user) {
		String salt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rocktop?autoReconnect=true&useSSL=false",
					"root", "root");
		} catch (ClassNotFoundException e) {
			System.out.println("classnotfound");
		} catch (SQLException e) {
			System.out.println("sql errro1");
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + user + "'");
			if (rs.next()) {
				salt = rs.getObject(3).toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (salt == null)
			salt = "";
		return salt;
	}
}
