package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CategoryModel;
import com.iSoftTech.inventoryms.model.StockModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
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
public class AddCategoryController implements Initializable {
    
    Stage dialogStage, primaryStage;
    public CategoryController cSC;
    private boolean okClicked = false;
    StockModel stockM;
    Connection conn = SqliteConnection.Connector(); 
    private static boolean hasData = false;
    PreparedStatement pS;
    ResultSet rS;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @FXML
    private ComboBox<String> cbSupplierData;

    @FXML
    private Button btnAddSupplier;
    
    @FXML
    private ComboBox<String> cbBrandData;

    @FXML
    private Button btnAddBrand;

    @FXML
    private JFXTextField category;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXDatePicker time;

    @FXML
    private JFXTextField addedBy;

    @FXML
    private JFXTextField description;

    @FXML
    private Label lblHeaderContent;

    @FXML
    private Button btnClose;
    
    @FXML
    private Label catIDLabel;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button updateButton;
     
    static String temp;
    
    public String id;
    private String supplierName;
    private String supplierId;
    private String brandName;
    private String brandId;
    private String catagoryName;
    private String catagoryId;
    private String unitId;
    
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
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSavedClicked() {
        return okClicked;
    }
    public void setSaveDisable(){
        saveButton.setDisable(true);
    }
    public void setUpdateDisable(){
        updateButton.setDisable(true);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        catIDLabel.setText(randomAplphaNum(5));
        
    }
    
    public void setCatData(CategoryModel catModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM categoryData where cat_id = '"+catModel.getCatId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                cbSupplierData.setValue(rs.getString("supplier"));
                cbBrandData.setValue(rs.getString("brand"));            
                addedBy.setText(rs.getString("category_creator")); 
                category.setText(rs.getString("cat_name"));
                description.setText(rs.getString("description"));
                date.setValue(LocalDate.parse(rs.getObject("date").toString()));            
                time.setTime(LocalTime.parse(rs.getObject("time").toString()));            
                catIDLabel.setText(rs.getString("cat_id"));
            }
            temp = catIDLabel.getText();
           
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
    boolean btnAddBrandOnAction(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            Group brandOverview = (Group)FXMLLoader.load(AddStockDataController.class.getResource("AddBrand.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Brand");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(brandOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddBrandController controller = new AddBrandController();
            controller.setDialogStage(dialogStage);
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isSavedClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    boolean btnAddSupplierOnAction(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            Group supOverview = (Group)FXMLLoader.load(AddStockDataController.class.getResource("AddSupplier.fxml"));

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
    void cbBrandNameOnClick(MouseEvent event) {
        cbBrandData.getItems().clear();
        try{
            String sql = "SELECT * FROM brandData order by brand";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                String brand = rS.getString("brand");
                cbBrandData.getItems().remove(brand);
                cbBrandData.getItems().add(brand);
            }
            pS.close();
            rS.close();
        }catch (SQLException ex) {
            Logger.getLogger(AddCategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void cbSupplierNameOnClick(MouseEvent event) {
        cbSupplierData.getItems().clear();
        try{
            String sql = "SELECT * FROM supplierData order by supplier";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                String sup = rS.getString("supplier");
                cbSupplierData.getItems().remove(sup);
                cbSupplierData.getItems().add(sup);
            }
            pS.close();
            rS.close();
        }catch (SQLException ex) {
            Logger.getLogger(AddCategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @FXML
    private void handleSave(ActionEvent event) throws SQLException {
        if (isInputValid()) {
                                                           
                        String cat = category.getText();
                        String brand = cbBrandData.getSelectionModel().getSelectedItem();
                        String supCombo = cbSupplierData.getSelectionModel().getSelectedItem();
                        String desc = description.getText();
                        String add = addedBy.getText();
                        Object dateL = date.getValue();
                        Object timeL = time.getTime();
                        String id = catIDLabel.getText();
                        
                String query = "INSERT INTO categoryData (cat_name, brand, supplier, category_creator, date, time, description, cat_id) "
                        + "VALUES(?,?,?,?,?,?,?,?)";
                    pS = null;
                    
                    try{
                        pS = conn.prepareStatement(query);
                        
                        pS.setString(1, cat);
                        pS.setString(2, brand);
                        pS.setString(3, supCombo);
                        pS.setString(4, add);
                        pS.setObject(5, dateL);
                        pS.setObject(6, timeL);
                        pS.setString(7, desc);
                        pS.setString(8, id);
                        
                        
                    } catch(SQLException e){
                    }finally{
                        pS.execute();
                        pS.close();
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
                    }               
        }
    }
    
    @FXML
    void handleUpdate(ActionEvent event) {
        if(isInputValid()){
                        String cat = category.getText();
                        String brand = cbBrandData.getSelectionModel().getSelectedItem();
                        String supCombo = cbSupplierData.getSelectionModel().getSelectedItem();
                        String desc = description.getText();
                        String add = addedBy.getText();
                        Object dateL = date.getValue();
                        Object timeL = time.getTime();
                        String id = catIDLabel.getText();
                        
                String data = "update categoryData set cat_name=?, brand=?, supplier=?, category_creator=?, date=?, time=?"
                                + "description=?, cat_id=? where cat_id='"+temp+"'";
                try {    
                    
                    pS = conn.prepareStatement(data);
                    
                        pS.setString(1, cat);
                        pS.setString(2, brand);
                        pS.setString(3, supCombo);
                        pS.setString(4, add);
                        pS.setObject(5, dateL);
                        pS.setObject(6, timeL);
                        pS.setString(7, desc);
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

    @FXML
    private void btnCloseOnAction(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (cbSupplierData.getSelectionModel().isEmpty()) {
            errorMessage += "No valid Supplier(Must be a string)!\n"; 
        }
        if (cbBrandData.getSelectionModel().isEmpty()) {
            errorMessage += "No valid Brand(Must be a string)!\n"; 
        }
       
        if (category.getText() == null || category.getText().length() == 0) {
            errorMessage += "No valid Category(Must Be a String!)\n"; 
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
