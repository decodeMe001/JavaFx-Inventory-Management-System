package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.CartSales;
import com.iSoftTech.inventoryms.model.OrganisationModel;
import com.iSoftTech.inventoryms.model.StockModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 *
 * @author Dada abiola
 */
public class HomeController implements Initializable {

    @FXML
    private AnchorPane organisationDetailsPage;

    @FXML
    private Button loadSalesFinance;

    @FXML
    private Button loadSales;

    @FXML
    private Button btnRefresh;

    @FXML
    private AnchorPane FinancialChartPage;

    @FXML
    private AnchorPane salesChartPage;

    @FXML
    private AnchorPane stockChartPage;

    @FXML
    private PieChart stockChart;

    @FXML
    private Label status;

    @FXML
    private GridPane root;

    @FXML
    private Button updateButton;

    @FXML
    private Label getName;

    @FXML
    private Label getNumberOfSales;

    @FXML
    private Label getPhone;

    @FXML
    private Label getOffice;

    @FXML
    private Label getMail;

    @FXML
    private Label getEmployee;

    @FXML
    private Label getSupplier;

    @FXML
    private Label getStock;

    @FXML
    private Label getSales;

    @FXML
    private TableView<CartSales> salesCartTable;

    @FXML
    private TableColumn<CartSales, String> tblCartProductID;

    @FXML
    private TableColumn<CartSales, Double> tblCartSellingPrice;

    @FXML
    private TableColumn<CartSales, Integer> tblCartQuantity;

    @FXML
    private TableColumn<CartSales, Double> tblCartTotal;

    @FXML
    private TableColumn<CartSales, String> tblCartName;

    @FXML
    private TableView<StockModel> tbProductInfo;

    @FXML
    private TableColumn<StockModel, String> tbprodName;

    @FXML
    private TableColumn<StockModel, Object> tbexpireDate;

    @FXML
    private TableColumn<StockModel, String> tbdaysLeft;

    @FXML
    private TableColumn<StockModel, String> tbstatusUpdate;

    @FXML
    private Button btnExport;

