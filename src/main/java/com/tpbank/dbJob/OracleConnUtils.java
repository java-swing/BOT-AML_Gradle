package com.tpbank.dbJob;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnUtils {
 
 // Kết nối vào ORACLE.
 public static Connection getOracleConnection() throws SQLException,
         ClassNotFoundException {
     String hostName = "10.1.28.82";
     String sid = "GOLDIB";
     String userName = "PAYGATE";
     String password = "paygate123";
 
     return getOracleConnection(hostName, sid, userName, password);
 }
 
 public static Connection getOracleConnection(String hostName, String sid,
         String userName, String password) throws ClassNotFoundException,
         SQLException {
 
     // Khai báo class Driver cho DB Oracle
     // Việc này cần thiết với Java 5
     // Java6 tự động tìm kiếm Driver thích hợp.
     // Nếu bạn dùng Java6, thì ko cần dòng này cũng được.
     Class.forName("oracle.jdbc.driver.OracleDriver");
 
     // Cấu trúc URL Connection dành cho Oracle
     // Ví dụ: jdbc:oracle:thin:@localhost:1521:db11g
     String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521/" + sid;
 
     Connection conn = DriverManager.getConnection(connectionURL, userName,
             password);
     return conn;
 }
}