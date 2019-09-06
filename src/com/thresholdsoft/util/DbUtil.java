package com.thresholdsoft.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*created date : 05-09-2019.
/**
 * @author uday
 *created date : 05-09-2019.       
 * This DbUtil contains database connection details.
 */
public class DbUtil {
	public static Connection getConnection() throws ClassNotFoundException, SQLException { // getting connection
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "The@1234");
		conn.setAutoCommit(false);
		return conn;
	}

	public static void closeConnection(Connection connection) throws SQLException { // close the connection
		if (connection == null)
			return;
		if (!connection.getAutoCommit())
			connection.commit();
		if (connection.isClosed())
			connection.close();
	}
}
