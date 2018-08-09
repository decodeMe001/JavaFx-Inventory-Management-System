package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.StockModel;
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
import javafx.scene.layout.AnchorPane;
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
public class AddStockDataController implements Initializable {
    
    Stage dialogStage;
    private Stage primaryStage;
    public CurrentStockController cSC;
    private boolean okClicked = false;
    StockModel stockModel;
    Connection conn = SqliteConnection.Connector();
    PreparedStatement pS = null;
    ResultSet rS = null;
    private static final String ALPHA_NUM_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    @FXML
    private Label prodId;

    @FXML
    private JFXTextField nameInput;

    @FXML
    private JFXTextField qtyInput;

    @FXML
    private JFXTextField descriptionLabel;

    @FXML
    private JFXTextField purchaseInput;

    @FXML
    private JFXDatePicker timePickLabel;

    @FXML
    private JFXDatePicker datePickLabel;

    @FXML
    private JFXTextField sellingPrice;

    @FXML
    private JFXTextField addedBy;
    
    @FXML
    private Button btnAddUnit;

    @FXML
    private ComboBox<Integer> cbUnit;

    @FXML
    private ComboBox<String> cbSupplier;

    @FXML
    private ComboBox<String> cbBrand;

    @FXML
    private ComboBox<String> cbCategory;

    @FXML
    private Button btnAddSupplier;

    @FXML
    private Button btnAddBrand;

    @FXML
    private Button btnAddCat;
    
    @FXML
    public Button saveButton;
    
    @FXML
    private JFXDatePicker expirationLabel;

