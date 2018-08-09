package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.RemarkModel;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class RemarkController implements Initializable {
    
    @FXML
    private TableView<RemarkModel> tblViewRMA;
     
    @FXML
    private JFXTextField tfSearch;

    @FXML
    private TableColumn<?, ?> clmRMAId;

    @FXML
    private TableColumn<RemarkModel, String> clmRMAName;

    @FXML
    private TableColumn<RemarkModel, String> clmCustomerSupplier;

    @FXML
    private TableColumn<RemarkModel, String> clmAddedBy;

    @FXML
    private TableColumn<RemarkModel, Object> clmDeadline;

    @FXML
    private TableColumn<RemarkModel, Object> clmRMADate;
    
    @FXML
    private TableColumn<RemarkModel, String> clmRMAID;
    
    public StockController stockApp;
    Connection conn = SqliteConnection.Connector();  
    private final ObservableList<RemarkModel> remarkData = FXCollections.observableArrayList();
    FilteredList<RemarkModel> filteredData = new FilteredList<>(remarkData, e->true);
    private Stage primaryStage;
    PreparedStatement pS = null;
    ResultSet rs = null;
    private boolean okClicked = false;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
       assert tblViewRMA !=null;
        // Initialize the stock table with the columns.
        clmRMAName.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());
        clmCustomerSupplier.setCellValueFactory(cellData -> cellData.getValue().customerDebitCreditProperty());
        clmAddedBy.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        clmDeadline.setCellValueFactory(cellData -> cellData.getValue().deadlineDateProperty());
        clmRMADate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmRMAID.setCellValueFactory(cellData -> cellData.getValue().remarkIdProperty());
          
        loadDataBaseData();
       
    }
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<RemarkModel> getRemarkData() {
        return remarkData;
    }
    public void loadDataBaseData(){
        String query = "SELECT * FROM remarkData";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                remarkData.add(new RemarkModel(
                            rs.getString("product_name"),
                            rs.getString("payment_remark"),
                            rs.getString("added_by"),
                            rs.getObject("deadline"),
                            rs.getObject("date"),
                            rs.getString("remark_id")
                            
                ));
               tblViewRMA.setItems(remarkData);
            }
            pS.close();
            rs.close();
        }catch (Exception e){
        }
    }
    
     @FXML
    void btnRefreshData(ActionEvent event) {
        getRemarkData().clear();
        tfSearch.clear();
        
        tblViewRMA.setItems(remarkData);
         // Initialize the stock table with the columns.
        clmRMAName.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());
        clmCustomerSupplier.setCellValueFactory(cellData -> cellData.getValue().customerDebitCreditProperty());
        clmAddedBy.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        clmDeadline.setCellValueFactory(cellData -> cellData.getValue().deadlineDateProperty());
        clmRMADate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmRMAID.setCellValueFactory(cellData -> cellData.getValue().remarkIdProperty());  
        loadDataBaseData();
    }

     @FXML
    boolean handleAddNew(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RemarkController.class.getResource("AddRemark.fxml"));
            Group remOverview = (Group) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Remark");           
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(remOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddRemarkController controller = loader.getController();
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
    public void searchRemark(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super RemarkModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getStockName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getAddedBy().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<RemarkModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblViewRMA.comparatorProperty());
        tblViewRMA.setItems(sortedData);
    }

    @FXML
    void handleDelete(ActionEvent event) {
        RemarkModel getSelectedRow = (RemarkModel)tblViewRMA.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from remarkData where remark_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getRemarkId());
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
                tray.setMessage("Remark Successfully Deleted.");
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
        
        getRemarkData().clear();
        loadDataBaseData();
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        RemarkModel getSelectedRow = (RemarkModel)tblViewRMA.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
                    boolean okClicked = showRemarkUpdateDialog(getSelectedRow);
            
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
    
      public boolean showRemarkUpdateDialog(RemarkModel remModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RemarkController.class.getResource("AddRemark.fxml"));
            Group remOverview = (Group) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(remOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddRemarkController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRemData(remModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            return false;
        }
    }
    
}
