package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.StaffModel;
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
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class StaffDataListController implements Initializable {
    
    @FXML
    private TableView<StaffModel> adminTableView;

    @FXML
    private TableColumn<StaffModel, String> tblName;

    @FXML
    private TableColumn<StaffModel, String> tblPhone;

    @FXML
    private TableColumn<StaffModel, String> tblPostalCode;

    @FXML
    private TableColumn<StaffModel, String> tblOffice;

    @FXML
    private TableColumn<StaffModel, Object> tblDob;

    @FXML
    private TableColumn<StaffModel, Object> tblDateOfEmployment;

    @FXML
    private TableColumn<StaffModel, Double> tblSalary;

    @FXML
    private TableColumn<StaffModel, String> tblAddress;

    @FXML
    private TableColumn<StaffModel, String> tblEmail;

    @FXML
    private TableColumn<StaffModel, String> tblStaffID;
    
    @FXML
    private JFXTextField searchList;
    
    private Stage primaryStage;
    private boolean okClicked = false;
    Stage dialogStage;
    Connection conn = SqliteConnection.Connector();  
    public ObservableList<StaffModel> staffData = FXCollections.observableArrayList();
    FilteredList<StaffModel> filteredData = new FilteredList<>(staffData, e->true);
    PreparedStatement pS = null;
    ResultSet rS = null;
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
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
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        assert adminTableView !=null;
        tblName.setCellValueFactory(cellData -> cellData.getValue().staffNameProperty());
        tblPhone.setCellValueFactory(cellData -> cellData.getValue().phoneNumProperty());
        tblPostalCode.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
        tblOffice.setCellValueFactory(cellData -> cellData.getValue().officeHeldProperty());
        tblDob.setCellValueFactory(cellData -> cellData.getValue().dateOfBirthProperty());
        tblDateOfEmployment.setCellValueFactory(cellData -> cellData.getValue().dateEmployeedProperty());
        tblSalary.setCellValueFactory(cellData -> cellData.getValue().salaryTakenProperty().asObject());
        tblAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        tblEmail.setCellValueFactory(cellData -> cellData.getValue().eMailProperty());
        tblStaffID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        
        loadDataBaseData();
        getStaffData();
        
    } 
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<StaffModel> getStaffData() {
        return staffData;
    }

    public void loadDataBaseData(){
        String query = "SELECT * FROM staffData";
        try{
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();
            while(rS.next()){
                staffData.add(new StaffModel(
                            rS.getString("name"),
                            rS.getString("phone"),
                            rS.getString("postal_code"),
                            rS.getString("office"),
                            rS.getDouble("salary"),
                            rS.getObject("edate"),
                            rS.getObject("dob"), 
                            rS.getString("email"),
                            rS.getString("username"),
                            rS.getString("password"),
                            rS.getString("address"),
                            rS.getString("staff_id")
                            
                ));
               adminTableView.setItems(staffData);
            }
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
    public void searchStaff(){
        searchList.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super StaffModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getStaffName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }if(user.getEmployeeID().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                else if(user.getUsername().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<StaffModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(adminTableView.comparatorProperty());
        adminTableView.setItems(sortedData);
    }
    
    @FXML
    void handleBioData(ActionEvent event) {
        StaffModel getSelectedRow = adminTableView.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
              boolean okClicked = showStaffBioDataDialog(getSelectedRow);
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Staff Selected");
               alert.setContentText("Please select an Item on the table.");
               alert.showAndWait();
           }
    }
    
    public boolean showStaffBioDataDialog(StaffModel staffModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StaffDataListController.class.getResource("StaffBioData.fxml"));
            AnchorPane stockOverview = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Staff Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(stockOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the staff into the controller.
            StaffBioDataController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStaffData(staffModel);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        StaffModel getSelectedRow = (StaffModel)adminTableView.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
            try{
                String query = "delete from staffData where staff_id=?";
                pS = conn.prepareStatement(query);
                pS.setString(1, getSelectedRow.getEmployeeID());
                pS.executeUpdate();
                }catch(SQLException e){
                }finally{
                      try{  
                        //conn.close();
                        pS.close();
                        rS.close();
                      }catch(Exception e){
                          
                      }
                }
            okClicked = true;
            if(okClicked){
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Notice!!!");
                tray.setMessage("Staff Successfully Deleted.");
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
        
        getStaffData().clear();
        loadDataBaseData();
    }
    
    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getStaffData().clear();
        searchList.clear();
        
        adminTableView.setItems(staffData);
        tblName.setCellValueFactory(cellData -> cellData.getValue().staffNameProperty());
        tblPhone.setCellValueFactory(cellData -> cellData.getValue().phoneNumProperty());
        tblPostalCode.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
        tblOffice.setCellValueFactory(cellData -> cellData.getValue().officeHeldProperty());
        tblDob.setCellValueFactory(cellData -> cellData.getValue().dateOfBirthProperty());
        tblDateOfEmployment.setCellValueFactory(cellData -> cellData.getValue().dateEmployeedProperty());
        tblSalary.setCellValueFactory(cellData -> cellData.getValue().salaryTakenProperty().asObject());
        tblAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        tblEmail.setCellValueFactory(cellData -> cellData.getValue().eMailProperty());
        tblStaffID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        
        loadDataBaseData();
    }

    @FXML
    boolean handleNew(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StaffDataListController.class.getResource("AddEmployee.fxml"));
            AnchorPane staffOverview = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Staff Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(staffOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the staff into the controller.
            AddEmployeeController controller = loader.getController();
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
    public void handleUpdate(ActionEvent event) {
        StaffModel getSelectedRow = adminTableView.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
              boolean okClicked = showStaffUpdateDialog(getSelectedRow);
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Staff Selected");
               alert.setContentText("Please select an Item on the table.");
               alert.showAndWait();
           }
    }
    
    public boolean showStaffUpdateDialog(StaffModel staffModel){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StaffDataListController.class.getResource("AddEmployee.fxml"));
            AnchorPane stockOverview = (AnchorPane) loader.load();
            
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Staff Data");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(stockOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the staff into the controller.
            AddEmployeeController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStaffData(staffModel);
            controller.setSaveDisable();
            controller.setUpdateImageDisable();
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
            
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
       
    
}
