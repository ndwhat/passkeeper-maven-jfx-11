package com.stendyx.passkeeper.passkeeper.helpers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.stendyx.passkeeper.passkeeper.Main;
import com.stendyx.passkeeper.passkeeper.controllers.MainController;
import com.stendyx.passkeeper.passkeeper.objects.User;
import com.stendyx.passkeeper.passkeeper.objects.Users;
import com.stendyx.passkeeper.passkeeper.objects.workspace.WorkSpaces;
import com.stendyx.passkeeper.passkeeper.properties.Pages;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface SceneHelper {


    /**
     * Change scene
     *
     * @param event {@link Event}
     * @param page  {@link String}
     */
    static Optional<FXMLLoader> changeScene(Event event, String page) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(page));
            Parent registerParent = loader.load();
            Scene registerScene = new Scene(registerParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(registerScene);
            window.show();

            return Optional.of(loader);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
        }
        Logger.getGlobal().log(Level.WARNING, "Empty loader in:" + event.getSource() + ". From page: " + page);
        return Optional.empty();
    }

    /**
     * Create Popup
     *
     * @param event {@link Event}
     * @param page  {@link String}
     */
    static Optional<FXMLLoader> createPopup(Event event, String page) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(page));
            Parent registerParent = loader.load();
            Scene registerScene = new Scene(registerParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            final Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(window);
            popup.setScene(registerScene);
            popup.setResizable(false);
            popup.show();
            return Optional.of(loader);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
        }
        Logger.getGlobal().log(Level.WARNING, "Empty loader in:" + event.toString() + ". From page: " + page);
        return Optional.empty();
    }


    static void goToMain(Event event, String login) {
        Optional<FXMLLoader> loader = SceneHelper.changeScene(event, Pages.MAIN.getPage());
        if (loader.isPresent()) {
            MainController controller = loader.get().getController();
            User user = new Users().getUser(login);
            controller.setTextToName(user.getName());
            controller.user.setLogin(login);
            controller.user.setUser(user);
            controller.categories = new WorkSpaces().getWorkSpaces(login);
            controller.generateTabs();

        }
    }

    /**
     * Close Popup
     *
     * @param event {@link Event}
     */
    static void closeWindows(ActionEvent event, int count) {
        if (count < 1)
            throw new IllegalArgumentException("Argument must be 1 and more");

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
        if (count > 1) {
            recursiveHide(window, count - 1);
        }
    }


    static private void recursiveHide(Stage stage, int count) {
        if (count > 0) {
            Stage window = (Stage) stage.getOwner();
            window.hide();
            recursiveHide(window, count - 1);
        }
    }

    static MainController changeSceneParams(Stage stage, String userName, String login, String password) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(Pages.MAIN.getPage()));
            Parent registerParent = loader.load();
            Scene registerScene = new Scene(registerParent,stage.getScene().getWidth(),stage.getScene().getHeight());
            MainController controller = loader.getController();
            controller.setTextToName(userName);
            controller.user.setLogin(login);
            controller.user.setUser(new com.stendyx.passkeeper.passkeeper.objects.User(userName, password));
            controller.categories = new WorkSpaces().getWorkSpaces(login);
            controller.generateTabs();
            stage.setScene(registerScene);
            return controller;
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
        }
        return  new MainController();
    }




}
