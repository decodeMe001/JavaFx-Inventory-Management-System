package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.BrandModel;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Dada abiola
 */
public class BrandController implements Initializable{
    
    @FXML
    private TableView<BrandModel> tblBrand;

    @FXML
    private TableColumn<?, ?> tblCollumId;

    @FXML
    private TableColumn<BrandModel, String> tblCollumName;

    @FXML
    private TableColumn<BrandModel, String> tblCollumSupplyer;

    @FXML
    private TableColumn<BrandModel, String> tblCollumDescription;

    @FXML
    private TableColumn<BrandModel, String> tblCollumCreator;

    @FXML
    private TableColumn<BrandModel, Object> tblClmDate;

    @FXML
    private TableColumn<BrandModel, Object> tblClmTime1;
    
    @FXML
    private TableColumn<BrandModel, String> tblClmBrandID;

    @FXML
    private JFXTextField tfSearch;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnAddBrand;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;
    
    public StockController stockApp;
    Connection conn = SqliteConnection.Connector();  
    private final ObservableList<BrandModel> brandData = FXCollections.observableArrayList();
    FilteredList<BrandModel> filteredData = new FilteredList<>(brandData, e->true);

    Stage dialogStage;
    private Stage primaryStage;
    private boolean okClicked = false;
    PreparedStatement pS = null;
    ResultSet rs = null;
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSavedClicked() {
        return okClicked;
    }
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
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
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<BrandModel> getBrandData() {
        return brandData;
    }
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
       assert tblBrand !=null;
        // Initialize the stock table with the columns.
        tblCollumName.setCellValueFactory(cellData -> cellData.getValue().brandNameProperty());
        tblCollumSupplyer.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        tblCollumDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionInfoProperty());
        tblCollumCreator.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        tblClmDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tblClmTime1.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tblClmBrandID.setCellValueFactory(cellData -> cellData.getValue().brandIdProperty());
            
        loadDataBaseData();
       
    }
    
    public void loadDataBaseData(){
                           String query = "SELECT * FROM brandData";

        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                brandData.add(new BrandModel(
                            rs.getString("brand"),
                            rs.getString("supplier"),
                            rs.getString("description"),
                            rs.getString("added_by"),
                            rs.getObject("date"),
                            rs.getObject("time"),
                            rs.getString("brand_id")
                            
                ));
               tblBrand.setItems(brandData);
            }
            pS.close();
            rs.close();
        }catch (Exception e){
        }
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getBrandData().clear();
        tfSearch.clear();
        
        tblBrand.setItems(brandData);
        // Initialize the supplier table with the columns.
        tblCollumName.setCellValueFactory(cellData -> cellData.getValue().brandNameProperty());
        tblCollumSupplyer.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        tblCollumDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionInfoProperty());
        tblCollumCreator.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        tblClmDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tblClmTime1.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tblClmBrandID.setCellValueFactory(cellData -> cellData.getValue().brandIdProperty());
        loadDataBaseData();
    }
    

    @FXML
    void tfSearchOnKeyPress(KeyEvent event) {

    }
    
    @FXML
    boolean handleAddNew(ActionEvent event) {
        try {
           // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BrandController.class.getResource("AddBrand.fxml"));
            Group brandOverview = (Group) loader.load();

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
            AddBrandController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUpdateDisable();
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isSavedClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        BrandModel getSelectedRow = (BrandModel)tblBrand.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from brandData where brand=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getBrandName());
                pS.executeUpdate();
                }catch(SQLException e){
                }finally{
                      try{  
                        //conn.close();
                        pS.close();
                        rs.close();
                      }catch(Exception e){
                          
                      }
                }
            okClicked = true;
            if(okClicked){
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Notice!!!");
                tray.setMessage("Item Successfully Deleted.");
                tray.setNotificationType(notificationType);
                tray.showAndDismiss(Duration.millis(3000));
            }
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("Selected An ITEM to delete");
               alert.setContentText("Please select an ITEM on the table.");
               alert.showAndWait();
           }
        
        getBrandData().clear();
        loadDataBaseData();
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        BrandModel getSelectedRow = (BrandModel)tblBrand.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
                    boolean okClicked = showBrandUpdateDialog(getSelectedRow);
            
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Brand Selected");
               alert.setContentText("Please select an ITEM on the table.");
               alert.showAndWait();
           }
        
    }
    public boolean showBrandUpdateDialog(BrandModel brandModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BrandController.class.getResource("AddBrand.fxml"));
            Group brandOverview = (Group) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(brandOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddBrandController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBrandData(brandModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            return false;
        }
    }
    
    @FXML
    public void searchBrand(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super BrandModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getBrandName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getSupplierName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<BrandModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblBrand.comparatorProperty());
        tblBrand.setItems(sortedData);
    }
    
}
