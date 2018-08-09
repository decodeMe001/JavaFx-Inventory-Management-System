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
public class StockController implements Initializable {
   
    @FXML
    private ToggleButton btnStock;

    @FXML
    private ToggleButton btnSupplier;

    @FXML
    private ToggleButton btnBrands;

    @FXML
    private ToggleButton btnCatagory;

    @FXML
    private ToggleButton btnUnit;

    @FXML
    private ToggleButton btnRma;

    @FXML
    private StackPane spMainContent;
    
    @FXML
    private Label lblHeader;
    
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        ToggleGroup toggleGroup = new ToggleGroup();
        btnStock.setSelected(true);
        btnStock.setToggleGroup(toggleGroup);
        btnSupplier.setToggleGroup(toggleGroup);
        btnBrands.setToggleGroup(toggleGroup);
        btnCatagory.setToggleGroup(toggleGroup);
        btnUnit.setToggleGroup(toggleGroup);
        btnRma.setToggleGroup(toggleGroup);
        
        StackPane stock;
        try {
            stock = (StackPane)FXMLLoader.load(StockController.class.getResource("CurrentStock.fxml"));
            //asc.settingPermission();
            spMainContent.getChildren().clear();
            spMainContent.getChildren().add(stock);
            
        } catch (IOException ex) {
        }
        

    }


    @FXML
    public void btnStockOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Stock");
        CurrentStockController asc = new CurrentStockController();
        StackPane stock = (StackPane)FXMLLoader.load(StockController.class.getResource("CurrentStock.fxml"));
        //asc.settingPermission();
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(stock);
    }
    
    @FXML
    private void btnSupplierOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Supplier");
        SupplierController vsc = new SupplierController();
        AnchorPane supplier = (AnchorPane)FXMLLoader.load(StockController.class.getResource("SupplierView.fxml"));
        
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(supplier);
    }

    @FXML
    private void btnBrandsOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Brand");
        BrandController vbc = new BrandController();
        AnchorPane brands = (AnchorPane)FXMLLoader.load(StockController.class.getResource("ViewBrand.fxml"));
        
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(brands);
    }

    @FXML
    private void btnCatagoryOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Category");
        CategoryController brd = new CategoryController();
        AnchorPane cat = (AnchorPane)FXMLLoader.load(StockController.class.getResource("ViewCategory.fxml"));
        
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(cat);
    }

    @FXML
    private void btnUnitOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Stock Unit Record");
        UnitController ut = new UnitController();
        AnchorPane unit = (AnchorPane)FXMLLoader.load(StockController.class.getResource("ViewUnit.fxml"));
        
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(unit);
    }

    @FXML
    private void btnRemarkOnAction(ActionEvent event) throws IOException {
        lblHeader.setText("Stock Remark");
        RemarkController rmk = new RemarkController();
        AnchorPane remark = (AnchorPane)FXMLLoader.load(StockController.class.getResource("RMAview.fxml"));
        
        spMainContent.getChildren().clear();
        spMainContent.getChildren().add(remark);
    }
}