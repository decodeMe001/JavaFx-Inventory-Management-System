package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CartRecord;
import com.iSoftTech.inventoryms.model.StockModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import java.lang.reflect.InvocationTargetException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


/**
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class AddSalesCartController implements Initializable {
    
    static ObservableList<CartRecord> cartData = FXCollections.observableArrayList();
    static int AmountTendered = 0;
    static String SalesID = "";
    //static ArrayList<CartRecord> getCat = new ArrayList<CartRecord>();
    
    @FXML
    private JFXDatePicker getSalesDate;

    @FXML
    private JFXTextField getQuantityBought;

    @FXML
    private Label getProductID;

    @FXML
    private Label getProductName;

    @FXML
    private Label getSellingPrice;

    @FXML
    private Label getPurchasePrice;

    @FXML
    private Label getQuantity;

    @FXML
    private Label getCategory;

    @FXML
    private Label getBrand;
    
    @FXML
    private Label getDescription;

    @FXML
    private Text getSoldBy;

    @FXML
    private TableView<CartRecord> salesCartTable;

    @FXML
    private TableColumn<CartRecord, String> tblCartProductID;

    @FXML
    private TableColumn<CartRecord, Double> tblCartSellingPrice;

    @FXML
    private TableColumn<CartRecord, Integer> tblCartQuantity;

    @FXML
    private TableColumn<CartRecord, Double> tblCartTotal;

    @FXML
    private TableColumn<CartRecord, String> tblCartName;

    @FXML
    private Label getSalesID;

    @FXML
    private TableView<StockModel> productTable;

    @FXML
    private TableColumn<StockModel, String> tblProductId;

    @FXML
    private TableColumn<StockModel, String> tblProdName;

    @FXML
    private TableColumn<StockModel, Double> tblPurchasePrice;

    @FXML
    private TableColumn<StockModel, Double> tblSalesPrice;

    @FXML
    private TableColumn<StockModel, Integer> tblQuantity;

    @FXML
    private TableColumn<StockModel, String> tblCategory;

    @FXML
    private TableColumn<StockModel, String> tblBrand;

    @FXML
    private TableColumn<StockModel, String> tblDescription;
    
    @FXML
    private Label getTotalPrice;

    @FXML
    private Button btnRefresh;

    @FXML
    private JFXTextField searchBox;
    
    @FXML
    private Label getUnit;

    @FXML
    private Label getQtyLeft;
    
    @FXML
    private TextField cashReceived;
    
    @FXML
    private Label getTotal;
    
    @FXML
    private Text totalItemCash;

    
    private boolean okClicked = false;
    Stage dialogStage;
    Connection conn = SqliteConnection.Connector(); 
    public ObservableList<StockModel> stockData = FXCollections.observableArrayList();
    public ObservableList<CartRecord> cartDataRecord = FXCollections.observableArrayList();
    FilteredList<StockModel> filteredData = new FilteredList<>(stockData, e->true);

    PreparedStatement pS = null;
    ResultSet rS = null;
    int quantity;
    int quatityLeft;
    private static final String ALPHA_NUM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    int count;
    static String temp;
    static String tempId;
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
        
        assert productTable !=null;
        assert salesCartTable !=null;
        // Initialize the stock table with the columns.
        tblProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty());
        tblProdName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tblPurchasePrice.setCellValueFactory(cellData -> cellData.getValue().purchaseProperty().asObject());
        tblSalesPrice.setCellValueFactory(cellData -> cellData.getValue().sellProperty().asObject());
        tblQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        tblCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty()); 
        tblDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        
         // Initialize the Shop Cart table with the columns.
        tblCartProductID.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        tblCartSellingPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        tblCartQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblCartTotal.setCellValueFactory(cellData -> cellData.getValue().totalPriceValueProperty().asObject());
        tblCartName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        
        loadDataBaseData();
        loadCartRecordData();
        getStockData();
        getCartRecordData();
        getSalesID.setText(randomAplphaNum(6));
        String getID = getSalesID.getText();
        AddSalesCartController.SalesID = getID;
      
    } 
    /**
     * Returns the data as an observable list of Stocks. 
     * @return
     */
    public ObservableList<StockModel> getStockData() {
        return stockData;
    }
    public ObservableList<CartRecord> getCartRecordData() {
        return cartDataRecord;
    }

    public void loadDataBaseData(){
        String query = "SELECT * FROM stockData";
        try{
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();
            while(rS.next()){
                stockData.add(new StockModel(
                            rS.getString("prodId"),
                            rS.getString("name"),
                            rS.getInt("quantity"),
                            rS.getInt("unit"),
                            rS.getString("supplier"),
                            rS.getString("brand"),
                            rS.getString("category"),
                            rS.getDouble("purchase_price"),
                            rS.getDouble("sell_price"),
                            rS.getObject("date"),
                            rS.getObject("time"),
                            rS.getString("added_by"),
                            rS.getString("description"),
                            rS.getObject("expireDate")
                            
                ));
               productTable.setItems(stockData);
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
    public void searchSellData(){
        searchBox.textProperty().addListener((observableValue,oldValue,newValue)->{
            filteredData.setPredicate((Predicate<? super StockModel>)user->{
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
        SortedList<StockModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedData);
    }
    
    public void loadCartRecordData(){
        subTotal();
        AddSalesCartController.cartData = FXCollections.observableArrayList();
        String query = "SELECT * FROM cartRecord";
        try{
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();
            while(rS.next()){
                CartRecord record = new CartRecord(
                            rS.getString("prod_id"),
                            rS.getString("name"),
                            rS.getDouble("sell_price"),
                            rS.getInt("quantity"),
                            rS.getDouble("total_price")
                            
                );
                cartDataRecord.add(record);
                AddSalesCartController.cartData.add(record);
               salesCartTable.setItems(cartDataRecord);
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
   public static String randomAplphaNum(int count){
       StringBuilder builder = new StringBuilder();
       while(count-- != 0){
           int character = (int)(Math.random()*ALPHA_NUM_STRING.length());
           builder.append(ALPHA_NUM_STRING.charAt(character));
       }
       return builder.toString();
   }
  
    @FXML
    void onTableClicked(MouseEvent event) {
        StockModel stock = (StockModel)productTable.getSelectionModel().getSelectedItem();
        ResultSet rs;
        String query = "SELECT * FROM stockData where prodId = '"+stock.getProductId()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                getProductID.setText(rs.getString("prodId"));
                getProductName.setText(rs.getString("name"));            
                getQuantity.setText(Integer.toString(rs.getInt("quantity")));            
                getBrand.setText(rs.getString("brand"));            
                getCategory.setText(rs.getString("category"));
                getDescription.setText(rs.getString("description"));
                getPurchasePrice.setText(Double.toString(rs.getDouble("purchase_price")));            
                getSellingPrice.setText(Double.toString(rs.getDouble("sell_price"))); 
                getUnit.setText(Integer.toString(rs.getInt("unit")));
            }
            temp = getProductID.getText();
           
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
    //Helps Calculate The total Cost while you select quantity.
    @FXML
    private void tfQuantityOnAction(KeyEvent event) {
        if (!getQuantityBought.getText().isEmpty()) {
            String givenQuantity = getQuantityBought.getText();
            int givenQtyInt = Integer.parseInt(givenQuantity);
            String currentQuantity = getQuantity.getText();
            int currentQtyInt = Integer.parseInt(currentQuantity);
            if (givenQtyInt > currentQtyInt) {
                getQuantityBought.clear();
                getTotalPrice.setText("0.0");
                getQtyLeft.setText("0.0");
            } else {
                quantity = Integer.parseInt(getQuantityBought.getText());
                float sellPrice = Float.parseFloat(getSellingPrice.getText());
                String netPrice = String.valueOf(sellPrice * quantity);
                String getQtyRem = String.valueOf(currentQtyInt - quantity);
                getTotalPrice.setText(netPrice);
                getQtyLeft.setText(getQtyRem);
            }
        } else {
            getTotalPrice.setText("0.0");
            getQtyLeft.setText("0.0");
        }
    }
    
    public void subTotal()
    {
        try {
            pS = conn.prepareStatement("select sum(total_price) as total from cartRecord");
            rS = pS.executeQuery();
            while (rS.next()) {
                getTotal.setText(rS.getString("total"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try{  
              //conn.close();
              pS.close();
              rS.close();
            }catch(Exception e){

            }
        }
    }
    

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        getCartRecordData().clear();
        getStockData().clear();
        searchBox.clear();
        getProductID.setText(null);
        getProductName.setText(null);
        getSellingPrice.setText(null);
        getPurchasePrice.setText(null);
        getQuantity.setText(null);
        getCategory.setText(null);
        getBrand.setText(null);
        getTotalPrice.setText(null);
        getDescription.setText(null);
        getQuantityBought.clear();
        getSalesDate.setValue(null);
        getUnit.setText(null);
        getQtyLeft.setText(null);
        
        salesCartTable.setItems(cartDataRecord);
        tblCartProductID.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        tblCartSellingPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        tblCartQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblCartTotal.setCellValueFactory(cellData -> cellData.getValue().totalPriceValueProperty().asObject());
        tblCartName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        loadCartRecordData();
        tblProductId.setCellValueFactory(cellData -> cellData.getValue().productIdProperty());
        tblProdName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tblPurchasePrice.setCellValueFactory(cellData -> cellData.getValue().purchaseProperty().asObject());
        tblSalesPrice.setCellValueFactory(cellData -> cellData.getValue().sellProperty().asObject());
        tblQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblBrand.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        tblCategory.setCellValueFactory(cellData -> cellData.getValue().categoryProperty()); 
        tblDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        loadDataBaseData();
    }
    
    @FXML
    void handlePrint(ActionEvent event) 
    {        
        String tendered = cashReceived.getText();
        if (!tendered.equals("")) {
          int amount = Integer.parseInt(tendered);
          AddSalesCartController.AmountTendered = amount;
        }
        this.subTotal();
        Stage receipt = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Receipt.fxml"));
            Scene receiptScene = new Scene(root);
            receipt.setScene(receiptScene);
            receipt.show();
            printNode(root);
            
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        cashReceived.clear();
        this.deleteCartEntry();
        this.btnRefreshOnAction(event);
    }
    
    public static void printNode(final Node node) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
	Paper paper = PrintHelper.createPaper("10x15", 100, 150, Units.MM);
            Printer printer = Printer.getDefaultPrinter();
            PageLayout pageLayout
                = printer.createPageLayout(paper, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
            PrinterAttributes attr = printer.getPrinterAttributes();
            PrinterJob job = PrinterJob.createPrinterJob();
            double scaleX
                = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
            double scaleY
                = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
            Scale scale = new Scale(scaleX, scaleY);
            node.getTransforms().add(scale);

            if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
              boolean success = job.printPage(pageLayout, node);
              if (success) {
                job.endJob();

              }
            }
            node.getTransforms().remove(scale);
          }
    
    @FXML
    void handleExit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    //Update Quantity in the stockData
    void handleUpdateQty() {
        String leftQuantity = getQtyLeft.getText();
        Integer qtyIntUpdate = Integer.parseInt(leftQuantity);
        if(qtyIntUpdate == 0){
            int reduction = 0;
            String query = "UPDATE stockData SET unit=?, quantity=? where prodId='"+temp+"'"; 
                try{    
                    pS = conn.prepareStatement(query);
                    pS.setInt(1, reduction);
                    pS.setInt(2, qtyIntUpdate);
                    pS.executeUpdate();
                        
                    } catch(SQLException e){
                    }finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                    }
        }else {
            String query = "UPDATE stockData SET quantity=? where prodId='"+temp+"'";     
            
                    try{
                        
                        pS = conn.prepareStatement(query);
                        pS.setInt(1, qtyIntUpdate);
                        pS.executeUpdate();
                        
                    } catch(SQLException e){
                    }finally{
                      try{  
                        //conn.close();
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                    }
        }   
                   
    }
    
    @FXML
    void handleAddToCart(ActionEvent event) {
        this.handleSaveButton();
        this.handleUpdateQty();
        if (isInputValid()){                                  
                    String id = getProductID.getText();
                    String name = getProductName.getText();
                    Integer qtity = Integer.parseInt(getQuantityBought.getText());
                    Double sellPrice = Double.parseDouble(getSellingPrice.getText());
                    Double totalPrice = Double.parseDouble(getTotalPrice.getText());
                            
                    String query = "INSERT INTO cartRecord (prod_id, sell_price, quantity, total_price, name) "
                        + "VALUES(?,?,?,?,?)";       
                    try{
                        
                        pS = conn.prepareStatement(query);
                        pS.setString(1, id);
                        pS.setDouble(2, sellPrice);
                        pS.setInt(3, qtity);
                        pS.setDouble(4, totalPrice);
                        pS.setString(5, name);
                        
         
                    } catch(SQLException e){
                    }finally{
                      try{  
                        pS.execute();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                    }  
                    okClicked = true;
                    if(okClicked)
                    { 
                        okClicked = true;
                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("Congratulation!!!");
                            tray.setMessage("Item Successfully Added to Cart!.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));
                    }
        }
                   
    }
    
    void deleteCartEntry(){
        try{
                String query = "DELETE FROM cartRecord";
                pS = conn.prepareStatement(query);
                pS.executeUpdate();
                }catch(SQLException e){
                }finally{
                      try{  
                        pS.close();
                        rS.close();
                      }catch(Exception e){
                          e.printStackTrace();
                      }
                }
    }

    void handleSaveButton() {
        if (isInputValid()){                                  
                        String saleId = getSalesID.getText();
                        String proId = getProductID.getText();
                        String name = getProductName.getText();
                        Integer qty = Integer.parseInt(getQuantityBought.getText());
                        Double total = Double.parseDouble(getTotalPrice.getText());
                        String brand = getBrand.getText();
                        String category = getCategory.getText();
                        Double purchasePrice = Double.parseDouble(getPurchasePrice.getText());
                        Double sellPrice = Double.parseDouble(getSellingPrice.getText());
                        Object date = getSalesDate.getValue();
                        String salesBy = getSoldBy.getText();
                        String describe = getDescription.getText();
                        
                String query = "INSERT INTO sales (sell_id, product_id, date, purchase_price, sales_price, quantity, total_price, description, sold_by, name, category, brand) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                         
                    try{
                        
                        pS = conn.prepareStatement(query);
                        pS.setString(1, saleId);
                        pS.setString(2, proId);
                        pS.setObject(3, date);
                        pS.setDouble(4, purchasePrice);
                        pS.setDouble(5, sellPrice);
                        pS.setInt(6, qty);
                        pS.setDouble(7, total);
                        pS.setString(8, describe);
                        pS.setString(9, salesBy);
                        pS.setString(10, name);
                        pS.setString(11, category);
                        pS.setString(12, brand);
         
                    } catch(SQLException e){
                    }finally{
                      try{  
                        //conn.close();
                        pS.executeUpdate();
                        pS.close();
                      }catch(Exception e){
                          
                      }
                    } 
                }    
        
    }  
       
    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (getProductID.getText().isEmpty() && getProductName.getText().isEmpty()
                && getSellingPrice.getText().isEmpty() && getPurchasePrice.getText().isEmpty()
                && getQuantity.getText().isEmpty() && getCategory.getText().isEmpty() && getBrand.getText().isEmpty()
                && getTotalPrice.getText().isEmpty() && getDescription.getText().isEmpty()) {
            errorMessage += "No valid Selection(Must make a Selection)!\n"; 
        }
              
        if (getSalesDate.toString().isEmpty()) {
            errorMessage += "No valid Date Choosen!\n";
        } 
       
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("No Product Selected!!!");
            alert.setHeaderText("Please Select Products for Sales");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public void generateSalesReceipt(){
        /**Jasper Report Method
        String reportLocale = "src\\resources\\image\\invoice.jrxml";   
        Map params = new HashMap();
        params.put("sold_by", getSoldBy.getText());
        params.put("sell_id", getSalesID.getText());
        params.put("sales_price", Double.parseDouble(getSellingPrice.getText()));
        params.put("quantity", Integer.parseInt(getQuantityBought.getText()));
        params.put("name", getProductName.getText());
        params.put("total_price", Double.parseDouble(getTotalPrice.getText()));
        //Compiling the final report
        JasperReport jr = JasperCompileManager.compileReport(reportLocale);
        JasperPrint jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
        JasperViewer.viewReport(jp, false);
        **/
    }
    
}
