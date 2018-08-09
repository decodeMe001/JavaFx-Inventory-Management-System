package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.MainApp;
import com.iSoftTech.inventoryms.model.StaffModel;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Dada abiola
 */
public class DashBoardController implements Initializable{
    
    @FXML
    private AnchorPane mainApp;
    
    @FXML
    private Button btnHome;

    @FXML
    private ImageView homeImg;

    @FXML
    private TabPane contentBox;

    @FXML
    private Tab homeScene;
    
    @FXML
    private Button btnStock;
    
    @FXML
    private Button btnSales;
    
    @FXML
    private Button btnEmployee;
    
    @FXML
    private Button btnAuth;
    
    @FXML
    private Button btnAbout;

    @FXML
    private ImageView stockImg;

    @FXML
    private ImageView salesImg;

    @FXML
    private ImageView employeeImg;

    @FXML
    private ImageView authImg;

    @FXML
    private ImageView aboutImg;

    @FXML
    private Button btnLogOut;

    StaffModel staff;
    MainApp main;
    StaffBioDataController staffData;
    String id;
    Connection conn = SqliteConnection.Connector();  
    PreparedStatement pS = null;
    ResultSet rS = null;
    //private static Stage primaryStage;
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        homeActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("Home.fxml"));
        HomeController homeController = new HomeController();
        homeController.getClass();
        homeScene.setContent(node);
        } catch (IOException e){
        }       
    }
    
    @FXML
    void btnLogOut(ActionEvent event) throws IOException {
        try{
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DashBoardController.class.getResource("LoginPage.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            scene.setFill(new Color(0, 0, 0, 0));
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setTitle("Welcome to SIMS-Login");
            primaryStage.getIcons().add(new Image("resources/images/Briefcase.png"));
            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);
            primaryStage.setMinHeight(500.0);
            primaryStage.setMinWidth(850.0);
            primaryStage.show();
            
        } catch(Exception e){
        }
    }
    
    public void setBtnAuthDisable(){
        btnAuth.setDisable(true);
        btnStock.setDisable(true);
        btnEmployee.setDisable(true);
    }
    public void setIdentity(){
        
    }
    @FXML
    void onAboutClicked(ActionEvent event) {
        aboutActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("About.fxml"));
        //StockController stockController = FXMLLoader.getController();
        Tab tb = new Tab("About Me", node);
        contentBox.getTabs().clear();
        contentBox.getTabs().add(tb);
        homeScene.setContent(node);
        } catch (IOException e){
        }
        
    }

    @FXML
    void onEmployeeClicked(ActionEvent event) {
        employeeActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("StaffDataList.fxml"));
        Tab tb = new Tab("Staff Data", node);
        contentBox.getTabs().clear();
        contentBox.getTabs().add(tb);
        homeScene.setContent(node);
        } catch (IOException e){
        }
        
    }

    @FXML
    void onHomeClicked(ActionEvent event) {
        homeActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("Home.fxml"));
        HomeController homeController = new HomeController();
        homeController.getClass();
        Tab tb = new Tab("Home", node);
        contentBox.getTabs().clear();
        contentBox.getTabs().add(tb);
        homeScene.setContent(node);
        } catch (IOException e){
        }
        
    }

    @FXML
    void onSalesClicked(ActionEvent event) {
        salesActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("SellView.fxml"));
        Tab tb = new Tab("Sales", node);
        contentBox.getTabs().clear();
        contentBox.getTabs().add(tb);
        homeScene.setContent(node);
        } catch (IOException e){
        }
       
    }

    @FXML
    void onStockClicked(ActionEvent event) {
        
        stockActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("StockView.fxml"));
        Tab tb = new Tab("Stock", node);
        contentBox.getTabs().clear();
        contentBox.getTabs().add(tb);
        homeScene.setContent(node);
        } catch (IOException e){
        }
     
       
    }

    @FXML
    void onAuthenticate(ActionEvent event) {
         
        authActive();
        try{
        Node node = (AnchorPane)FXMLLoader.load(DashBoardController.class.getResource("Auth.fxml"));
        Tab tb = new Tab("Authentication", node);
        contentBox.getTabs().clear();
        contentBox.getTabs().add(tb);
        homeScene.setContent(node);
        } catch (IOException e){
        }
        
    }
    Image image;
    String defaultStyle = "-fx-border-width: 0px 0px 0px 5px;"
            + "-fx-border-color:none";

    String activeStyle = "-fx-border-width: 0px 0px 0px 5px;"
            + "-fx-border-color:#FF4E3C";

    Image home = new Image("resources/icon/home.png");
    Image homeRed = new Image("resources/icon/homeRed.png");
    Image stock = new Image("resources/icon/stock.png");
    Image stockRed = new Image("resources/icon/stockRed.png");
    Image sell = new Image("resources/icon/sell2.png");
    Image sellRed = new Image("resources/icon/sell2Red.png");
    Image employee = new Image("resources/icon/employe.png");
    Image employeeRed = new Image("resources/icon/employeRed.png");
    Image setting = new Image("resources/icon/settings.png");
    Image settingRed = new Image("resources/icon/settingsRed.png");
    Image about = new Image("resources/icon/about.png");
    Image aboutRed = new Image("resources/icon/aboutRed.png");

   
    @FXML
    private void mainApp(KeyEvent event) {
        if (event.getCode() == KeyCode.F11) {
            Stage stage = (Stage) mainApp.getScene().getWindow();
            stage.setFullScreen(true);
        }
    }
    
    private void homeActive() {
        homeImg.setImage(homeRed);
        stockImg.setImage(stock);
        salesImg.setImage(sell);
        employeeImg.setImage(employee);
        authImg.setImage(setting);
        aboutImg.setImage(about);
        btnHome.setStyle(activeStyle);
        btnStock.setStyle(defaultStyle);
        btnSales.setStyle(defaultStyle);
        btnEmployee.setStyle(defaultStyle);
        btnAuth.setStyle(defaultStyle);
        btnAbout.setStyle(defaultStyle);
    }

    private void stockActive() {
        homeImg.setImage(home);
        stockImg.setImage(stockRed);
        salesImg.setImage(sell);
        employeeImg.setImage(employee);
        authImg.setImage(setting);
        aboutImg.setImage(about);
        btnHome.setStyle(defaultStyle);
        btnStock.setStyle(activeStyle);
        btnSales.setStyle(defaultStyle);
        btnEmployee.setStyle(defaultStyle);
        btnAuth.setStyle(defaultStyle);
        btnAbout.setStyle(defaultStyle);
        
    }

    private void salesActive() {
       homeImg.setImage(home);
        stockImg.setImage(stock);
        salesImg.setImage(sellRed);
        employeeImg.setImage(employee);
        authImg.setImage(setting);
        aboutImg.setImage(about);
        btnHome.setStyle(defaultStyle);
        btnStock.setStyle(defaultStyle);
        btnSales.setStyle(activeStyle);
        btnEmployee.setStyle(defaultStyle);
        btnAuth.setStyle(defaultStyle);
        btnAbout.setStyle(defaultStyle);
        
    }

    private void employeeActive() {
        homeImg.setImage(home);
        stockImg.setImage(stock);
        salesImg.setImage(sell);
        employeeImg.setImage(employeeRed);
        authImg.setImage(setting);
        aboutImg.setImage(about);
        btnHome.setStyle(defaultStyle);
        btnStock.setStyle(defaultStyle);
        btnSales.setStyle(defaultStyle);
        btnEmployee.setStyle(activeStyle);
        btnAuth.setStyle(defaultStyle);
        btnAbout.setStyle(defaultStyle);
        
    }

    private void authActive() {
        homeImg.setImage(home);
        stockImg.setImage(stock);
        salesImg.setImage(sell);
        employeeImg.setImage(employee);
        authImg.setImage(settingRed);
        aboutImg.setImage(about);
        btnHome.setStyle(defaultStyle);
        btnStock.setStyle(defaultStyle);
        btnSales.setStyle(defaultStyle);
        btnEmployee.setStyle(defaultStyle);
        btnAuth.setStyle(activeStyle);
        btnAbout.setStyle(defaultStyle);
        
    }

    private void aboutActive() {
        homeImg.setImage(home);
        stockImg.setImage(stock);
        salesImg.setImage(sell);
        employeeImg.setImage(employee);
        authImg.setImage(setting);
        aboutImg.setImage(aboutRed);
        btnHome.setStyle(defaultStyle);
        btnStock.setStyle(defaultStyle);
        btnSales.setStyle(defaultStyle);
        btnEmployee.setStyle(defaultStyle);
        btnAuth.setStyle(defaultStyle);
        btnAbout.setStyle(activeStyle);
    } 
}
