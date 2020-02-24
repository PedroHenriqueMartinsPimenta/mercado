/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; 
import javafx.stage.Stage;

/**
 *
 * @author Pedro Henrique
 */
public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Logar");
            primaryStage.setResizable(false);
            Image img = new Image("/midia/logo.png");
            primaryStage.getIcons().add(img);
            primaryStage.show();
            stage = primaryStage;
        
            
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
