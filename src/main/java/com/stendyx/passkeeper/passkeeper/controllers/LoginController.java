package com.stendyx.passkeeper.passkeeper.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import com.stendyx.passkeeper.passkeeper.helpers.SceneHelper;
import com.stendyx.passkeeper.passkeeper.objects.Users;
import com.stendyx.passkeeper.passkeeper.properties.Pages;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.LoginValidator;

public class LoginController implements SceneHelper {


    @FXML
    private Label actiontarget;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submit;


    void setActionTargetContent(String text, Paint paint) {
        actiontarget.setText(text);
        actiontarget.setTextFill(paint);
    }


    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("");
        Users users = new Users();
        String login = loginField.getText();
        String password = passwordField.getText();

        Validator status = users.authorize(login, password);
        LoginValidator loginValidator = new LoginValidator();
        actiontarget.setTextFill(loginValidator.getPaint(status.isError()));

        if (status.isError())
            actiontarget.setText(status.getMessage());
        else {
            SceneHelper.goToMain(event, login);
        }
    }

    @FXML
    protected void handleRegister(ActionEvent event) {
        SceneHelper.changeScene(event, Pages.REGISTER.getPage());
    }

    @FXML
    protected void handleType(KeyEvent event) {
        LoginValidator validator = new LoginValidator();
        validator.validateEmptyFields(submit, loginField, passwordField);

    }


}