    Connection conn = SqliteConnection.Connector();
    private static final boolean hasData = false;
    PreparedStatement pS;
    ResultSet rS;
    private final ObservableList data = FXCollections.observableArrayList();
    public ObservableList<OrganisationModel> orgData = FXCollections.observableArrayList();
    public ObservableList<CartSales> cartDataRecord = FXCollections.observableArrayList();
    public ObservableList<StockModel> expireRecord = FXCollections.observableArrayList();
    public CurrentStockController stock;
    final Timeline timeline = new Timeline();
    Stage primaryStage;
    private static boolean okClicked = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        assert salesCartTable != null;
        assert tbProductInfo != null;
        // Initialize the Shop Cart table with the columns.
        tblCartProductID.setCellValueFactory(cellData -> cellData.getValue().productIDProperty());
        tblCartSellingPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty().asObject());
        tblCartQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityValueProperty().asObject());
        tblCartTotal.setCellValueFactory(cellData -> cellData.getValue().totalPriceValueProperty().asObject());
        tblCartName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());

        tbprodName.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tbexpireDate.setCellValueFactory(cellData -> cellData.getValue().expireDateProperty());
        tbstatusUpdate.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
        tbdaysLeft.setCellValueFactory(cellData -> cellData.getValue().getDaysLeftProperty());

        loadChartStock();
        loadDataBaseData();
        loadCartRecordData();
        loadExpirationDate();
        valueCount();
        stock = new CurrentStockController();
        stock.loadDataBaseData();

        stockChart.getData().addAll(data);
        stockChart.getData().stream().forEach((dat) -> {
            dat.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                status.setText(String.valueOf("Amount in Stock: " + dat.getPieValue()));
            });
        });

    }

    public void loadExpirationDate() {
        String query = "SELECT * FROM stockData";
        try {
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();
            
            while (rS.next()) {
                String  Productstatus = "Active";
                String daysLeft = "";
                     if (rS.getObject("expireDate") != null) {
                    String initDate = rS.getObject("expireDate").toString();
                    Calendar expDate = Calendar.getInstance();
                    String dateExpired[] = initDate.split("-");
                    expDate.set(Integer.parseInt(dateExpired[0]), Integer.parseInt(dateExpired[1]) -1, Integer.parseInt(dateExpired[2]));
                    Calendar now = Calendar.getInstance();
                    
                    Date old = new Date(expDate.getTimeInMillis());
                    Date newd = new Date(now.getTimeInMillis());
                    long minutes = HomeController.getDateDiff(newd, old, TimeUnit.MINUTES);
                     if((minutes/60)/24 < 0) 
                     {
                        daysLeft = " " + (-1 * ((minutes/60)/24)) + " day(s) ago";
                        Productstatus = "Expired";
                        
                     } else if((minutes/60)/24 == 0)
                     {
                      daysLeft = "Today";
                      Productstatus = "Expired";
                        // Custom rendering of the table cell.
                     } else 
                     {
                        daysLeft = " " + ((minutes/60)/24) + " day(s)";
                        Productstatus = "Active";
                     }
                     // Custom rendering of the table cell.
                    tbstatusUpdate.setCellFactory((TableColumn<StockModel, String> column) -> {
                        return new TableCell<StockModel, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item == null || empty) {
                                    setText(null);
                                    setStyle("");
                                } else {
                                    // 
                                    if (item.equalsIgnoreCase("Expired")) {
                                        setTextFill(Color.WHITE);
                                        setText("Expired");
                                        setStyle("-fx-background-color: #B71C1C; -fx-font-family: \"Helvetica\"; -fx-font-weight: BOLD;");
                                    } else {
                                        setTextFill(Color.WHITE);
                                        setText("Active");
                                        setStyle("-fx-background-color: #00C853; -fx-font-family: \"Helvetica\"; -fx-font-weight: BOLD;");
                                    }
                                }
                            }
                        };
                    });
                }
                
                  StockModel model = new StockModel(
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
                );
                  model.setStatus(Productstatus);
                  model.setDaysLeft(daysLeft);
                expireRecord.add(model);
                tbProductInfo.setItems(expireRecord);
           
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //conn.close();
                pS.close();
                rS.close();
            } catch (Exception e) {

            }
        }
    }

    public void loadCartRecordData() {
        String query = "SELECT * FROM sales";
        try {
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();
            while (rS.next()) {
                cartDataRecord.add(new CartSales(
                        rS.getString("sell_id"),
                        rS.getString("product_id"),
                        rS.getString("name"),
                        rS.getObject("date"),
                        rS.getDouble("purchase_price"),
                        rS.getDouble("sales_price"),
                        rS.getInt("quantity"),
                        rS.getDouble("total_price"),
                        rS.getString("description"),
                        rS.getString("category"),
                        rS.getString("brand"),
                        rS.getString("sold_by") 
                ));
                salesCartTable.setItems(cartDataRecord);
            }
        } catch (Exception e) {
        } finally {
            try {
                //conn.close();
                pS.close();
                rS.close();
            } catch (Exception e) {

            }
        }
    }

    /**
     * Get a diff between two dates
     *
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        loadDataBaseData();
        valueCount();
    }

    /**
     *
     * Loading Default Data
     */
    public void valueCount() {
        try {
            pS = conn.prepareStatement("select sum(total_price) from sales");
            rS = pS.executeQuery();
            while (rS.next()) {
                getSales.setText(rS.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //conn.close();
                pS.close();
                rS.close();
            } catch (Exception e) {

            }
        }
    }

    public void loadDataBaseData() {
        try {
            pS = conn.prepareStatement("select count(*) from sales");
            rS = pS.executeQuery();
            while (rS.next()) {
                getNumberOfSales.setText(rS.getString(1));
            }
            pS = conn.prepareStatement("select count(*) from supplierData");
            rS = pS.executeQuery();
            while (rS.next()) {
                getSupplier.setText(rS.getString(1));
            }
            pS = conn.prepareStatement("select count(*) from stockData");
            rS = pS.executeQuery();
            while (rS.next()) {
                getStock.setText(rS.getString(1));
            }
            pS = conn.prepareStatement("select count(*) from staffData");
            rS = pS.executeQuery();
            while (rS.next()) {
                getEmployee.setText(rS.getString(1));
            }

            pS = conn.prepareStatement("SELECT * FROM orgInfo where id=1");
            rS = pS.executeQuery();
            while (rS.next()) {
                getName.setText(rS.getString(2));
                getPhone.setText(rS.getString(3));
                getMail.setText(rS.getString(4));
                getOffice.setText(rS.getString(5));
            }
        } catch (Exception e) {
        } finally {
            try {
                //conn.close();
                pS.close();
                rS.close();
            } catch (Exception e) {

            }
        }
    }

    //Export to Excel Spreed sheet
    @FXML
    void ExportAction(ActionEvent event) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(*.xlsx)", "*.xls");
        chooser.getExtensionFilters().add(filter);
        //FileOutputStream fileOut = new FileOutputStream("SalesDetails.xlsx");//for < 2007 use xls
        // Show save dialog
        File file = chooser.showSaveDialog(btnExport.getScene().getWindow());
        if (file != null) {
            saveXLSFile(file);

        }

    }

    private void saveXLSFile(File file) {
        try {
            String query = "select * from sales";
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();

            //Create Excel Workbook
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook();// For earlier excel version use HSSF
            XSSFSheet sheet = wb.createSheet("SIMS Sales Report");
            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("DATE");
            header.createCell(2).setCellValue("PRODUCT");
            header.createCell(3).setCellValue("BRAND");
            header.createCell(4).setCellValue("PURCHASE PRICE");
            header.createCell(5).setCellValue("SALES PRICE");
            header.createCell(6).setCellValue("QUANTITY BOUGHT");
            header.createCell(7).setCellValue("TOTAL PRICE");

            sheet.setZoom(150);//scale 150%
            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(1, 256 * 25);//256 char lenght
            sheet.setColumnWidth(2, 256 * 25);
            sheet.setColumnWidth(3, 256 * 25);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            int index = 1;
            //fetch database
            while (rS.next()) {
                XSSFRow row = sheet.createRow(index);
                row.createCell(0).setCellValue(rS.getInt("id"));
                row.createCell(1).setCellValue(rS.getString("date"));
                row.createCell(2).setCellValue(rS.getString("name"));
                row.createCell(3).setCellValue(rS.getString("brand"));
                row.createCell(4).setCellValue(rS.getDouble("purchase_price"));
                row.createCell(5).setCellValue(rS.getDouble("sales_price"));
                row.createCell(6).setCellValue(rS.getInt("quantity"));
                row.createCell(7).setCellValue(rS.getDouble("total_price"));
                index++;
            }

            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();

            okClicked = true;
            if (okClicked) {
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Excel Sheet Created!!!");
                tray.setMessage("Financial Details Successfully Created.");
                tray.setNotificationType(notificationType);
                tray.showAndDismiss(Duration.millis(3000));
            }

        } catch (SQLException e) {
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //conn.close();
                pS.close();
                rS.close();
            } catch (Exception e) {

            }
        }
    }

    public void loadChartStock() {
        String query = "select quantity, name FROM stockData ORDER BY quantity";
        try {
            pS = conn.prepareStatement(query);
            rS = pS.executeQuery();
            while (rS.next()) {
                data.add(new PieChart.Data(rS.getString("name"), rS.getInt("quantity")));
            }

        } catch (Exception e) {
        } finally {
            try {
                //conn.close();
                pS.close();
                rS.close();
            } catch (Exception e) {

            }
        }
    }

    @FXML
    boolean updateDetails(ActionEvent event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AddUpdateOrg.class.getResource("UpdateOrg.fxml"));
            AnchorPane unitOverview = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update Details");
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(unitOverview);
            scene.setFill(new Color(0, 0, 0, 0));
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.setMaximized(false);
            dialogStage.setResizable(false);

            //Organisation Details.
            AddUpdateOrg controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUpdateDetails();

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            return false;
        }

    }

}
