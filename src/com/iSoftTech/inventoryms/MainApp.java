package com.iSoftTech.inventoryms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Dada abiola
 */
public class MainApp extends Application{
    
    private static Stage primaryStage;

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        
    }

    @Override
    public void start(Stage primaryStage) {
        
        try{
            Parent root = FXMLLoader.load(getClass().getResource("view/LoginPage.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            scene.setFill(new Color(0, 0, 0, 0));
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.getIcons().add(new Image("resources/images/Briefcase.png"));
            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch(Exception ex){
        }
        /**
             // Die after October 1, 2010
        Calendar expireDate = Calendar.getInstance();
        // January is 0 (y, m, d)
        expireDate.set(2017, 3, 7);
        // Get current date and compare
        if (Calendar.getInstance().after(expireDate)) {
          // Die
          System.exit(0);
        }**/
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
}
