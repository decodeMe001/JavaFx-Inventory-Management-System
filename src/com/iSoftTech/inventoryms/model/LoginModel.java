package com.iSoftTech.inventoryms.model;
import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.view.ViewSellController;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author Dada abiola
 */
public class LoginModel implements Initializable {
    Connection connection;
    
    public LoginModel(){
        connection = SqliteConnection.Connector();
        if(connection == null) 
            System.exit(1);
    }
    
    public boolean isDbConnected(){
        try
        {
          //if connection is not closed, then we're connected
          return !connection.isClosed();
            
        }catch (SQLException e){
            return false;
        }
    }
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
    
    public boolean adminLogin(String user, String pass) throws SQLException{
        
        PreparedStatement pS=null;
        ResultSet rS=null;
        String query = "select * from simslogin where username = ? and password = ?";
        try{
            pS = connection.prepareStatement(query);
            pS.setString(1, user);
            pS.setString(2, pass);
            rS = pS.executeQuery();

            return rS.next();
            
        }catch (Exception e){
            return false;
        }finally {
            pS.close();
            rS.close();
        }
    }
    
    public boolean staffLogin(String user, String pass) throws SQLException{
              
                PreparedStatement pS = null;
                ResultSet rS = null;
                String query = "select * from staffData where username = ? and password = ?";
                try{
                    pS = connection.prepareStatement(query);
                    pS.setString(1, user);
                    pS.setString(2, pass);
                    rS = pS.executeQuery();
                    
                    return rS.next();
                    
                }catch (Exception e){
                    return false;
                }finally {
                    pS.close();
                    rS.close();
                }
            }
}
