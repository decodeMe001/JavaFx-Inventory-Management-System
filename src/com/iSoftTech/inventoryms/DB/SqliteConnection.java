package com.iSoftTech.inventoryms.DB;
import java.sql.*;

/**
 *
 * @author Dada abiola
 */
public class SqliteConnection {
    
    public static Connection Connector(){
        
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:inventoryDB.sqlite";
            Connection conn;
            conn = (Connection) DriverManager.getConnection(url);
            return conn;
            
        }catch (ClassNotFoundException | SQLException e){
            System.out.println(e);
            return null;
        }
    }
}
