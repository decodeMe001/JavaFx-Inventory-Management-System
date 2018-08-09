package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.RemarkModel;
import com.iSoftTech.inventoryms.model.StockModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class AddRemarkController implements Initializable {
   
    Stage dialogStage;
    public CurrentStockController cSC;
    private boolean okClicked = false;
    StockModel stockM;
    private Stage primaryStage;
    Connection conn = SqliteConnection.Connector(); 
    private static boolean hasData = false;
    PreparedStatement pS;
    ResultSet rS;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isSavedClicked() {
        return okClicked;
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        remarkIDLabel.setText(randomAplphaNum(5));
        
    } 

    @FXML
    private ComboBox<String> cbProductName;

     @FXML
    private JFXTextField remarkPayment;

    @FXML
    private JFXDatePicker deadlineDate;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXTextField added_By;

    @FXML
    private Button saveButton;
    
    @FXML
    private Button updateButton;
    @FXML
    private Label lblHeaderContent;

    @FXML
    private Button btnClose;
    static String temp;
    @FXML
    private Label remarkIDLabel;
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    public static String randomAplphaNum(int count){
       StringBuilder builder = new StringBuilder();
       while(count-- != 0){
           int character = (int)(Math.random()*ALPHA_NUM_STRING.length());
           builder.append(ALPHA_NUM_STRING.charAt(character));
       }
       return builder.toString();
   }
    
    public void setSaveDisable(){
        saveButton.setDisable(true);
    }
    public void setUpdateDisable(){
        updateButton.setDisable(true);
    }

    @FXML
    boolean btnAddProductOnAction(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            AnchorPane supOverview = (AnchorPane)FXMLLoader.load(AddBrandController.class.getResource("AddStockData.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Stock");            
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(supOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            // Set the stock into the controller.
            AddStockDataController controller = new AddStockDataController();
            controller.setDialogStage(dialogStage);
        
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isSavedClicked();
            
        } catch (IOException e) {
            return false;
        }
    }

    @FXML
    void cbProductNameOnClick(MouseEvent event) {
        cbProductName.getItems().clear();
        try{
            String sql = "SELECT * FROM stockData order by name";
            pS = conn.prepareStatement(sql);
            rS = pS.executeQuery();
            
            while(rS.next()){
                String sup = rS.getString("name");
                cbProductName.getItems().remove(sup);
                cbProductName.getItems().add(sup);
            }
            pS.close();
            rS.close();
        }catch (SQLException ex) {
            Logger.getLogger(AddBrandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleSave(ActionEvent event) throws SQLException {
        if (isInputValid()) {
                                                           
                        String proData = cbProductName.getSelectionModel().getSelectedItem();
                        String clientRem = remarkPayment.getText();
                        String add = added_By.getText();
                        Object deadLine = deadlineDate.getValue();
                        Object dateL = date.getValue();
                        String id = remarkIDLabel.getText();
                        
                String query = "INSERT INTO remarkData (product_name, payment_remark, added_by, deadline, date, remark_id) VALUES(?,?,?,?,?,?)";
                    pS = null;
                    
                    try{
                        pS = conn.prepareStatement(query);
                        
                        pS.setString(1, proData);
                        pS.setString(2, clientRem);
                        pS.setString(3, add);
                        pS.setObject(4, deadLine);
                        pS.setObject(5, dateL);
                        pS.setString(6, id);
                           
                    } catch(SQLException e){
                        e.printStackTrace();
                    }finally{
                        pS.execute();
                        pS.close();
                    }
                    
                    okClicked = true;
                    if(okClicked)
                    { 
                        okClicked = true;
                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("Congratulation!!!");
                            tray.setMessage("Item Successfully Added!.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                    }                
        }
    }
    
    @FXML
    void handleUpdate(ActionEvent event) {
        if(isInputValid()){
                        String proData = cbProductName.getSelectionModel().getSelectedItem();
                        String clientRem = remarkPayment.getText();
                        String add = added_By.getText();
                        Object deadLine = deadlineDate.getValue();
                        Object dateL = date.getValue();
                        String id = remarkIDLabel.getText();
                String query = "UPDATE remarkData SET product_name=?, payment_remark=?, added_by=?, deadline=?, date=?, remark_id=? where remark_id='"+temp+"'";
            try{
                pS = conn.prepareStatement(query);
                    
                        pS.setString(1, proData);
                        pS.setString(2, clientRem);
                        pS.setString(3, add);
                        pS.setObject(4, deadLine);
                        pS.setObject(5, dateL);
                        pS.setString(6, id);
                        
                   pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddCategoryController.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                }
            okClicked = true;
            if(okClicked){
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Congratulation!!!");
                tray.setMessage("Items Successfully Updated.");
                tray.setNotificationType(notificationType);
                tray.showAndDismiss(Duration.millis(3000));
            }
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        
    }
    
    public void setRemData(RemarkModel remModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM remarkData where remark_id = '"+remModel.getRemarkId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                cbProductName.setValue(rs.getString("product_name"));            
                added_By.setText(rs.getString("added_by")); 
                remarkPayment.setText(rs.getString("payment_remark"));
                date.setValue(LocalDate.parse(rs.getObject("date").toString()));                     
                deadlineDate.setValue(LocalDate.parse(rs.getObject("deadline").toString()));                     
                remarkIDLabel.setText(rs.getString("remark_id"));
            }
            temp = remarkIDLabel.getText();
           
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
    void btnCloseOnAction(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (cbProductName.getSelectionModel().isEmpty() || cbProductName.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "No valid Supplier!\n"; 
        }
        if (remarkPayment.getText() == null || remarkPayment.getText().length() == 0) {
            errorMessage += "No valid Input Field!\n"; 
        } 
       
        if (added_By.getText() == null || added_By.getText().length() == 0) {
            errorMessage += "No valid Field Entered\n"; 
        }
               
        if (deadlineDate.toString().isEmpty()) {
            errorMessage += "No valid Date!\n";
        } 
        if (date.toString().isEmpty()) {
            errorMessage += "No valid Date!\n";
        } 

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please Insert valid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
}
