package rocktop;

import java.sql.*;

public class RockProtocol {
	Connection conn = null;
	public int process(String str) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rocktop?autoReconnect=true&useSSL=false", "root", "root");
		} catch (ClassNotFoundException e) {
			System.out.println("classnotfound");
		} catch (SQLException e) {
			System.out.println("sql errro1");
		}
		String[] input = str.split(",");
		if (input.length == 3) {
			switch (input[0]) {
			case "login":
				try {
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + input[1] + "'");
					rs.next();
					if(rs.getObject(2).toString().equals(input[2])) {
						System.out.println("login: success");
						return 1; //logged in
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "create":
				try {
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + input[1] + "'");
					if(rs.next()) return 0; //account exists
					PreparedStatement pstmt = conn.prepareStatement("INSERT INTO acc (user, pass) VALUES(\"" + input[1] + "\",\"" + input[2] + "\")");
					if(!pstmt.execute()) return 2; //created
				} catch (SQLException e) {
					System.out.println("sql error2");
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Invalid request");
				break;
			}
		}
		return -1; //failed
	}
}
