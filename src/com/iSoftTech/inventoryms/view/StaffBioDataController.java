package com.iSoftTech.inventoryms.view;

import com.iSoftTech.inventoryms.DB.SqliteConnection;
import com.iSoftTech.inventoryms.model.StaffModel;
import java.awt.image.BufferedImage;
import java.io.File;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dada abiola
 */
public class StaffBioDataController implements Initializable {
    
    private boolean okClicked = false;
    Stage dialogStage;
    static String temp;
    Connection conn = SqliteConnection.Connector();  

    PreparedStatement pS = null;
    ResultSet rS = null;

    private BufferedImage bufferedImage;
    public Image image;
    public Blob userImage;
  
    
    @FXML
    private ImageView profilePic;

    @FXML
    private Text Name;

    @FXML
    private Text office;

    @FXML
    private Text phone;

    @FXML
    private Text address;

    @FXML
    private Text dob;

    @FXML
    private Text id;

    @FXML
    private Text email;
    @FXML
    private Text postalCode;

    @FXML
    private Text salaryEarned;

    @FXML
    private Text dateEmployeed;

    @FXML
    private Button btnClose;
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
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
 
    
    public void setStaffData(StaffModel staffModel) {
        
        ResultSet rs;
        String query = "SELECT * FROM staffData where staff_id = '"+staffModel.getEmployeeID()+"'";
        try{
            pS = conn.prepareStatement(query);
            rs = pS.executeQuery();
            while(rs.next()){
                
                Name.setText(rs.getString("name"));            
                address.setText(rs.getString("address"));            
                phone.setText(rs.getString("phone"));            
                postalCode.setText(rs.getString("postal_code"));            
                office.setText(rs.getString("office"));   
                dob.setText(rs.getObject("dob").toString());
                dateEmployeed.setText(rs.getObject("edate").toString());
                salaryEarned.setText(Double.toString(rs.getDouble("salary")));                         
                email.setText(rs.getString("email"));  
                id.setText(rs.getString("staff_id"));
                
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
                profilePic.setImage(image);

            }
            temp = id.getText();
           
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

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
