/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tubesmibd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nicho
 */
public class DatabaseConnection {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String dbURL = "jdbc:sqlserver://VITTORIO;databaseName=master;encrypt=true;trustServerCertificate=true;integratedSecurity=false";
                String username = "user1";
                String password = "password";
                connection = DriverManager.getConnection(dbURL, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}

//    public static void main(String[] args) throws SQLException {
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String dbURL = "jdbc:sqlserver://VITTORIO;databaseName=master;encrypt=true;trustServerCertificate=true;itegratedSecurity=true";
//            
//            String username = "user1";
//            String password = "password";
//
//            Connection connection = DriverManager.getConnection(dbURL, username, password);
//            Statement stat = connection.createStatement();
//            String query = "SELECT * FROM ASISTEN";
//            ResultSet rs = stat.executeQuery(query);
//            while (rs.next()) {
//                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
//            }
//
//            rs.close();
//            stat.close();
//            connection.close();
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
//    private static Connection connection;
//
//    public static Connection getConnection() {
//        if (connection == null) {
//            try {
//                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
////                String dbURL = "jdbc:sqlserver://VITTORIO;databaseName=SubsDirectory;integratedSecurity=true";
//                String dbURL = "jdbc:sqlserver://VITTORIO:1433;databaseName=namaDB;encrypt=false;trustServerCertificate=true";
//                Connection conn = DriverManager.getConnection(dbURL, "username", "password");
//
//                connection = DriverManager.getConnection(dbURL);
//            } catch (ClassNotFoundException | SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return connection;
//    }
// public static Connection getConnection() throws SQLException {
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String dbURL = "jdbc:sqlserver://VITTORIO;databaseName=master;encrypt=true;trustServerCertificate=true;";
//            String username = "user1";
//            String password = "password";
//
//            return DriverManager.getConnection(dbURL, username, password);
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            throw new SQLException("Driver tidak ditemukan");
//        }
//    }
//}
