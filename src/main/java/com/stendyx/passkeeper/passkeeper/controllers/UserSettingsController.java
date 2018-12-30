package com.stendyx.passkeeper.passkeeper.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.stendyx.passkeeper.passkeeper.helpers.SceneHelper;
import com.stendyx.passkeeper.passkeeper.libs.Methods;
import com.stendyx.passkeeper.passkeeper.models.User;
import com.stendyx.passkeeper.passkeeper.objects.Users;
import com.stendyx.passkeeper.passkeeper.properties.Pages;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.LoginValidator;

import java.util.Optional;

public class UserSettingsController {

    User user = new User();


    @FXML
    private TextField userName;

    @FXML
    private TextField oldPassword;

    @FXML
    private TextField newPassword1;

    @FXML
    private TextField newPassword2;

    @FXML
    private Button submit;

    @FXML
    private Label actionTarget;

    @FXML
    void setTextToUserName(String textToUserName) {
        userName.setText(textToUserName);
    }


    @FXML
    public void handleSubmit(ActionEvent event) {
        Users users = new Users();
        LoginValidator validator = new LoginValidator();
        String password = user.getUser().getPassword();
        boolean isErrors = false;
        if (!validator.isEmpty(oldPassword)) {
            if (newPassword1.getText().equals(newPassword2.getText())) {
                Validator status = users.authorize(user.getLogin(), oldPassword.getText());
                if (status.isError()) {
                    actionTarget.setTextFill(validator.getPaint(status.isError()));
                    actionTarget.setText(status.getMessage());
                    isErrors = true;
                } else {
                    password = Methods.hashString(newPassword1.getText());
                }
            } else {
                Validator status = Validator.ERROR_DIFFERENT_PASS;
                actionTarget.setTextFill(validator.getPaint(status.isError()));
                actionTarget.setText(status.getMessage());
                isErrors = true;
            }
        }
        if (!isErrors) {
            users.updateUser(user.getLogin(), password, userName.getText());
            Validator status = Validator.SUCCESS_UPDATE;
            actionTarget.setTextFill(validator.getPaint(status.isError()));
            actionTarget.setText(status.getMessage());

            Stage stage = (Stage) ((Stage) ((Node) event.getSource()).getScene().getWindow()).getOwner();
            SceneHelper.changeSceneParams(stage, userName.getText(), user.getLogin(), password);
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {

        SceneHelper.closeWindows(event, 1);

    }


    @FXML
    public void handleDelete(ActionEvent event) {
        Optional<FXMLLoader> loader = SceneHelper.createPopup(event, Pages.USERDELETE.getPage());
        if (loader.isPresent()) {
            UserSettingsDeleteController controller = loader.get().getController();
            controller.user.setLogin(user.getLogin());
            controller.user.setUser(user.getUser());
        }
    }

    @FXML
    public void handleType() {
        LoginValidator validator = new LoginValidator();
        validator.validateEmptyFields(submit, userName);
        if ((!validator.isEmpty(oldPassword) || !validator.isEmpty(newPassword1) || !validator.isEmpty(newPassword2)) && !validator.isEmpty(userName)) {
            validator.validateEmptyFields(submit, oldPassword, newPassword1, newPassword2);
        }
    }
}
