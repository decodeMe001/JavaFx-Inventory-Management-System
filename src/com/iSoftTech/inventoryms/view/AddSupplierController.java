package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.SupplierModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Dada abiola
 */
public class AddSupplierController implements Initializable{
    
    Stage dialogStage;
    public CurrentStockController cSC;
    private boolean okClicked = false;
    SupplierModel supplierM;
    Connection conn = SqliteConnection.Connector(); 
    private static boolean hasData = false;
    PreparedStatement pS;
    ResultSet rS;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    @FXML
    private JFXTextField supplierInput;

    @FXML
    private JFXTextField phoneInput;

    @FXML
    private JFXTextField addressInput;

    @FXML
    private JFXTextField remarkInput;

    @FXML
    private JFXDatePicker dateL;

    @FXML
    private JFXDatePicker timeL;
    
    @FXML
    private Label lblCaption;

    @FXML
    private Button btnClose;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button saveButton;
    
    static String temp; 
    
    @FXML
    private Label supplierIDLabel;
    
    public static String randomAplphaNum(int count){
       StringBuilder builder = new StringBuilder();
       while(count-- != 0){
           int character = (int)(Math.random()*ALPHA_NUM_STRING.length());
           builder.append(ALPHA_NUM_STRING.charAt(character));
       }
       return builder.toString();
   }
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    public void setSaveDisable(){
        saveButton.setDisable(true);
    }
    public void setUpdateDisable(){
        updateButton.setDisable(true);
    }
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSavedClicked() {
        return okClicked;
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        supplierIDLabel.setText(randomAplphaNum(5));
        
    }
    
    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public void setSupplierData(SupplierModel supplierModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM supplierData where supplier_id = '"+supplierModel.getSupplierId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                supplierInput.setText(rs.getString("supplier"));
                phoneInput.setText(rs.getString("phone_num"));            
                addressInput.setText(rs.getString("address"));            
                remarkInput.setText(rs.getString("payment"));
                dateL.setValue(LocalDate.parse(rs.getObject("date_supplied").toString()));            
                timeL.setTime(LocalTime.parse(rs.getObject("time_supplied").toString()));            
                supplierIDLabel.setText(rs.getString("supplier_id"));
            }
            temp = supplierIDLabel.getText();
           
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

    @FXML
    void handleSave(ActionEvent event) throws SQLException {
        if (isInputValid()) {
                                                           
                        String supInput = supplierInput.getText();
                        String phone = phoneInput.getText();
                        String addrInput = addressInput.getText();
                        String remark = remarkInput.getText();
                        Object date = dateL.getValue();
                        Object time = timeL.getTime();
                        String id = supplierIDLabel.getText();
                        
                String query = "INSERT INTO supplierData (supplier, phone_num, address, date_supplied, time_supplied, payment, supplier_id) "
                        + "VALUES(?,?,?,?,?,?,?)";
                    pS = null;
                    
                    try{
                        pS = conn.prepareStatement(query);                     
                        pS.setString(1, supInput);
                        pS.setString(2, phone);
                        pS.setString(3, addrInput);
                        pS.setObject(4, date);
                        pS.setObject(5, time);
                        pS.setString(6, remark);
                        pS.setString(7, id);
                        
                    } catch(SQLException e){
                    }finally{
                        pS.execute();
                        pS.close();
                    }
                    okClicked = true;
                    if(okClicked)
                    { 
                        
                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("Congratulation!!!");
                            tray.setMessage("Item Successfully Added!.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));
                        ((Node)(event.getSource())).getScene().getWindow().hide();
                    }               
        }
    }
    
     @FXML
    void handleUpdate(ActionEvent event) {
        if(isInputValid()){
                        String supInput = supplierInput.getText();
                        String phone = phoneInput.getText();
                        String addrInput = addressInput.getText();
                        String remark = remarkInput.getText();
                        Object date = dateL.getValue();
                        Object time = timeL.getTime();
                        String id = supplierIDLabel.getText();
                        
                        String data = "update supplierData set supplier=?, phone_num=?, address=?, date_supplied=?, time_supplied=?, payment=?"
                                + "supplier_id=? where supplier_id='"+temp+"'";
                try {    
                    
                    pS = conn.prepareStatement(data);
                        pS.setString(1, supInput);
                        pS.setString(2, phone);
                        pS.setString(3, addrInput);
                        pS.setObject(4, date);
                        pS.setObject(5, time);
                        pS.setString(6, remark);
                        pS.setString(7, id);
                        pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddSupplierController.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                }
                okClicked = true;
                if(okClicked){
                    NotificationType notificationType = NotificationType.SUCCESS;
                    TrayNotification tray = new TrayNotification();
                    tray.setTitle("Congratulation!!!");
                    tray.setMessage("Items Successfully Updated.");
                    tray.setNotificationType(notificationType);
                    tray.showAndDismiss(Duration.millis(3000));
                    ((Node)(event.getSource())).getScene().getWindow().hide(); 
                }     
       }
                
    }


    
     /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (supplierInput.getText() == null || supplierInput.getText().length() == 0) {
            errorMessage += "No valid Type(Must be a string)!\n"; 
        }
        if (phoneInput.getText() == null || phoneInput.getText().length() == 0) {
            errorMessage += "No valid phone Number!\n"; 
        } 
       
        if (addressInput.getText() == null || addressInput.getText().length() == 0) {
            errorMessage += "No valid Address(Must Be a String!)\n"; 
        }
        
        if (remarkInput.getText() == null || remarkInput.getText().length() == 0) {
            errorMessage += "No valid Remark!\n"; 
        } 
        
        
        if (dateL.toString() == null || dateL.toString().length() == 0) {
            errorMessage += "No valid Date!\n";
        } 
        if (timeL.timeProperty() == null) {
            errorMessage += "No valid Time!\n";
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
}
