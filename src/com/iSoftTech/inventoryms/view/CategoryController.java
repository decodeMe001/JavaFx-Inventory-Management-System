package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CategoryModel;
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
import javafx.scene.control.MenuItem;
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
public class CategoryController implements Initializable {
    
    @FXML
    private TableView<CategoryModel> tblCategory;

    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryName;

    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryBrand;

    @FXML
    private TableColumn<CategoryModel, String> clmSupplyer;

    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryCreator;

    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryDate;

    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryTime1;

    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryDescription;
    
    @FXML
    private TableColumn<CategoryModel, String> clmCatagoryID;

    @FXML
    private MenuItem miSearch;

    @FXML
    private MenuItem miUpdate;

    @FXML
    private MenuItem miAddNew;

    @FXML
    private MenuItem miDelete;

    @FXML
    private MenuItem miView;

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
   
    
    public StockController stockApp;
    Connection conn = SqliteConnection.Connector();  
    
    private final ObservableList<CategoryModel> categoryData = FXCollections.observableArrayList();
    FilteredList<CategoryModel> filteredData = new FilteredList<>(categoryData, e->true);
    private Stage primaryStage;
    PreparedStatement pS = null;
    ResultSet rs = null;
    private boolean okClicked = false;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
       assert tblCategory !=null;
        // Initialize the stock table with the columns.
        clmCatagoryName.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
        clmCatagoryBrand.setCellValueFactory(cellData -> cellData.getValue().brandNameProperty());
        clmSupplyer.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        clmCatagoryCreator.setCellValueFactory(cellData -> cellData.getValue().catCreatorProperty());
        clmCatagoryDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmCatagoryTime1.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        clmCatagoryDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionInfoProperty());
        clmCatagoryID.setCellValueFactory(cellData -> cellData.getValue().catIdProperty());
       
        loadDataBaseData();
       
    }
    
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<CategoryModel> getCatData() {
        return categoryData;
    }
    
    
    public void loadDataBaseData(){
                           String query = "SELECT * FROM categoryData";

        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                categoryData.add(new CategoryModel(
                            rs.getString("cat_name"),
                            rs.getString("brand"),
                            rs.getString("supplier"),
                            rs.getString("category_creator"),
                            rs.getObject("date"),
                            rs.getObject("time"),
                            rs.getString("description"),
                        rs.getString("cat_id")
                            
                ));
               tblCategory.setItems(categoryData);
            }
            pS.close();
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getCatData().clear();
        tfSearch.clear();
        
        tblCategory.setItems(categoryData);
        // Initialize the stock table with the columns.
        clmCatagoryName.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
        clmCatagoryBrand.setCellValueFactory(cellData -> cellData.getValue().brandNameProperty());
        clmSupplyer.setCellValueFactory(cellData -> cellData.getValue().supplierNameProperty());
        clmCatagoryCreator.setCellValueFactory(cellData -> cellData.getValue().catCreatorProperty());
        clmCatagoryDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        clmCatagoryTime1.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        clmCatagoryDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionInfoProperty());
        clmCatagoryID.setCellValueFactory(cellData -> cellData.getValue().catIdProperty());
        loadDataBaseData();
    }
    
    @FXML
    void tfSearchOnType(KeyEvent event) {

    }
     @FXML
    boolean handleAddNew(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SupplierController.class.getResource("AddCategory.fxml"));
            Group catOverview = (Group) loader.load();

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
            AddCategoryController controller = loader.getController();
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
        CategoryModel getSelectedRow = (CategoryModel)tblCategory.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from categoryData where cat_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getCatId());
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
        
        getCatData().clear();
        loadDataBaseData();
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        CategoryModel getSelectedRow = (CategoryModel)tblCategory.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
                    boolean okClicked = showCatUpdateDialog(getSelectedRow);
            
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
    
    public boolean showCatUpdateDialog(CategoryModel catModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CategoryController.class.getResource("AddCategory.fxml"));
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
            AddCategoryController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCatData(catModel);
            controller.setSaveDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            return false;
        }
    }
    @FXML
    public void searchCat(){
        tfSearch.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super CategoryModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getCategoryName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getBrandName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<CategoryModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblCategory.comparatorProperty());
        tblCategory.setItems(sortedData);
    }
    
}
