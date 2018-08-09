package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.SupplierModel;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
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
 *
 * @author Dada abiola
 */
public class SupplierController implements Initializable{
    
    Connection conn = SqliteConnection.Connector();  
    private final ObservableList<SupplierModel> supplierData = FXCollections.observableArrayList();
    FilteredList<SupplierModel> filteredData = new FilteredList<>(supplierData, e->true);
    
    
    PreparedStatement pS = null;
    ResultSet rs = null;
    private Stage primaryStage;
    private boolean okClicked = false;
    
    @FXML
    private AnchorPane apContent;
    @FXML
    private Label lblCaption;
    @FXML
    private Button btnClose;
    
    @FXML
    private AnchorPane acContent;

    @FXML
    private JFXTextField tfSearch;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<SupplierModel> tblSupplyer;

    @FXML
    private TableColumn<SupplierModel, String> clmSupplyerName;

    @FXML
    private TableColumn<SupplierModel, String> clmSupplyerPhoneNumber;

    @FXML
    private TableColumn<SupplierModel, String> clmSupplyerAddress;

    @FXML
    private TableColumn<SupplierModel, Object> clmSuppliedDate;

    @FXML
    private TableColumn<SupplierModel, Object> clmSupplierTimeSupplied;

    @FXML
    private TableColumn<SupplierModel, String> clmSupplierPayment;
    
    @FXML
    private TableColumn<SupplierModel, String> clmSupplierID;
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<SupplierModel> getSupplierData() {
        return supplierData;
    }
     
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSavedClicked() {
        return okClicked;
    }
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
       assert tblSupplyer !=null;
        // Initialize the stock table with the columns.
        clmSupplyerName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        clmSupplyerPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        clmSupplyerAddress.setCellValueFactory(cellData -> cellData.getValue().addressNoteProperty());
        clmSuppliedDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmSupplierTimeSupplied.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        clmSupplierPayment.setCellValueFactory(cellData -> cellData.getValue().paymentInfoProperty());
        clmSupplierID.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty());
                
        loadDataBaseData();
       
    }
    
    public void loadDataBaseData(){
            String query = "SELECT * FROM supplierData";

        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                supplierData.add(new SupplierModel(
                            rs.getString("supplier"),
                            rs.getString("phone_num"),
                            rs.getString("address"),
                            rs.getObject("date_supplied"),
                            rs.getObject("time_supplied"),
                            rs.getString("payment"),
                            rs.getString("supplier_id")
                            
                ));
               tblSupplyer.setItems(supplierData);
            }
            pS.close();
            rs.close();
        }catch (Exception e){
        }
    }
     @FXML
    public void searchSupplier(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super SupplierModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getSupplierName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getPaymentInfo().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<SupplierModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblSupplyer.comparatorProperty());
        tblSupplyer.setItems(sortedData);
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getSupplierData().clear();
        tfSearch.clear();
        
        tblSupplyer.setItems(supplierData);
        // Initialize the supplier table with the columns.
        clmSupplyerName.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        clmSupplyerPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        clmSupplyerAddress.setCellValueFactory(cellData -> cellData.getValue().addressNoteProperty());
        clmSuppliedDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmSupplierTimeSupplied.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        clmSupplierPayment.setCellValueFactory(cellData -> cellData.getValue().paymentInfoProperty());
        clmSupplierID.setCellValueFactory(cellData -> cellData.getValue().supplierIdProperty());
        loadDataBaseData();
    }

    @FXML
    public boolean handleAddNew(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CurrentStockController.class.getResource("AddSupplier.fxml"));
            Group supplierOverview = (Group) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Supplier");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(supplierOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddSupplierController controller = loader.getController();
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
    void handleUpdate(ActionEvent event) {
        SupplierModel getSelectedRow = (SupplierModel)tblSupplyer.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
                    boolean okClicked = showSupplierUpdateDialog(getSelectedRow);
            
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Supplier Selected");
               alert.setContentText("Please select an ITEM on the table.");
               alert.showAndWait();
           }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        SupplierModel getSelectedRow = (SupplierModel)tblSupplyer.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from supplierData where supplier_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getSupplierId());
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
        
        getSupplierData().clear();
        loadDataBaseData();
    }
    
    public boolean showSupplierUpdateDialog(SupplierModel supplierModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SupplierController.class.getResource("AddSupplier.fxml"));
            Group supplierOverview = (Group) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(supplierOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddSupplierController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSupplierData(supplierModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    void tblSupplyerOnClick(MouseEvent event) {

    }

    @FXML
    void tblSupplyerOnKeyPress(KeyEvent event) {

    }

    @FXML
    void tfSearchOnType(KeyEvent event) {

    }
    
}
