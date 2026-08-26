//package vn.iotstar.connection;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class DBConnection {
//
//    private final String serverName = "localhost";
//    private final String dbName = "CategoryCRUD";
//    private final String portNumber = "3306";
//
//    private final String userID = "root";
//    private final String password = "Son@01664425573";
//
//    public Connection getConnection() throws Exception {
//
//        String url = "jdbc:mysql://" 
//                + serverName 
//                + ":" 
//                + portNumber
//                + "/" 
//                + dbName
//                + "?useSSL=false"
//                + "&serverTimezone=UTC"
//                + "&allowPublicKeyRetrieval=true";
//
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        return DriverManager.getConnection(
//                url,
//                userID,
//                password
//        );
//    }
//}