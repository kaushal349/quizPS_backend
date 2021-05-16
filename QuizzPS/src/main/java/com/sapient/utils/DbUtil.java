package com.sapient.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    public static Connection createConnection() throws ClassNotFoundException, SQLException {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://165.22.217.98/psquiz";
        String user = "root";
        String password = "root";

		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
    }
}
