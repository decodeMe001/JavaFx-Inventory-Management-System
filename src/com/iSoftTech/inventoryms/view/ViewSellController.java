package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CartSales;
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
import javafx.scene.input.KeyEvent;
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
public class ViewSellController implements Initializable{
    @FXML
    private JFXTextField tfSearch;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<CartSales> tblSellView;

    @FXML
    private TableColumn<CartSales, String> tblClmSellId;

    @FXML
    private TableColumn<CartSales, String> tblClmProductId;

    @FXML
    private TableColumn<CartSales, String> tblClmName;

    @FXML
    private TableColumn<CartSales, Object> tblClmSoldDate;

    @FXML
    private TableColumn<CartSales, Double> tblClmPursrsPrice;

    @FXML
    private TableColumn<CartSales, Double> tblClmSellPrice;

    @FXML
    private TableColumn<CartSales, Integer> tblClmQuantity;

    @FXML
    private TableColumn<CartSales, Double> tblClmTotalPrice;

    @FXML
    private TableColumn<CartSales, String> tblClmDescription;

    @FXML
    private TableColumn<CartSales, String> tblClmCategory;

    @FXML
    private TableColumn<CartSales, String> tblClmBrand;
    
    @FXML
    private TableColumn<CartSales, String> tblClmSoldBy;
  
    @FXML
    private Button getDeleteButton;

    Stage dialogStage;
    private Stage primaryStage;
    Connection conn = SqliteConnection.Connector();  
    private static boolean hasData = false;
    public ObservableList<CartSales> cartData = FXCollections.observableArrayList();
    FilteredList<CartSales> filteredData = new FilteredList<>(cartData, e->true);
    private boolean okClicked = false;
    PreparedStatement pS = null;
    ResultSet rs = null;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
       
        assert tblSellView !=null;
        
        // Initialize the stock table with the columns.
        tblClmSellId.setCellValueFactory(cellData -> cellData.getValue().sellIDProperty());
        tblClmProductId.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        tblClmName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tblClmSoldDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tblClmPursrsPrice.setCellValueFactory(cellData -> cellData.getValue().purchasePriceProperty().asObject());
        tblClmSellPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        tblClmQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblClmTotalPrice.setCellValueFactory(cellData -> cellData.getValue().totalPriceValueProperty().asObject());
        tblClmDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        tblClmCategory.setCellValueFactory(cellData -> cellData.getValue().catProperty()); 
        tblClmBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        tblClmSoldBy.setCellValueFactory(cellData -> cellData.getValue().soldByProperty());
            
        loadDataBaseData();
        getSalesData();
        
        
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
    public ObservableList<CartSales> getSalesData() {
        return cartData;
    }
    
    public void loadDataBaseData(){
        String query = "SELECT * FROM sales";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                cartData.add(new CartSales(
                            rs.getString("sell_id"),
                            rs.getString("product_id"),
                            rs.getString("name"),
                            rs.getObject("date"),
                            rs.getDouble("purchase_price"),
                            rs.getDouble("sales_price"),
                            rs.getInt("quantity"),
                            rs.getDouble("total_price"),
                            rs.getString("description"),
                            rs.getString("category"),
                            rs.getString("brand"),
                            rs.getString("sold_by")              
                ));
               tblSellView.setItems(cartData);
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
    public void searchSellData(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super CartSales>)user->{
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
        SortedList<CartSales> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblSellView.comparatorProperty());
        tblSellView.setItems(sortedData);
    }
    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getSalesData().clear();
        tfSearch.clear();
        
        tblSellView.setItems(cartData);
        // Initialize the stock table with the columns.
        tblClmSellId.setCellValueFactory(cellData -> cellData.getValue().sellIDProperty());
        tblClmProductId.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        tblClmName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tblClmSoldDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tblClmPursrsPrice.setCellValueFactory(cellData -> cellData.getValue().purchasePriceProperty().asObject());
        tblClmSellPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        tblClmQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblClmTotalPrice.setCellValueFactory(cellData -> cellData.getValue().totalPriceValueProperty().asObject());
        tblClmDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        tblClmCategory.setCellValueFactory(cellData -> cellData.getValue().catProperty()); 
        tblClmBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        tblClmSoldBy.setCellValueFactory(cellData -> cellData.getValue().soldByProperty());
        loadDataBaseData();
    }
    
    public void setBtnDisable(){
        getDeleteButton.setDisable(true);
    }
    
    @FXML
    void handleDelete(ActionEvent event) {
         CartSales getSelectedRow = tblSellView.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
             try{
                String query = "DELETE FROM sales WHERE sell_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getSellID());
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
               alert.setHeaderText("No Product Selected");
               alert.setContentText("Please select an Item on the table.");
               alert.showAndWait();
           }
        
        getSalesData().clear();
        loadDataBaseData();
        
    }
    
 

    @FXML
     boolean handleStartSales(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            AnchorPane salesOverview = (AnchorPane)FXMLLoader.load(ViewSellController.class.getResource("AddSalesCart.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Stock Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(salesOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddSalesCartController controller = new AddSalesCartController();
            controller.setDialogStage(dialogStage);        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    void tfSearchOnKeyReleased(KeyEvent event) {

    }

    
    
}
