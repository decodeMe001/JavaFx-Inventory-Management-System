package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.StockModel;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
public class CurrentStockController implements Initializable {
        
    @FXML
    private StackPane spProductContent;

    @FXML
    private TableView<StockModel> tblViewCurrentStore;

    @FXML
    private TableColumn<StockModel, String> tblClmProductId;

    @FXML
    private TableColumn<StockModel, String> tblClmProductName;

    @FXML
    private TableColumn<StockModel, Integer> tblClmProductQuantity;

    @FXML
    private TableColumn<StockModel, Integer> tblClmProductUnit;

    @FXML
    private TableColumn<StockModel, String> tblClmProductSupplyer;

    @FXML
    private TableColumn<StockModel, String> tblClmProductBrand;

    @FXML
    private TableColumn<StockModel, String> tblClmProductCatagory;

    @FXML
    private TableColumn<StockModel, Double> tblClmProductPurchasePrice;

    @FXML
    private TableColumn<StockModel, Double> tblClmProductSellPrice;

    @FXML
    private TableColumn<StockModel, Object> tblClmProductdate;

    @FXML
    private TableColumn<StockModel, Object> tbcTimeInput;

    @FXML
    private TableColumn<StockModel, String> tblClmProductAddBy;

    @FXML
    private TableColumn<StockModel, String> tblClmProductdescription;
     @FXML
    private TableColumn<StockModel, String> tblClmProductExpire;

    @FXML
    private MenuItem miSellSelected;
    
    @FXML
    private JFXTextField tfSearch;

    @FXML
    private AnchorPane apCombobox;

    @FXML
    private ComboBox<String> cbSoteViewSupplyer;

    @FXML
    private ComboBox<String> cbSoteViewBrands;

    @FXML
    private ComboBox<String> cbSoteViewCatagory;
        
    private Stage primaryStage;
    StockModel stockModel;
    AddStockDataController addStock;
    Connection conn = SqliteConnection.Connector();  
    private static boolean hasData = false;
    public ObservableList<StockModel> stockData = FXCollections.observableArrayList();
    FilteredList<StockModel> filteredData = new FilteredList<>(stockData, e->true);
    private boolean isSetUpdateButtonClick;
    private boolean okClicked = false;
    PreparedStatement pS = null;
    ResultSet rs = null;
    
     
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
      
        assert tblViewCurrentStore !=null;
        // Initialize the stock table with the columns.
        tblClmProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty());
        tblClmProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tblClmProductQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblClmProductUnit.setCellValueFactory(cellData -> cellData.getValue().unitValueProperty().asObject());
        tblClmProductSupplyer.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        tblClmProductBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        tblClmProductCatagory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        tblClmProductPurchasePrice.setCellValueFactory(cellData -> cellData.getValue().purchaseProperty().asObject());
        tblClmProductSellPrice.setCellValueFactory(cellData -> cellData.getValue().sellProperty().asObject());
        tblClmProductdate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tbcTimeInput.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tblClmProductAddBy.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        tblClmProductdescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        tblClmProductExpire.setCellValueFactory(cellData -> cellData.getValue().expireDateProperty());
        
        loadDataBaseData();
        getStockData();
        
    }    
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<StockModel> getStockData() {
        return stockData;
    }
    
    public void loadDataBaseData(){
        String query = "SELECT * FROM stockData";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                stockData.add(new StockModel(
                            rs.getString("prodId"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getInt("unit"),
                            rs.getString("supplier"),
                            rs.getString("brand"),
                            rs.getString("category"),
                            rs.getDouble("purchase_price"),
                            rs.getDouble("sell_price"),
                            rs.getObject("date"),
                            rs.getObject("time"),
                            rs.getString("added_by"),
                            rs.getString("description"),
                            rs.getObject("expireDate")
                            
                ));
               
            }
            tblViewCurrentStore.setItems(stockData);
        }catch (Exception e){
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
    void handleDelete(ActionEvent event) {
        StockModel getSelectedRow = (StockModel)tblViewCurrentStore.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from stockData where prodId=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getProductId());
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
        
        getStockData().clear();
        loadDataBaseData();
   }
    
    public void getExpire(){
        
    }
    
    @FXML
    private boolean handleAddNew(ActionEvent event) {
        
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CurrentStockController.class.getResource("AddStockData.fxml"));
            AnchorPane stockOverview = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Stock Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(stockOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddStockDataController controller = loader.getController();
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
    boolean handleUpdate(ActionEvent event) {
        isSetUpdateButtonClick = true;
        StockModel getSelectedRow = (StockModel)tblViewCurrentStore.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
                    boolean okClicked = showStockUpdateDialog(getSelectedRow);
            
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Stock Selected");
               alert.setContentText("Please select an Item on the table.");
               alert.showAndWait();
           }
        return isSetUpdateButtonClick;
    }
     
    public boolean showStockUpdateDialog(StockModel stockModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CurrentStockController.class.getResource("AddStockData.fxml"));
            AnchorPane stockOverview = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(stockOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddStockDataController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStockData(stockModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
  
    @FXML
    public void btnRefreshData(ActionEvent event) {
        
        getStockData().clear();
        tfSearch.clear();
        cbSoteViewSupplyer.getItems().clear();
        cbSoteViewBrands.getItems().clear();
        cbSoteViewCatagory.getItems().clear();
        cbSoteViewSupplyer.setPromptText("Select Supplier");
        cbSoteViewBrands.setPromptText("Select Brands");
        cbSoteViewCatagory.setPromptText("Select Cat");
        
        tblViewCurrentStore.setItems(stockData);
        tblClmProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty());
        tblClmProductName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tblClmProductQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblClmProductUnit.setCellValueFactory(cellData -> cellData.getValue().unitValueProperty().asObject());
        tblClmProductSupplyer.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        tblClmProductBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        tblClmProductCatagory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        tblClmProductPurchasePrice.setCellValueFactory(cellData -> cellData.getValue().purchaseProperty().asObject());
        tblClmProductSellPrice.setCellValueFactory(cellData -> cellData.getValue().sellProperty().asObject());
        tblClmProductdate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tbcTimeInput.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        tblClmProductAddBy.setCellValueFactory(cellData -> cellData.getValue().addedByProperty());
        tblClmProductdescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        loadDataBaseData();
       
    }
    @FXML
    public void searchStock(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super StockModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getProductName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getBrand().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<StockModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblViewCurrentStore.comparatorProperty());
        tblViewCurrentStore.setItems(sortedData);
    }

    @FXML
    void cProductRemarkOnAction(ActionEvent event) {

    }

    @FXML
    void cbSoteViewBrandOnClick(MouseEvent event) {

    }

    @FXML
    void cbSoteViewCatagoryOnClick(MouseEvent event) {

    }

    @FXML
    void cbSoteViewSupplyerOnClick(MouseEvent event) {

    }

    @FXML
    void tblViewCurrentStoreOnScroll(ScrollEvent event) {

    }

    @FXML
    void tfSearchOnKeyRelese(KeyEvent event) {

    }

}

