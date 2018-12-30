package com.stendyx.passkeeper.passkeeper.controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.stendyx.passkeeper.passkeeper.helpers.SceneHelper;
import com.stendyx.passkeeper.passkeeper.models.User;
import com.stendyx.passkeeper.passkeeper.objects.workspace.Category;
import com.stendyx.passkeeper.passkeeper.objects.workspace.Project;
import com.stendyx.passkeeper.passkeeper.objects.workspace.WorkSpaces;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.CategoryValidator;
import com.stendyx.passkeeper.passkeeper.validators.LoginValidator;
import com.stendyx.passkeeper.passkeeper.validators.ProjectValidator;


public class AddProjectController {


    User user = new User();

    int activeTab;

    Category category;

    @FXML
    private TextField nameField;

    @FXML
    private TextField hostField;

    @FXML
    private ChoiceBox<String> typeSelect;

    @FXML
    private TextField protocolField;

    @FXML
    private TextField portField;

    @FXML
    private Button submit;

    @FXML
    private Button deleteButton;

    @FXML
    private Label actionTarget;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;


    @FXML
    void handleCancel(ActionEvent event) {
        SceneHelper.closeWindows(event, 1);
    }

    @FXML
    void handleSubmit(ActionEvent event) {

        Project project = new Project(
                nameField.getText(),
                protocolField.getText(),
                hostField.getText(),
                portField.getText(),
                loginField.getText(),
                passwordField.getText()
        );
        WorkSpaces workSpaces = new WorkSpaces();
        ProjectValidator validator = new ProjectValidator();
        Validator status = workSpaces.addWorkSpace(user.getLogin(), category, project);
        if (status.isError()) {
            actionTarget.setTextFill(validator.getPaint(status.isError()));
            actionTarget.setText(status.getMessage());

        }
        else {

            Stage stage = (Stage) ((Stage) ((Node) event.getSource()).getScene().getWindow()).getOwner();

            MainController controller = SceneHelper.changeSceneParams(stage, user.getUser().getName(), user.getLogin(), user.getUser().getPassword());
            controller.setActiveTab(activeTab);
            SceneHelper.closeWindows(event, 1);
            CategoryValidator categoryValidator = new CategoryValidator();
            Label successAddCategoryLabel = controller.getSuccessAddCategoryLabel();
            successAddCategoryLabel.setTextFill(categoryValidator.getPaint(Validator.SUCCESS_ADD.isError()));
            successAddCategoryLabel.setText(Validator.SUCCESS_ADD.getMessage());
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> successAddCategoryLabel.setText(""));
            pause.play();
        }
    }

    @FXML
    void handleType(KeyEvent event) {
        LoginValidator validator = new LoginValidator();
        validator.validateEmptyFields(submit, nameField);
    }


    @FXML
    void initialize() {
        typeSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "HTTP":
                    protocolField.setText("http://");
                    protocolField.setDisable(true);
                    portField.setText("80");
                    portField.setDisable(true);
                    break;
                case "HTTPS":
                    protocolField.setText("https://");
                    protocolField.setDisable(true);
                    portField.setText("80");
                    portField.setDisable(true);
                    break;
                case "FTP":
                    protocolField.setText("ftp://");
                    protocolField.setDisable(true);
                    portField.setText("21");
                    portField.setDisable(true);
                    break;
                case "SFTP":
                    protocolField.setText("sftp://");
                    protocolField.setDisable(true);
                    portField.setText("22");
                    portField.setDisable(true);
                    break;
                default:
                    protocolField.setText("");
                    protocolField.setDisable(false);
                    portField.setText("");
                    portField.setDisable(false);

            }
        });
    }
}
