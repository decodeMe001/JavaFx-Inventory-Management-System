package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CustomerModel;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class CustomerController implements Initializable {
    
    @FXML
    private AnchorPane acCustomerMainContent;

    @FXML
    private TableView<CustomerModel> tblCustomer;

    @FXML
    private TableColumn<CustomerModel, String> tblClmName;

    @FXML
    private TableColumn<CustomerModel, String> tblClmContNo;

    @FXML
    private TableColumn<CustomerModel, String> tblClmAddres;

    @FXML
    private TableColumn<CustomerModel, Object> tblClmDate;

    @FXML
    private TableColumn<CustomerModel, String> tblClmAddBy;

    @FXML
    private TableColumn<CustomerModel, Double> tblClmTotalBuy;
    
    @FXML
    private TableColumn<CustomerModel, String> tblClmRemark;
    
    @FXML
    private TableColumn<CustomerModel, String> tblClmID;

   @FXML
    private JFXTextField tfSearch;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;
    
    private Stage primaryStage;
    Connection conn = SqliteConnection.Connector();  
    private static boolean hasData = false;
    public ObservableList<CustomerModel> customerData = FXCollections.observableArrayList();
    FilteredList<CustomerModel> filteredData = new FilteredList<>(customerData, e->true);
    private boolean okClicked = false;
    PreparedStatement pS = null;
    ResultSet rs = null;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
       
        assert tblCustomer !=null;
        
        // Initialize the stock table with the columns.
        tblClmName.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        tblClmContNo.setCellValueFactory(cellData -> cellData.getValue().phoneNumProperty());
        tblClmAddres.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        tblClmDate.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        tblClmAddBy.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        tblClmTotalBuy.setCellValueFactory(cellData -> cellData.getValue().totalPurchaseProperty().asObject());
        tblClmRemark.setCellValueFactory(cellData -> cellData.getValue().remarkCustomerProperty());
        tblClmID.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        
        try {
            loadDataBaseData();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        getCustomerData();
    }
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<CustomerModel> getCustomerData() {
        return customerData;
    }
    
    public void loadDataBaseData() throws SQLException{
       
                    String query = "SELECT * FROM customerInfo";
                    try{
                        pS = conn.prepareStatement(query);
                        rs = pS.executeQuery();
                        while(rs.next()){
                            customerData.add(new CustomerModel(
                                        rs.getString("name"),
                                        rs.getString("phone"),
                                        rs.getString("address"),
                                        rs.getDouble("total"),
                                        rs.getObject("date"),
                                        rs.getString("add_by"),
                                        rs.getString("remark"),
                                        rs.getString("customer_id")
                                    ));
                           tblCustomer.setItems(customerData);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally{
                                  try{  
                                    //conn.close();
                                    pS.close();
                                    rs.close();
                                  }catch(Exception e){

                                  }
                            }
    }
    
    @FXML
    boolean handleAddNew(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CustomerController.class.getResource("AddCustomer.fxml"));
            AnchorPane customerOverview = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Customer Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(customerOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddCustomerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUpdateDisable();
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    void handleDelete(ActionEvent event) throws SQLException {
        CustomerModel getSelectedRow = (CustomerModel)tblCustomer.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from customerInfo where customer_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getCustomerId());
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
                tray.setMessage("Customer Successfully Deleted.");
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
        
        getCustomerData().clear();
        loadDataBaseData();
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        CustomerModel getSelectedRow = tblCustomer.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
              boolean okClicked = showStaffUpdateDialog(getSelectedRow);
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Customer Selected");
               alert.setContentText("Please select a CUSTOMER on the table.");
               alert.showAndWait();
           }
    }
    public boolean showStaffUpdateDialog(CustomerModel customerModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CustomerController.class.getResource("AddCustomer.fxml"));
            AnchorPane customerOverview = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(customerOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddCustomerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomerData(customerModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) throws SQLException {
        getCustomerData().clear();
        tfSearch.clear();
        
        tblCustomer.setItems(customerData);
        // Initialize the stock table with the columns.
        tblClmName.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        tblClmContNo.setCellValueFactory(cellData -> cellData.getValue().phoneNumProperty());
        tblClmAddres.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        tblClmDate.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        tblClmAddBy.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        tblClmTotalBuy.setCellValueFactory(cellData -> cellData.getValue().totalPurchaseProperty().asObject());
        tblClmRemark.setCellValueFactory(cellData -> cellData.getValue().remarkCustomerProperty());
        tblClmID.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        
        loadDataBaseData();
    }
    
     @FXML
    public void searchCustomer(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super CustomerModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getCustomerName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getRemarkCustomer().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<CustomerModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblCustomer.comparatorProperty());
        tblCustomer.setItems(sortedData);
    }

    @FXML
    void tblCustomerOnClick(MouseEvent event) {

    }

    @FXML
    void tfSearchOnKeyReleased(KeyEvent event) {

    }

    
    
}
