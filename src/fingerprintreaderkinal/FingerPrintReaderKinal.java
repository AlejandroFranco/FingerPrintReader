/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintreaderkinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Broadcast
 */
public class FingerPrintReaderKinal extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Image anotherIcon = new Image("/Images/logo.jpg");
        stage.getIcons().add(anotherIcon);
        stage.centerOnScreen();
        stage.setTitle("Lector de Huellas kinal");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setWidth(909);
        stage.setHeight(729);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
