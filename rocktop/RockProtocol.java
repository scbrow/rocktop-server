package rocktop;

import java.sql.*;

public class RockProtocol {
	Connection conn = null;
	Statement stmt = null;
	public int process(String str) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rocktop", "root", "root");
			stmt = conn.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("SQL error");
		}
		String[] input = str.split(",");
		if (input.length == 3) {
			switch (input[0]) {
			case "login":
				try {
					ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + input[1] + "'");
					if(rs.getObject(2) == input[2]) return 1; //logged in
				} catch (SQLException e) {
					System.out.println("sql error2");
				}
				break;
			case "create":
				try {
					ResultSet rs = stmt.executeQuery("SELECT * FROM acc WHERE user = '" + input[1] + "'");
					if(!rs.next()) return 0; //account exists
					PreparedStatement pstmt = conn.prepareStatement("INSERT INTO acc (user, pass) " +
																   "VALUES (" + input[1] + ", " + input[2] + ")");
					if(pstmt.execute()) return 2; //created
				} catch (SQLException e) {
					System.out.println("sql error2");
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
