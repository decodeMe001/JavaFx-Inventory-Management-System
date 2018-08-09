package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.StaffModel;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class AuthController implements Initializable {

    @FXML
    private ComboBox<String> cbStaffName;

    @FXML
    private JFXTextField userInput;

    @FXML
    private JFXPasswordField userPass;

    @FXML
    private Label getStaffID;

    @FXML
    private Label getStaffEmail;

    @FXML
    private TableView<StaffModel> authTable;

    @FXML
    private TableColumn<StaffModel, String> staffName;

    @FXML
    private TableColumn<StaffModel, String> staffID;

    @FXML
    private TableColumn<StaffModel, String> emailAuth;

    @FXML
    private TableColumn<StaffModel, String> authUsername;

    @FXML
    private TableColumn<StaffModel, String> authPassword;

    @FXML
    private JFXTextField searchBox;

    @FXML
    private Button btnRefresh;
    
    Stage dialogStage;
    private Stage primaryStage;
    private boolean okClicked = false;
    Connection conn = SqliteConnection.Connector();  
    public ObservableList<StaffModel> staffData = FXCollections.observableArrayList();
    FilteredList<StaffModel> filteredData = new FilteredList<>(staffData, e->true);


    PreparedStatement pS = null;
    ResultSet rS = null;
    static String temp;
    
     @FXML
    public void initialize(URL location, ResourceBundle resources) {

        assert authTable !=null;
        staffName.setCellValueFactory(cellData -> cellData.getValue().staffNameProperty());
        authUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        authPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        emailAuth.setCellValueFactory(cellData -> cellData.getValue().eMailProperty());
        staffID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        
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
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
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
               authTable.setItems(staffData);
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
        searchBox.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super StaffModel>)user->{
                if(newValue==null||newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(user.getStaffName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(user.getEmployeeID().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
                
            });
        });
        SortedList<StaffModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(authTable.comparatorProperty());
        authTable.setItems(sortedData);
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getStaffData().clear();
        searchBox.clear();
        staffName.setCellValueFactory(cellData -> cellData.getValue().staffNameProperty());
        authUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        authPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        emailAuth.setCellValueFactory(cellData -> cellData.getValue().eMailProperty());
        staffID.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty());
        
        loadDataBaseData();
    }

    @FXML
    void cbStaffName(MouseEvent event) {
        cbStaffName.getItems().clear();
        cbStaffName.getValue();
        try{
            String sql = "SELECT * FROM staffData order by name";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                String cat = rS.getString("name");
                cbStaffName.getItems().remove(cat);
                cbStaffName.getItems().add(cat);   
            }
           
        }catch (SQLException ex) {
            Logger.getLogger(AuthController.class.getName()).log(Level.SEVERE, null, ex);
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
    void handleUpdate(ActionEvent event) {
        StaffModel getSelectedRow = authTable.getSelectionModel().getSelectedItem();
        if (getSelectedRow != null) {
              //boolean okClicked = showStaffUpdateDialog(getSelectedRow);
        } else {
               // Nothing selected.
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.initOwner(primaryStage);
               alert.setTitle("No Selection");
               alert.setHeaderText("No Staff Selected!!!");
               alert.setContentText("Please select a STAFF on the table.");
               alert.showAndWait();
           }
        if(isInputValid()){
                        String userName = userInput.getText();
                        String pass = userPass.getText();
        
                        String data = "UPDATE staffData SET username=?, password=? where staff_id='"+temp+"'";
                try {    
                    
                    pS = conn.prepareStatement(data);
                    
                        pS.setString(1, userName);
                        pS.setString(2, pass);                     
                        pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddStockDataController.class.getName()).log(Level.SEVERE, null, ex);
                } finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                }
                clearAll();
                okClicked = true;
            if(okClicked){
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Congratulation!!!");
                tray.setMessage("Items Successfully Updated.");
                tray.setNotificationType(notificationType);
                tray.showAndDismiss(Duration.millis(3000));
            }
       }
        
    }
    @FXML
    void onTableClicked(MouseEvent event) {
        StaffModel staff = (StaffModel)authTable.getSelectionModel().getSelectedItem();
        ResultSet rs;
        String query = "SELECT * FROM staffData where staff_id = '"+staff.getEmployeeID()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                cbStaffName.setValue(rs.getString("name"));
                getStaffID.setText(rs.getString("staff_id"));            
                getStaffEmail.setText(rs.getString("email"));            
                userInput.setText(rs.getString("username"));
                userPass.setText(rs.getString("password"));
                       
            }
            temp = getStaffID.getText();
           
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
    public void clearAll() {
        userInput.setText(null);
        userPass.setText(null);
        getStaffID.setText(null);
        getStaffEmail.setText(null);
        cbStaffName.setValue(null);
        
    }

   /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
       
        if (userInput.getText() == null) {
            errorMessage += "Username field is Empty!\n"; 
        } 
        if (userPass.getText() == null) {
            errorMessage += "Password field is Empty!\n"; 
        } 
        if (cbStaffName.getSelectionModel().getSelectedItem() == null && cbStaffName.getPromptText().isEmpty()) {
            errorMessage += "Name Choosen field is Empty!\n"; 
        } 
       
       
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Empty Fields");
            alert.setHeaderText("Please Fill All Fields with Valid Data!");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
}
