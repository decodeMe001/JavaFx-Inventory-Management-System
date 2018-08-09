package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.UnitModel;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class UnitController implements Initializable {
    
     @FXML
    private TableView<UnitModel> tblViewUnit;

    @FXML
    private TableColumn<UnitModel, Integer> clmUnitName;

    @FXML
    private TableColumn<UnitModel, String> clmUnitBrand;

    @FXML
    private TableColumn<UnitModel, String> clmUnitCreator;

    @FXML
    private TableColumn<UnitModel, Object> clmUnitCreateDate;

    @FXML
    private TableColumn<UnitModel, Object> clmUnitCreateTime1;
    
    @FXML
    private TableColumn<UnitModel, String> clmUnitID;

   @FXML
    private JFXTextField tfSearch;

    @FXML
    private Button btnRefresh;
    
    public StockController stockApp;
    private Stage primaryStage;
    Connection conn = SqliteConnection.Connector();  
    private static boolean hasData = false;
    private final ObservableList<UnitModel> unitData = FXCollections.observableArrayList();
    FilteredList<UnitModel> filteredData = new FilteredList<>(unitData, e->true);
    private boolean okClicked = false;
    PreparedStatement pS = null;
    ResultSet rs = null;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
       assert tblViewUnit !=null;
        // Initialize the stock table with the columns.
        clmUnitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty().asObject());
        clmUnitBrand.setCellValueFactory(cellData -> cellData.getValue().brandInfoProperty());
        clmUnitCreator.setCellValueFactory(cellData -> cellData.getValue().createdByProperty());
        clmUnitCreateDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmUnitCreateTime1.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        clmUnitID.setCellValueFactory(cellData -> cellData.getValue().unitIDProperty());
            
        loadDataBaseData();
       
    }
    
    
    public void loadDataBaseData(){
                String query = "SELECT * FROM unitdata";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                unitData.add(new UnitModel(
                            rs.getInt("unit"),
                            rs.getString("brand"),
                            rs.getString("created_by"),
                            rs.getObject("date"),
                            rs.getObject("time"),
                            rs.getString("unit_id")
                                                       
                ));
               tblViewUnit.setItems(unitData);
            }
            pS.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<UnitModel> getUnitData() {
        return unitData;
    }
    
     @FXML
    public void searchUnit(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super UnitModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getBrandInfo().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<UnitModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblViewUnit.comparatorProperty());
        tblViewUnit.setItems(sortedData);
    }
    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getUnitData().clear();
        tfSearch.clear();
        
        tblViewUnit.setItems(unitData);
        // Initialize the stock table with the columns.
        clmUnitName.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty().asObject());
        clmUnitBrand.setCellValueFactory(cellData -> cellData.getValue().brandInfoProperty());
        clmUnitCreator.setCellValueFactory(cellData -> cellData.getValue().createdByProperty());
        clmUnitCreateDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmUnitCreateTime1.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        clmUnitID.setCellValueFactory(cellData -> cellData.getValue().unitIDProperty());
        
        loadDataBaseData();
    }
    
     @FXML
     boolean handleAddNew(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UnitController.class.getResource("AddUnit.fxml"));
            AnchorPane unitOverview = (AnchorPane) loader.load();

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
            AddUnitController controller = loader.getController();
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
        UnitModel getSelectedRow = (UnitModel)tblViewUnit.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from unitdata where unit_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getUnitID());
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
        
        getUnitData().clear();
        loadDataBaseData();
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        UnitModel getSelectedRow = (UnitModel)tblViewUnit.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
                    boolean okClicked = showUnitUpdateDialog(getSelectedRow);
            
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
    
      public boolean showUnitUpdateDialog(UnitModel unitModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UnitController.class.getResource("AddUnit.fxml"));
            AnchorPane unitOverview = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(unitOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddUnitController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUnitData(unitModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            return false;
        }
    }
    
}
