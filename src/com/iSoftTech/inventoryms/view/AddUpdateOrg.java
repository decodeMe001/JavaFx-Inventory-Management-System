package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.OrganisationModel;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Dada abiola
 */
public class AddUpdateOrg implements Initializable{
    
    @FXML
    private JFXTextField nameOfOrg;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField address;

    @FXML
    private Button updateButton;
    
    private Stage primaryStage;
    Connection conn = SqliteConnection.Connector();  
    private static boolean hasData = false;
    private final ObservableList<OrganisationModel> orgData = FXCollections.observableArrayList();
    Stage dialogStage;
    PreparedStatement pS = null;
    ResultSet rS = null;
    boolean okClicked = false;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
        setUpdateDetails();
    }
    
    public boolean isOkClicked() {    
        return okClicked;
    }
       /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    @FXML
    void handleExit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handleUpdate(ActionEvent event) throws SQLException {
      if (isInputValid()) {
                                                           
                        String name = nameOfOrg.getText();
                        String num = phoneNumber.getText();
                        Object eMail = email.getText();
                        Object add = address.getText();
                        
                String query = "UPDATE orgInfo SET name=?, phone=?, email=?, head_office=?";
                    pS = null;
                    
                    try{
                        pS = conn.prepareStatement(query);
                      
                        pS.setString(1, name);
                        pS.setString(2, num);
                        pS.setObject(3, eMail);
                        pS.setObject(4, add);
                        pS.executeUpdate();
                        
                    } catch(SQLException e){
                    }finally{                  
                        pS.close();
                    }
                    
                    okClicked = true;
                            if(okClicked){
                                NotificationType notificationType = NotificationType.SUCCESS;
                                TrayNotification tray = new TrayNotification();
                                tray.setTitle("Congratulation!!!");
                                tray.setMessage("Details Successfully Updated.");
                                tray.setNotificationType(notificationType);
                                tray.showAndDismiss(Duration.millis(3000));
                            }
                        
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }  
    }
    
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameOfOrg.getText() == null || nameOfOrg.getText().length() == 0) {
            errorMessage += "No valid Name(Must Be a String!)\n"; 
        }
        if (phoneNumber.getText() == null || phoneNumber.getText().length() == 0) {
            errorMessage += "No valid Number(Must Be a Number!)\n"; 
        }
        
        if (email.getText() == null || email.getText().length() == 0) {
            errorMessage += "No valid e-mail!\n"; 
        }        
        
        if (address.getText() == null || address.getText().length() == 0) {
            errorMessage += "No valid Address!\n"; 
        }         
       
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please Insert valid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
     public void setUpdateDetails() {
        
        ResultSet rs;
        String query = "SELECT * FROM orgInfo";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                nameOfOrg.setText(rs.getString("name"));            
                phoneNumber.setText(rs.getString("phone")); 
                email.setText(rs.getString("email"));            
                address.setText(rs.getString("head_office"));
            }
           
        }catch (Exception e){
        }finally{
                      try{  
                        //conn.close();
                        pS.close();
                        rS.close();
                      }catch(Exception e){
                          
                      }
                }
     }
}