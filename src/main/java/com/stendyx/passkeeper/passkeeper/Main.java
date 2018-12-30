package com.stendyx.passkeeper.passkeeper;


import com.stendyx.passkeeper.passkeeper.properties.Pages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) {


        try {

            Parent content = FXMLLoader.load(getClass().getResource(Pages.LOGIN.getPage()));
            Scene scene = new Scene(content);
            primaryStage.setTitle(Pages.LOGIN.getTitle());
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.getIcons().add(
                    new Image("img/logo.png"));
        } catch (IOException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }


    }
}
