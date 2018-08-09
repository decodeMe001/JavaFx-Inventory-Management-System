package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.BrandModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class AddBrandController implements Initializable {
    
    Stage dialogStage;
    public CurrentStockController cSC;
    private boolean okClicked = false;
    BrandModel brandM;
    private Stage primaryStage;
    Connection conn = SqliteConnection.Connector(); 
    private static boolean hasData = false;
    PreparedStatement pS;
    ResultSet rS;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    @FXML
    private ComboBox<String> cbSupplier;

    @FXML
    private Button btnAddSupplyer;

    @FXML
    private Label lblHeader;

    @FXML
    private Button btnClose;
    
    @FXML
    private JFXTextField brandName;

    @FXML
    private JFXTextField description;

    @FXML
    private JFXTextField addedBy;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXDatePicker time;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Label brandIDLabel;
    
    static String temp; 

    
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
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        brandIDLabel.setText(randomAplphaNum(5));
        
    } 
    
    public void setSaveDisable(){
        saveButton.setDisable(true);
    }
    public void setUpdateDisable(){
        updateButton.setDisable(true);
    }

    @FXML
    private void cbSupplyerOnClick(MouseEvent event) {
        cbSupplier.getItems().clear();
        cbSupplier.getValue();
        try{
            String sql = "SELECT * FROM supplierData order by supplier";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                String sup = rS.getString("supplier");
                cbSupplier.getItems().remove(sup);
                cbSupplier.getItems().add(sup);
            }
            pS.close();
            rS.close();
        }catch (SQLException ex) {
            Logger.getLogger(AddBrandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private boolean btnAddSupplyerOnAction(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            Group supOverview = (Group)FXMLLoader.load(AddBrandController.class.getResource("AddSupplier.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Supplier");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(supOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddSupplierController controller = new AddSupplierController();
            controller.setDialogStage(dialogStage);
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isSavedClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    private void handleSave(ActionEvent event) throws SQLException {
        if (isInputValid()) {
                                                           
                        String brand = brandName.getText();
                        String supCombo = cbSupplier.getSelectionModel().getSelectedItem();
                        String desc = description.getText();
                        String add = addedBy.getText();
                        Object dateL = date.getValue();
                        Object timeL = time.getTime();
                        String id = brandIDLabel.getText();
                        
                String query = "INSERT INTO brandData (brand, supplier, description, added_by, date, time, brand_id) "
                        + "VALUES(?,?,?,?,?,?,?)";
                    pS = null;
                    
                    try{
                        pS = conn.prepareStatement(query);
                        
                        pS.setString(1, brand);
                        pS.setString(2, supCombo);
                        pS.setString(3, desc);
                        pS.setString(4, add);
                        pS.setObject(5, dateL);
                        pS.setObject(6, timeL);
                        pS.setString(7, id);
                        
                    } catch(SQLException e){
                        e.printStackTrace();
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
                         String brand = brandName.getText();
                        String supCombo = cbSupplier.getValue();
                        String desc = description.getText();
                        String add = addedBy.getText();
                        Object dateL = date.getValue();
                        Object timeL = time.getTime();
                        String id = brandIDLabel.getText();
                         
                        String data = "update brandData set brand=?, supplier=?, description=?, added_by=?, date=?, time=?, brand_id=?"
                                + " where brand_id='"+temp+"'";
                try {    
                    
                    pS = conn.prepareStatement(data);
                    
                        pS.setString(1, brand);
                        pS.setString(2, supCombo);
                        pS.setString(3, desc);
                        pS.setString(4, add);
                        pS.setObject(5, dateL);
                        pS.setObject(6, timeL);
                        pS.setString(7, id);
                        pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddBrandController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void btnCloseOnAction(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    
    public void setBrandData(BrandModel brandModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM brandData where brand_id ='"+brandModel.getBrandId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                brandName.setText(rs.getString("brand"));
                cbSupplier.setValue(rs.getString("supplier"));            
                description.setText(rs.getString("description"));            
                addedBy.setText(rs.getString("added_by"));
                date.setValue(LocalDate.parse(rs.getObject("date").toString()));            
                time.setTime(LocalTime.parse(rs.getObject("time").toString()));            
                brandIDLabel.setText(rs.getString("brand_id"));
            }
            temp = brandIDLabel.getText();
           
        }catch (Exception e){
            e.printStackTrace();
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

        if (cbSupplier.getSelectionModel().getSelectedItem() == null && cbSupplier.getPromptText().isEmpty()) {
            errorMessage += "No valid Supplier(Must be a string)!\n"; 
        }
        if (brandName.getText() == null || brandName.getText().length() == 0) {
            errorMessage += "No valid brand!\n"; 
        } 
       
        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "No valid Desc(Must Be a String!)\n"; 
        }
        
        if (addedBy.getText() == null || addedBy.getText().length() == 0) {
            errorMessage += "No valid Input!\n"; 
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
