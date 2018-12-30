package com.stendyx.passkeeper.passkeeper.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import com.stendyx.passkeeper.passkeeper.helpers.SceneHelper;
import com.stendyx.passkeeper.passkeeper.models.User;
import com.stendyx.passkeeper.passkeeper.objects.Users;
import com.stendyx.passkeeper.passkeeper.objects.workspace.Category;
import com.stendyx.passkeeper.passkeeper.objects.workspace.Project;
import com.stendyx.passkeeper.passkeeper.objects.workspace.WorkSpaces;
import com.stendyx.passkeeper.passkeeper.properties.Pages;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.LoginValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


public class UserSettingsDeleteController {

    User user = new User();

    @FXML
    private PasswordField password;

    @FXML
    private Label actiontarget;

    @FXML
    private Button deleteButton;

    @FXML
    public void handleCancel(ActionEvent event) {
        SceneHelper.closeWindows(event, 1);
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        Users users = new Users();
        WorkSpaces workSpaces = new WorkSpaces();


        Validator status = users.authorize(user.getLogin(), password.getText());
        LoginValidator loginValidator = new LoginValidator();

        if (status.isError()) {
            actiontarget.setTextFill(loginValidator.getPaint(status.isError()));
            actiontarget.setText(status.getMessage());
        } else {
            users.deleteUser(user.getLogin());
            SceneHelper.closeWindows(event, 3);
            HashMap<Category, ArrayList<Project>> workSpaces1 = workSpaces.getWorkSpaces(user.getLogin());
            workSpaces1.forEach((category,value) ->  workSpaces.deleteWorkSpace(user.getLogin(),category));

            Optional<FXMLLoader> loader = SceneHelper.changeScene(event, Pages.LOGIN.getPage());
            if (loader.isPresent()) {
                LoginController controller = loader.get().getController();
                LoginValidator validator = new LoginValidator();
                Validator successDeleted = Validator.SUCCESS_DELETE;
                controller.setActionTargetContent(successDeleted.getMessage(), validator.getPaint(successDeleted.isError()));
            }
        }
    }

    @FXML
    public void handleType() {
        LoginValidator validator = new LoginValidator();
        validator.validateEmptyFields(deleteButton, password);
    }

}
