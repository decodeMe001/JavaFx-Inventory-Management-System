package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CustomerModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class AddCustomerController implements Initializable {

    @FXML
    private JFXTextField nameOfCustomer;

    @FXML
    private JFXTextField phoneNumberInput;

    @FXML
    private JFXTextField addressLabelInput;

    @FXML
    private JFXTextField remarkLabel;

    @FXML
    private JFXTextField totalPurchaseInput;

    @FXML
    private JFXDatePicker datePickLabel;

    @FXML
    private JFXTextField addedBy;
    @FXML
    private Button saveButton;

    @FXML
    private Button updateButton;
    
    @FXML
    private Label customerIDLabel;
    
    private boolean okClicked = false;
    Stage dialogStage;
    static String temp;
    Connection conn = SqliteConnection.Connector();  
    private static boolean hasData = false;
    PreparedStatement pS = null;
    ResultSet rS = null;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        customerIDLabel.setText(randomAplphaNum(5));
        
    }    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
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
    
    public static String randomAplphaNum(int count){
       StringBuilder builder = new StringBuilder();
       while(count-- != 0){
           int character = (int)(Math.random()*ALPHA_NUM_STRING.length());
           builder.append(ALPHA_NUM_STRING.charAt(character));
       }
       return builder.toString();
   }
    
    public void setSaveDisable(){
        saveButton.setDisable(true);
    }
    public void setUpdateDisable(){
        updateButton.setDisable(true);
    }

    @FXML
    void handleExit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void handleSave(ActionEvent event) {
        if (isInputValid()){                                  
                        String custName = nameOfCustomer.getText();
                        String phoneNum = phoneNumberInput.getText();
                        String addrss = addressLabelInput.getText();
                        Double total = Double.parseDouble(totalPurchaseInput.getText());
                        String remk = remarkLabel.getText();
                        Object date = datePickLabel.getValue();
                        String addBy = addedBy.getText();
                        String id = customerIDLabel.getText();
                        
                String query = "INSERT INTO customerInfo (name, phone, address, date, add_by, total, remark, customer_id) "
                        + "VALUES(?,?,?,?,?,?,?,?)";
                         
                    try{
                        
                        pS = conn.prepareStatement(query);
                        pS.setString(1, custName);
                        pS.setString(2, phoneNum);
                        pS.setString(3, addrss);
                        pS.setObject(4, date);
                        pS.setString(5, addBy);
                        pS.setDouble(6, total);
                        pS.setString(7, remk);
                        pS.setString(8, id);
                        
         
                    } catch(SQLException e){
                    }finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                    } 
                    okClicked = true;
                    if(okClicked)
                    { 
                        okClicked = true;
                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("Congratulation!!!");
                            tray.setMessage("Item Successfully Added!.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                    }else{
                        System.exit(0);
                    }               
                }    
        
    }  
     
    @FXML
    void handleUpdate(ActionEvent event) {
        if(isInputValid()){
                        String custName = nameOfCustomer.getText();
                        String phoneNum = phoneNumberInput.getText();
                        String addrss = addressLabelInput.getText();
                        Double total = Double.parseDouble(totalPurchaseInput.getText());
                        String remk = remarkLabel.getText();
                        Object date = datePickLabel.getValue();
                        String addBy = addedBy.getText();
                        String id = customerIDLabel.getText();
                        
                String query = "UPDATE customerInfo SET name=?, phone=?, address=?, date=?, add_by=?, total=?, remark=?, customer_id=? where customer_id='"+temp+"'";
            try{
                pS = conn.prepareStatement(query);
                        pS.setString(1, custName);
                        pS.setString(2, phoneNum);
                        pS.setString(3, addrss);
                        pS.setObject(4, date);
                        pS.setString(5, addBy);
                        pS.setDouble(6, total);
                        pS.setString(7, remk);
                        pS.setString(8, id);
                        
                   pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddCategoryController.class.getName()).log(Level.SEVERE, null, ex);
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
            }
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
       
    }
    public void setCustomerData(CustomerModel customerModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM customerInfo where customer_id = '"+customerModel.getCustomerId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                nameOfCustomer.setText(rs.getString("name"));            
                phoneNumberInput.setText(rs.getString("phone")); 
                addressLabelInput.setText(rs.getString("address"));
                datePickLabel.setValue(LocalDate.parse(rs.getObject("date").toString()));                     
                totalPurchaseInput.setText(Double.toString(rs.getDouble("total")));                     
                addedBy.setText(rs.getString("add_by"));
                remarkLabel.setText(rs.getString("remark"));
                customerIDLabel.setText(rs.getString("customer_id"));
            }
            temp = customerIDLabel.getText();
           
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
     /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameOfCustomer.getText().isEmpty() && phoneNumberInput.getText().isEmpty()
                && addressLabelInput.getText().isEmpty() && totalPurchaseInput.getText().isEmpty()
                && remarkLabel.getText().isEmpty() && addedBy.getText().isEmpty()) {
            errorMessage += "No valid Selection(Must make a Selection)!\n"; 
        }        
        
        if (datePickLabel.toString().isEmpty()) {
            errorMessage += "No valid Date Choosen!\n";
        } 
       
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("No Customer Selected!!!");
            alert.setHeaderText("Please Select Products for Sales");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
