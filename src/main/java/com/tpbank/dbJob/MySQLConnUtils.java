package com.tpbank.dbJob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnUtils {


    // Connect to MySQL
    public static Connection getMySQLConnectionToGetLog() throws SQLException,
            ClassNotFoundException {
        String hostName = "localhost";

        String dbName = "AML_BOT_LOG_MYSQL";
        String userName = "root";
        String password = "123456";

        return getMySQLConnectionToGetLog(hostName, dbName, userName, password);
    }
    // Connect to MySQL
    public static Connection getMySQLConnectionToInputStatus() throws SQLException,
            ClassNotFoundException {
        String hostName = "localhost";

        String dbName = "AsaveLastStatusOperation";
        String userName = "root";
        String password = "123456";

        return getMySQLConnectionToGetLog(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnectionToGetLog(String hostName, String dbName,
                                                        String userName, String password) throws SQLException,
            ClassNotFoundException {
        // Declare the class Driver for MySQL DB
        // This is necessary with Java 5 (or older)
        // Java6 (or newer) automatically find the appropriate driver.
        // If you use Java> 5, then this line is not needed.
//        Class.forName("com.mysql.jdbc.Driver");

        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);
        return conn;
    }
    public static Connection getMySQLConnectionToInputStatus(String hostName, String dbName,
                                                        String userName, String password) throws SQLException,
            ClassNotFoundException {
        // Declare the class Driver for MySQL DB
        // This is necessary with Java 5 (or older)
        // Java6 (or newer) automatically find the appropriate driver.
        // If you use Java> 5, then this line is not needed.
//        Class.forName("com.mysql.jdbc.Driver");

        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

        Connection conn = DriverManager.getConnection(connectionURL, userName,
                password);
        return conn;
    }
}