    @FXML
    public Button updateButton;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
       prodId.setText(randomAplphaNum(5));
    }
    
    public static String randomAplphaNum(int count){
       StringBuilder builder = new StringBuilder();
       while(count-- != 0){
           int character = (int)(Math.random()*ALPHA_NUM_STRING.length());
           builder.append(ALPHA_NUM_STRING.charAt(character));
       }
       return builder.toString();
    }
    private static String temp;
    
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

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    //Handle Save Button
    @FXML
    private void handleSave(ActionEvent event) throws SQLException {
        
        if (isInputValid()){                                  
                        String id = prodId.getText();
                        String name = nameInput.getText();
                        Integer quantity = Integer.parseInt(qtyInput.getText());
                        Integer unitValue = cbUnit.getSelectionModel().getSelectedItem();
                        String supply = cbSupplier.getSelectionModel().getSelectedItem();
                        String brand = cbBrand.getSelectionModel().getSelectedItem();
                        String category = cbCategory.getSelectionModel().getSelectedItem();
                        Double purchasePrice = Double.parseDouble(purchaseInput.getText());
                        Double sellPrice = Double.parseDouble(sellingPrice.getText());
                        Object date = datePickLabel.getValue();
                        Object time = timePickLabel.getTime();
                        String addBy = addedBy.getText();
                        String describeIt = descriptionLabel.getText();
                        Object expire = expirationLabel.getValue();
                        
                        
                String query = "INSERT INTO stockData (prodId, name, quantity, unit, supplier, brand, category, purchase_price, "
                        + "sell_price, date, time, added_by, description, expireDate) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                         
                    try{
                        
                        pS = conn.prepareStatement(query);
                        pS.setString(1, id);
                        pS.setString(2, name);
                        pS.setInt(3, quantity);
                        pS.setInt(4, unitValue);
                        pS.setString(5, supply);
                        pS.setString(6, brand);
                        pS.setString(7, category);
                        pS.setDouble(8, purchasePrice);
                        pS.setDouble(9, sellPrice);
                        pS.setObject(10, date);
                        pS.setObject(11, time);
                        pS.setString(12, addBy);
                        pS.setString(13, describeIt);
                        pS.setObject(14, expire);
         
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
    private void handleUpdate(ActionEvent event) throws SQLException {
        
       if(isInputValid()){
                        String id = prodId.getText();
                        String name = nameInput.getText();
                        Integer quantity = Integer.parseInt(qtyInput.getText());
                        Integer unitValue = cbUnit.getSelectionModel().getSelectedItem();
                        String supply = cbSupplier.getValue();
                        String brand = cbBrand.getValue();
                        String category = cbCategory.getValue();
                        Double purchasePrice = Double.parseDouble(purchaseInput.getText());
                        Double sellPrice = Double.parseDouble(sellingPrice.getText());
                        Object date = datePickLabel.getValue();
                        Object time = timePickLabel.getTime();
                        String addBy = addedBy.getText();
                        String describeIt = descriptionLabel.getText();
                        Object expire = expirationLabel.getValue();
                        
                        String data = "UPDATE stockData SET prodId=?, name=?, quantity=?, unit=?, "
                            + "supplier=?, brand=?, category=?,"
                            + " purchase_price=?, sell_price=?, date=?, time=?, added_by=?, description=?, expireDate=?  where prodId='"+temp+"'";
                try {    
                    
                    pS = conn.prepareStatement(data);
                    
                    pS.setString(1, id);
                    pS.setString(2, name);
                    pS.setInt(3, quantity);
                    pS.setInt(4, unitValue);
                    pS.setString(5, supply);
                    pS.setString(6, brand);
                    pS.setString(7, category);
                    pS.setDouble(8, purchasePrice);
                    pS.setDouble(9, sellPrice);
                    pS.setObject(10, date);
                    pS.setObject(11, time);
                    pS.setString(12, addBy);
                    pS.setString(13, describeIt);
                    pS.setObject(14, expire);
                    pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddStockDataController.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
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
    void cbAddCategory(MouseEvent event) {
        cbCategory.getItems().clear();
        cbCategory.getValue();
        try{
            String sql = "SELECT * FROM categoryData order by cat_name";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                String cat = rS.getString("cat_name");
                cbSupplier.getItems().remove(cat);
                cbCategory.getItems().add(cat);
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
    void cbAddSupplier(MouseEvent event) {
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
    void cbAddUnit(MouseEvent event) {
        cbUnit.getItems().clear();
        cbUnit.getValue();
        
        try{
            String sql = "SELECT * FROM unitdata order by unit";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                Integer unit = rS.getInt("unit");
                cbUnit.getItems().add(unit);
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
    void cmBoxCategoryAction(ActionEvent event) {
        
        cbCategory.getValue();
        try {
            pS = conn.prepareStatement("select * from categoryData where cat_name=?");
            pS.setString(1, cbCategory.getSelectionModel().getSelectedItem().trim());
            rS = pS.executeQuery();
           
        } catch (SQLException e) {
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

    @FXML
    void cmBoxSupplierAction(ActionEvent event) {
        
        cbSupplier.getValue();
        try {
            pS = conn.prepareStatement("select * from supplierData where supplier=?");
            pS.setString(1, cbSupplier.getSelectionModel().getSelectedItem().trim());
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
    void cmBoxUnitAction(ActionEvent event) {
        
        cbUnit.getValue();
        try {
            pS = conn.prepareStatement("select * from unitdata where unit=?");
            pS.setInt(1, cbUnit.getSelectionModel().getSelectedItem());
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
    private void handleExit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
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
            //controller.setUpdateDisable();
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isSavedClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    boolean btnAddCategoryOnAction(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            Group catOverview = (Group)FXMLLoader.load(AddStockDataController.class.getResource("AddCategory.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Category");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(catOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddCategoryController controller = new AddCategoryController();
            controller.setDialogStage(dialogStage);
            //controller.setUpdateDisable();
        
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
    boolean btnAddUnitOnAction(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            AnchorPane unitOverview = (AnchorPane)FXMLLoader.load(AddStockDataController.class.getResource("AddUnit.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Unit");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(unitOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddUnitController controller = new AddUnitController();
            controller.setDialogStage(dialogStage);
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isSavedClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    
    public void setStockData(StockModel stockModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM stockData where prodId = '"+stockModel.getProductId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                prodId.setText(rs.getString("prodId"));
                nameInput.setText(rs.getString("name"));            
                qtyInput.setText(Integer.toString(rs.getInt("quantity")));            
                cbUnit.setValue(rs.getInt("unit"));
                cbSupplier.setValue(rs.getString("supplier"));
                cbBrand.setValue(rs.getString("brand"));            
                cbCategory.setValue(rs.getString("category"));
                purchaseInput.setText(Double.toString(rs.getDouble("purchase_price")));            
                sellingPrice.setText(Double.toString(rs.getDouble("sell_price"))); 
                datePickLabel.setValue(LocalDate.parse(rs.getObject("date").toString()));            
                timePickLabel.setTime(LocalTime.parse(rs.getObject("time").toString()));            
                addedBy.setText(rs.getString("added_by"));  
                descriptionLabel.setText(rs.getString("description"));
                expirationLabel.setValue(LocalDate.parse(rs.getObject("expireDate").toString())); 
                       
            }
            temp = prodId.getText();
           
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
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (prodId.getText() == null || prodId.getText().length() == 0) {
            errorMessage += "No valid Type(Must be a string)!\n"; 
        }
        if (nameInput.getText() == null || nameInput.getText().length() == 0) {
            errorMessage += "No valid Name(Must Be a String!)\n"; 
        }
        if (qtyInput.getText() == null || qtyInput.getText().length() == 0) {
            errorMessage += "No valid quantity!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(qtyInput.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid quantity (must be an integer)!\n"; 
            }
        }
        if (cbSupplier.getSelectionModel().getSelectedItem() == null && cbSupplier.getPromptText().isEmpty()) {
            errorMessage += "No Supplier Choosen(Must Be a String!)\n"; 
        }
        if (cbCategory.getSelectionModel().getSelectedItem() == null && cbCategory.getPromptText().isEmpty()) {
            errorMessage += "No Category Choosen(Must Be a String!)\n"; 
        }
        if (descriptionLabel.getText() == null || descriptionLabel.getText().length() == 0) {
            errorMessage += "Not Valid(Must Be a String!)\n"; 
        }
        if (cbBrand.getSelectionModel().getSelectedItem() == null && cbBrand.getPromptText().isEmpty()) {
            errorMessage += "No Brand Entry(Must Be a String!)\n"; 
        }
        if (cbUnit.getSelectionModel().getSelectedItem() == null && cbUnit.getPromptText().isEmpty()) {
            errorMessage += "No valid Unit!\n"; 
        }
        if (purchaseInput.getText() == null || purchaseInput.getText().length() == 0) {
            errorMessage += "No valid Amount!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Double.parseDouble(purchaseInput.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Amount (must be an Integer)!\n"; 
            }
        }
        if (sellingPrice.getText() == null || sellingPrice.getText().length() == 0) {
            errorMessage += "No valid Fee!\n"; 
        } else {
            // try to parse the postal code into an int.
            try {
                Double.parseDouble(sellingPrice.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Fee (must be an integer)!\n"; 
            }
        }
       
        if (addedBy.getText() == null || addedBy.getText().length() == 0) {
            errorMessage += "No valid Name!\n"; 
        } 
        
        
        if (datePickLabel.toString() == null || datePickLabel.toString().length() == 0) {
            errorMessage += "No valid Date Choosen!\n";
        } 
        if (timePickLabel.timeProperty() == null) {
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
