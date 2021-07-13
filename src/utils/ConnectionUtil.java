package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionUtil {
    Connection conn = null;
    public static Connection conDB()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3325/burn_in_tester?useUnicode=true&characterEncoding=utf8";
            Connection con = DriverManager.getConnection(url,"root","");  

            return con;
        } catch (Exception e) {
            System.out.println("Database not connected");
           return null;
        }
    }
}
