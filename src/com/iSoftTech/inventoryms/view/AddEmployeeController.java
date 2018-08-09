package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.StaffModel;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class AddEmployeeController implements Initializable {
    
    static String temp;
    @FXML
    private Rectangle recUsrImage;
    @FXML
    private Button btnAttachImage;
    @FXML
    private JFXTextField staffname;
    @FXML
    private JFXTextField staffPhone;
    @FXML
    private JFXTextField postalCode;
    @FXML
    private JFXTextField officeHeld;
    @FXML
    private JFXTextField staffSalary;
    @FXML
    private JFXTextField staffEmail;
    @FXML
    private JFXDatePicker dOB;
    @FXML
    private JFXDatePicker dateOfEmployment;
    @FXML
    private JFXTextArea staffAddress;
    @FXML
    private Label getStaffID;
    @FXML
    private Button saveButton;
    @FXML
    private Button updateButton;
    
    Stage dialogStage;
    private Stage primaryStage;
    private boolean okClicked = false;
    Connection conn = SqliteConnection.Connector(); 
    private static boolean hasData = false;
    PreparedStatement pS = null;
    PreparedStatement hs = null;
    ResultSet rS = null;
    private static final String ALPHA_NUM_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private File file;
    private BufferedImage bufferedImage;
    private Image image;
    public Blob userImage;
    private String imagePath;
    private FileInputStream fis;
    private ImageView imageView;
   
    
    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
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
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        
        getStaffID.setText(randomAplphaNum(5));
       
    }
    
    public static String randomAplphaNum(int count){
       StringBuilder builder = new StringBuilder();
       while(count-- != 0){
           int character = (int)(Math.random()*ALPHA_NUM_STRING.length());
           builder.append(ALPHA_NUM_STRING.charAt(character));
       }
       return builder.toString();
   }
    public void setUpdateImageDisable(){
        btnAttachImage.setDisable(true);
    }
    
    public void setSaveDisable(){
        saveButton.setDisable(true);
    }
    
    public void setUpdateDisable(){
        updateButton.setDisable(true);
    }

    @FXML
    private void btnAttachImageOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG (Joint Photographic Group)", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG (Joint Photographic Experts Group)", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG (Portable Network Graphics)", "*.png")
        );

        fileChooser.setTitle("Choose an Image File");
        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            System.out.println(file);
            bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            recUsrImage.setFill(new ImagePattern(image));
            imagePath = file.getAbsolutePath();
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (isInputValid()){                                  
                        String name = staffname.getText();
                        String phone = staffPhone.getText();
                        String postal = postalCode.getText();
                        String office = officeHeld.getText();
                        Double salary = Double.parseDouble(staffSalary.getText());
                        String add = staffAddress.getText();
                        String mail = staffEmail.getText();
                        Object dateE = dateOfEmployment.getValue();
                        Object dateB = dOB.getValue();
                        String staffID = getStaffID.getText();
                        
                String query = "INSERT INTO staffData (name, address, phone, postal_code, office, dob, edate, salary, email, staff_id, image) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                         
                    try{
                        
                        pS = conn.prepareStatement(query);
                        pS.setString(1, name);
                        pS.setString(2, add);
                        pS.setString(3, phone);
                        pS.setString(4, postal);
                        pS.setString(5, office);
                        pS.setObject(6, dateB);
                        pS.setObject(7, dateE);
                        pS.setDouble(8, salary);
                        pS.setString(9, mail);
                        pS.setString(10, staffID);
                        
                        InputStream fis = new FileInputStream(file);
                        pS.setBinaryStream(11, (InputStream)fis, (int)file.length());
                        pS.execute();
         
                    } catch(SQLException e){
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AddEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                    }finally{
                      try{  
                        //conn.close();
                        
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
                            tray.setMessage("Staff Data Successfully Added!.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));
                            ((Node)(event.getSource())).getScene().getWindow().hide();
                    }
        }
        
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        
        if(isInputValid()){
            
                        String name = staffname.getText();
                        String phone = staffPhone.getText();
                        String postal = postalCode.getText();
                        String office = officeHeld.getText();
                        Double salary = Double.parseDouble(staffSalary.getText());
                        String add = staffAddress.getText();
                        String mail = staffEmail.getText();
                        Object dateE = dateOfEmployment.getValue();
                        Object dateB = dOB.getValue();
                        String staffID = getStaffID.getText();
                        
                        String data = "update staffData set name=?, address=?, phone=?, postal_code=?, "
                            + "office=?, dob=?, edate=?,"
                            + " salary=?, email=?, staff_id=? where staff_id='"+temp+"'";
                try {     
                    pS = conn.prepareStatement(data);
                    
                        pS.setString(1, name);
                        pS.setString(2, add);
                        pS.setString(3, phone);
                        pS.setString(4, postal);
                        pS.setString(5, office);
                        pS.setObject(6, dateB);
                        pS.setObject(7, dateE);
                        pS.setDouble(8, salary);
                        pS.setString(9, mail);
                        pS.setString(10, staffID);  
                       
                        pS.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AddStockDataController.class.getName()).log(Level.SEVERE, null, ex);
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
                tray.setMessage("Staff Data Successfully Updated.");
                tray.setNotificationType(notificationType);
                tray.showAndDismiss(Duration.millis(3000));
            }
            ((Node)(event.getSource())).getScene().getWindow().hide();
       }
             
    }
    
    @FXML
    private void handleExit(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    public void setStaffData(StaffModel staffModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM staffData where staff_id = '"+staffModel.getEmployeeID()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                
                staffname.setText(rs.getString("name"));            
                staffAddress.setText(rs.getString("address"));            
                staffPhone.setText(rs.getString("phone"));            
                postalCode.setText(rs.getString("postal_code"));            
                officeHeld.setText(rs.getString("office"));   
                dOB.setValue(LocalDate.parse(rs.getObject("dob").toString()));
                dateOfEmployment.setValue(LocalDate.parse(rs.getObject("edate").toString()));
                staffSalary.setText(Double.toString(rs.getDouble("salary")));                         
                staffEmail.setText(rs.getString("email"));  
                getStaffID.setText(rs.getString("staff_id"));
                
                //set dafault image if image=null
                    InputStream is = rs.getBinaryStream("image"); //saved image column
                    OutputStream os = new FileOutputStream(new File("photo.jpg")); 
                    byte[] content = new byte[1024];
                    int size = 0;
                    while((size = is.read(content)) != -1){
                        os.write(content, 0, size);
                    }
                    os.close();
                    is.close();
                
                image = new Image("file:photo.jpg", 100, 150, true, true);
                recUsrImage.setFill(new ImagePattern(image));

            }
            temp = getStaffID.getText();
           
        }catch (SQLException e){
               Logger.getLogger(AddEmployeeController.class.getName()).log(Level.SEVERE, null, e);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddEmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                      try{  
                        //conn.close();
                        pS.close();
                        rS.close();
                      }catch(Exception e){
                          
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
       
        if (staffname.getText() == null || staffname.getText().length() == 0) {
            errorMessage += "Name field is Empty!\n"; 
        } 
        if (staffPhone.getText() == null || staffPhone.getText().length() == 0) {
            errorMessage += "Phone field is Empty!\n"; 
        } 
        if (postalCode.getText() == null || postalCode.getText().length() == 0) {
            errorMessage += "Postal Code field is Empty!\n"; 
        } 
        if (officeHeld.getText() == null || officeHeld.getText().length() == 0) {
            errorMessage += "Office field is Empty!\n"; 
        } 
        if (staffAddress.getText() == null || staffname.getText().length() == 0) {
            errorMessage += "Address field is Empty!\n"; 
        } 
        if (staffSalary.getText() == null || staffSalary.getText().length() == 0) {
            errorMessage += "Salary Field is Empty!\n"; 
        } 
        if (staffEmail.getText() == null || staffEmail.getText().length() == 0) {
            errorMessage += "Email Field is Empty!\n"; 
        } 
        if (dateOfEmployment.toString().isEmpty()) {
            errorMessage += "No valid Date Choosen!\n";
        } 
        if (dOB.toString().isEmpty()) {
            errorMessage += "No valid Date Choosen!\n";
        }
        if (recUsrImage.getFill() == null){
            errorMessage += "No valid Image Choosen!\n";
        }
       
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Empty Fields Found!!");
            alert.setHeaderText("Please Enter All Fields with Valid Data");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
}
