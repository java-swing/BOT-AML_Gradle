package com.tpbank.dbJob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDBManage {
    public static class MySqlConnection
    {
        private static String dbUrl = "jdbc:mysql://localhost:3306/AML_BOT_LOG_MYSQL";
        private static String dbUsername = "root";
        private static String dbPassword = "123456";

        public static Connection getConnection() throws SQLException
        {
            try {
                return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            }catch (Exception e){
                return null;
            }

        }
    }
}
