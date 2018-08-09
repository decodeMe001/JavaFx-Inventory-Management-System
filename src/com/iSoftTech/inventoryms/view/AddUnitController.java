package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.UnitModel;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Dada abiola
 */
public class AddUnitController implements Initializable{
    
    Stage dialogStage;
    public UnitController cSC;
    private boolean okClicked = false;
    Connection conn = SqliteConnection.Connector(); 
    
    PreparedStatement pS;
    ResultSet rS;
     
    @FXML
    private JFXTextField unitValue;

    @FXML
    private JFXTextField addedBy;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXDatePicker time;

    @FXML
    private Label lblHeader;
    
    @FXML
    private ComboBox<String> cbBrand;
    
    @FXML
    private Label unitIDLabel;
   
    @FXML
    private Button btnClose;
    @FXML
    private Button saveButton;
    @FXML
    private Button updateButton;
    static String temp;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        unitIDLabel.setText(randomAplphaNum(5));
        
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
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSavedClicked() {
        return okClicked;
    }
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
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
    void handleSave(ActionEvent event) throws SQLException {
        if (isInputValid()) {
                                                           
                        Integer uniData = Integer.parseInt(unitValue.getText());
                        String brandPro = cbBrand.getSelectionModel().getSelectedItem();
                        String add = addedBy.getText();
                        Object dateL = date.getValue();
                        Object timeL = time.getTime();
                        String id = unitIDLabel.getText();
                        
                String query = "INSERT INTO unitdata (unit, brand, created_by, date, time, unit_id) VALUES(?,?,?,?,?,?)";
                    pS = null;
                    
                    try{
                        pS = conn.prepareStatement(query);
                        
                        pS.setInt(1, uniData);
                        pS.setString(2, brandPro);
                        pS.setString(3, add);
                        pS.setObject(4, dateL);
                        pS.setObject(5, timeL);
                        pS.setString(6, id);
                           
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
    void handleUpdate(ActionEvent event){
        if (isInputValid()) {
                                                           
                        Integer uniData = Integer.parseInt(unitValue.getText());
                        String brandPro = cbBrand.getSelectionModel().getSelectedItem();
                        String add = addedBy.getText();
                        Object dateL = date.getValue();
                        Object timeL = time.getTime();
                        String id = unitIDLabel.getText();
                        
                String query = "UPDATE unitdata SET unit=?, brand=?, created_by=?, date=?, time=?, unit_id=? where unit_id='"+temp+"'";
                   try{
                        pS = conn.prepareStatement(query);
                        pS.setInt(1, uniData);
                        pS.setString(2, brandPro);
                        pS.setString(3, add);
                        pS.setObject(4, dateL);
                        pS.setObject(5, timeL);
                        pS.setString(6, id);
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
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
        
        }
        
    }
    @FXML
    void cbAddBrand(MouseEvent event) {
        cbBrand.getItems().clear();
        cbBrand.getValue();
        try{
            String sql = "SELECT * FROM brandData order by brand";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
           
            while(rS.next()){
                String brand = rS.getString("brand");
                cbBrand.getItems().remove(brand);
                cbBrand.getItems().add(brand);
            }
           
        }catch (SQLException ex) {
            Logger.getLogger(AddStockDataController.class.getName()).log(Level.SEVERE, null, ex);
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
    void cmBoxBrandAction(ActionEvent event) {
        
        cbBrand.getValue();
        try {
            pS = conn.prepareStatement("select * from brandData where brand=?");
            pS.setString(1, cbBrand.getSelectionModel().getSelectedItem());
            rS = pS.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(AddStockDataController.class.getName()).log(Level.SEVERE, null, ex);
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
    public void handleExit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public void setUnitData(UnitModel unitModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM unitdata WHERE unit_id = '"+unitModel.getUnitID()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                unitValue.setText(Integer.toString(rs.getInt("unit")));            
                cbBrand.setValue(rs.getString("brand")); 
                addedBy.setText(rs.getString("created_by"));
                date.setValue(LocalDate.parse(rs.getObject("date").toString()));                     
                time.setTime(LocalTime.parse(rs.getObject("time").toString()));                     
                unitIDLabel.setText(rs.getString("unit_id"));
            }
            temp = unitIDLabel.getText();
           
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

        if (unitValue.getText() == null || unitValue.getText().length() == 0) {
            errorMessage += "No valid Unit Input(Must Be a String!)\n"; 
        }else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(unitValue.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Amount (must be an Integer)!\n"; 
            }
        }
        if (cbBrand.getSelectionModel().getSelectedItem() == null && cbBrand.getPromptText().isEmpty()) {
            errorMessage += "No Brand Entry(Must Be a String!)\n"; 
        }
        
        if (addedBy.getText() == null || addedBy.getText().length() == 0) {
            errorMessage += "No valid Added By!\n"; 
        }         
        
        if (date.toString()== null) {
            errorMessage += "No valid Date!\n";
        } 
        if (time.timeProperty() == null) {
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
