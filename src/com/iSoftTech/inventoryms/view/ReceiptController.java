package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CartRecord;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
/**
 *
 * @author Dada abiola
 */
public class ReceiptController implements Initializable{
    
   
    @FXML
    private Label subTotal;

    @FXML
    private Label getTotal;
    
     @FXML
    private TableView<CartRecord> tableReceipt;
      
    @FXML
    private TableColumn<CartRecord, Double> getTotalPro;

    @FXML
    private TableColumn<CartRecord, String> getProName;

    @FXML
    private TableColumn<CartRecord, Integer> getProQty;

    @FXML
    private TableColumn<CartRecord, Double> getProdPrice;
    
    @FXML
    private Label receiptdate;

    @FXML
    private Label salesId;

    @FXML
    private Label cashier;

    @FXML
    private Pane itemsHolder;

    @FXML
    private Label amountGiven;

    @FXML
    private Label changeGiven;
    
    Connection conn = SqliteConnection.Connector();
    PreparedStatement pS = null;
    ResultSet rS = null;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        assert tableReceipt != null;
        getProdPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        getProQty.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        getProName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        getTotalPro.setCellValueFactory(cellData -> cellData.getValue().totalPriceValueProperty().asObject());
        tableReceipt.setItems(AddSalesCartController.cartData);
        amountGiven.setText(""+AddSalesCartController.AmountTendered);
        salesId.setText(""+AddSalesCartController.SalesID);
        cashier.setText("Cashier");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //get current date time with Date()
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        receiptdate.setText(dateFormat.format(date));
        subTotal();
    }
    
    public void subTotal()
    {
        try {
            pS = conn.prepareStatement("select sum(total_price) from cartRecord");
            rS = pS.executeQuery();
            while (rS.next()) {
                subTotal.setText(rS.getString(1));
                getTotal.setText(rS.getString(1));
                changeGiven.setText(" " + (AddSalesCartController.AmountTendered - Double.parseDouble(rS.getString(1))));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try{  
              pS.close();
              rS.close();
            }catch(Exception e){

            }
        }
    }
}
