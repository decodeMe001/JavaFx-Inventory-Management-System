package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.MainApp;
import com.iSoftTech.inventoryms.model.LoginModel;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Dada abiola
 */
public class LoginController implements Initializable{
    
   // Reference to the main application.
    public MainApp mainApp;
    public LoginModel loginModel = new LoginModel();
   
    @FXML
    private JFXPasswordField handlePass;
    @FXML
    private JFXTextField handleUser;
    @FXML
    private Hyperlink createAccount;
    
    @FXML
    private Label isConnected;
    
    @FXML
    private ComboBox<String> choiceBox;
    private final ObservableList<String> user  = FXCollections.observableArrayList("Select","Admin","Staff");

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate combobox with initial users
        choiceBox.setItems(user);
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.setTooltip(new Tooltip("Select User"));
        
    }
    
    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    @FXML
    private Button btnClose;
    
    @FXML
    public void onLogin(ActionEvent event) throws IOException{
        if(isAllFieldFillup()){
        String userType = choiceBox.getValue().toString().trim();
        
        switch (userType){
            case "Admin":
                try{
                    if(loginModel.adminLogin(handleUser.getText().toLowerCase(), handlePass.getText().toLowerCase())){

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage primaryStage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(LoginController.class.getResource("DashBoard.fxml"));
                        AnchorPane root = (AnchorPane) loader.load();
                        Scene scene = new Scene(root);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("Storekeeper Inventory Management System(SIMS) v2.00");
                        primaryStage.getIcons().add(new Image("resources/images/Briefcase.png"));
                        primaryStage.show();

                }else{
            isConnected.setText("Access Denied! Invalid username Or password");
            handleUser.clear();
            handlePass.clear();
        }
        }catch (SQLException e){
           isConnected.setText("Access Denied! Invalid username Or password");
        }
       break;
            case "Staff":
               
                try{
                    if(loginModel.staffLogin(handleUser.getText().toLowerCase(), handlePass.getText().toLowerCase())){

                        ((Node)event.getSource()).getScene().getWindow().hide();
                        Stage primaryStage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(LoginController.class.getResource("DashBoard.fxml"));
                        AnchorPane root = (AnchorPane) loader.load();
                        Scene scene = new Scene(root);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("StoreKeeper Inventory Management System v1.00");
                        primaryStage.getIcons().add(new Image("resources/images/Briefcase.png"));
                        DashBoardController controller = loader.getController();
                        controller.setBtnAuthDisable();
                        primaryStage.show();


                }else{
                    isConnected.setText("Access Denied! Invalid username Or password");
                    handleUser.clear();
                    handlePass.clear();
                }
            }catch (SQLException e){
               isConnected.setText("Access Denied! Invalid username Or password");
            }
                    break;
           
            }
        }
    }
    
    private boolean isAllFieldFillup(){
        boolean fillup;
        if(handleUser.getText().trim().isEmpty()||handlePass.getText().isEmpty()){

            NotificationType notificationType = NotificationType.ERROR;
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Error 504!!!");
            tray.setMessage("Username or Password should not be Empty!");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(3000));

            fillup = false;
        }
        else fillup = true;
        return fillup;
    }
}
