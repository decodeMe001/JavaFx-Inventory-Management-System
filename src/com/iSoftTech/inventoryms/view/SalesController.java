package com.iSoftTech.inventoryms.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Dada abiola
 */
public class SalesController implements Initializable{

    @FXML
    private AnchorPane acMainSells;

    @FXML
    private Label lblPathInfo;

    @FXML
    private ToggleButton tbtnSell;

    @FXML
    private ToggleButton tbtnCustomer;

    @FXML
    private ToggleButton tbtnReports;

    @FXML
    private StackPane spMainContent;
    
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        
        ToggleGroup toggleGroup = new ToggleGroup();
        tbtnSell.setSelected(true);
        tbtnSell.setToggleGroup(toggleGroup);
        tbtnCustomer.setToggleGroup(toggleGroup);
        
        AnchorPane sales;
        try {
            //load sales Scene
            sales = (AnchorPane)FXMLLoader.load(SalesController.class.getResource("ViewSell.fxml"));
            spMainContent.getChildren().clear();
            spMainContent.getChildren().add(sales);
            
        } catch (IOException ex) {
        }
    }
    @FXML
    void btnSellOrderOnAction(ActionEvent event) {
        AnchorPane sales;
        try {
            sales = (AnchorPane)FXMLLoader.load(SalesController.class.getResource("ViewSell.fxml"));
            spMainContent.getChildren().clear();
            spMainContent.getChildren().add(sales);
            
        } catch (IOException ex) {
        }
    }

     @FXML
    void tbtnCustomerOnAction(ActionEvent event) {
        AnchorPane customer;
        try {
            customer = (AnchorPane)FXMLLoader.load(SalesController.class.getResource("ViewCustomer.fxml"));
            spMainContent.getChildren().clear();
            spMainContent.getChildren().add(customer);
            
        } catch (IOException ex) {
        }
    }

    
    
   
}
