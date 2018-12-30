package com.stendyx.passkeeper.passkeeper.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;
import com.stendyx.passkeeper.passkeeper.helpers.SceneHelper;
import com.stendyx.passkeeper.passkeeper.models.User;
import com.stendyx.passkeeper.passkeeper.objects.workspace.Category;
import com.stendyx.passkeeper.passkeeper.objects.workspace.Project;
import com.stendyx.passkeeper.passkeeper.objects.workspace.WorkSpaces;
import com.stendyx.passkeeper.passkeeper.properties.Images;
import com.stendyx.passkeeper.passkeeper.properties.Pages;
import com.stendyx.passkeeper.passkeeper.properties.Validator;
import com.stendyx.passkeeper.passkeeper.validators.CategoryValidator;
import com.stendyx.passkeeper.passkeeper.validators.InterfaceValidator;
import com.stendyx.passkeeper.passkeeper.validators.LoginValidator;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MainController implements SceneHelper {

    public User user = new User();

    public HashMap<Category, ArrayList<Project>> categories = new HashMap<>();


    @FXML
    private Label name;

    @FXML
    private TextField categoryTextField;

    @FXML
    private Button addCategoryButton;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label successAddCategoryLabel;


    public void setActiveTab(int tab) {

        tabPane.getSelectionModel().select(tab);
    }

    public Label getSuccessAddCategoryLabel() {
        return successAddCategoryLabel;
    }


    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    public void handleReenter(ActionEvent event) {
        SceneHelper.changeScene(event, Pages.LOGIN.getPage());
    }

    public void setTextToName(String userName) {
        name.setText(userName);
    }

    @FXML
    public void handleSettings(ActionEvent event) {
        Optional<FXMLLoader> loader = SceneHelper.createPopup(event, Pages.USERSETTINGS.getPage());
        if (loader.isPresent()) {
            UserSettingsController controller = loader.get().getController();
            controller.setTextToUserName(user.getUser().getName());
            controller.user.setLogin(user.getLogin());
            controller.user.setUser(user.getUser());
        }
    }

    @FXML
    public void handlerOnTypedCategory(KeyEvent event) {
        LoginValidator validator = new LoginValidator();
        validator.validateEmptyFields(addCategoryButton, categoryTextField);
    }

    public GridPane generateTab(Category category, List<Project> project, Tab tab) {

        GridPane root = new GridPane();

        root.setPadding(new Insets(20));
        root.setHgap(5);
        root.setVgap(5);
        root.setMinWidth(700);

        Label labeltab = new Label(tab.getText());


        final ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        contextMenu.getItems().add(edit);
        labeltab.setContextMenu(contextMenu);
        tab.setText("");
        tab.setGraphic(labeltab);

        edit.setOnAction(event -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            HBox dialogVbox = new HBox(10);
            dialogVbox.setPadding(new Insets(5));
            Scene dialogScene = new Scene(dialogVbox);

            // Update category part
            Button updateCategoryButton = new Button();
            updateCategoryButton.setDefaultButton(true);
            ImageView imageView = Images.SUCC.getImageView(20, 20);

            updateCategoryButton.setGraphic(imageView);
            TextField categoryUpdateField = new TextField();
            categoryUpdateField.setText(labeltab.getText());
            categoryUpdateField.setOnKeyTyped(event3 -> {
                LoginValidator validator = new LoginValidator();
                validator.validateEmptyFields(updateCategoryButton, categoryUpdateField);
            });

            updateCategoryButton.setOnAction(event2 -> {
                SceneHelper.closeWindows(event2, 1);

                Validator validator = executeAction("update", categoryUpdateField.getText(), category);

                if (!validator.isError())
                    labeltab.setText(categoryUpdateField.getText());
            });


            // Delete category part
            Button deleteCategoryButton = new Button();
            ImageView imageView2 = Images.NSUCC.getImageView(20, 20);
            deleteCategoryButton.setGraphic(imageView2);
            deleteCategoryButton.setOnAction(event2 -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you really want to delete that category? ",
                        ButtonType.YES, ButtonType.NO);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    SceneHelper.closeWindows(event2, 1);
                    executeAction("delete", "", category);
                    tab.getTabPane().getTabs().remove(tab);
                }
            });
            dialogVbox.getChildren().addAll(categoryUpdateField, updateCategoryButton, deleteCategoryButton);
            deleteCategoryButton.setCancelButton(true);
            dialog.setScene(dialogScene);
            dialog.show();
        });

        WorkSpaces workSpaces = new WorkSpaces();
        ArrayList<Project> projects = workSpaces.getProjects(user.getLogin(), category);

        TextField searchField = new TextField();

        Button searchButton = new Button();


        ImageView imageView = Images.SEARCH.getImageView(20, 20);
        searchButton.setGraphic(imageView);

        searchButton.setTooltip(new Tooltip("Search in Names/Hosts"));

        Button resetButton = new Button("Reset to default");

        // Add project part
        Button addProjectButton = new Button();
        addProjectButton.setDefaultButton(true);
        addProjectButton.setText("Add project");
        addProjectButton.setOnAction(event -> {
            Optional<FXMLLoader> popup = SceneHelper.createPopup(event, Pages.ADDPROJECT.getPage());
            if (popup.isPresent()) {
                AddProjectController controller = popup.get().getController();
                controller.user = user;
                controller.category = category;
                controller.activeTab = tabPane.getSelectionModel().getSelectedIndex();
            }
        });

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        GridPane.setHalignment(addProjectButton, HPos.RIGHT);
        TableView<Project> tableView = new TableView<>();
        ObservableList<Project> projectsData = FXCollections.observableArrayList();
        tableView.setEditable(true);
        tableView.setMinWidth(650);

//        String projectName;
//        String protocol;
//        String host;
//        String port;
//        String userName;
//        String userPassword;


        searchButton.setOnAction(event -> {
            String searchString = searchField.getText();
            ArrayList<Project> collect = projectsData.stream().filter(project1 ->
                    project1.getProjectName().toLowerCase().contains(searchString.toLowerCase()) || project1.getHost().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toCollection(ArrayList::new));

            projectsData.removeAll(projectsData);
            if (searchString.trim().isEmpty()) {
                projectsData.addAll(workSpaces.getProjects(user.getLogin(), category));
            } else {
                projectsData.addAll(collect);
            }
        });
        resetButton.setOnAction(event -> {
            projectsData.removeAll(projectsData);
            projectsData.addAll(workSpaces.getProjects(user.getLogin(), category));

        });


        tableView.setRowFactory(new Callback<TableView<Project>, TableRow<Project>>() {
            @Override
            public TableRow<Project> call(TableView<Project> tableView) {
                final TableRow<Project> row = new TableRow<>();
                final ContextMenu rowMenu = new ContextMenu();

                MenuItem copyUrl = new MenuItem("Copy URL");
                copyUrl.setOnAction(event -> {
                    Project project1 = tableView.getSelectionModel().getSelectedItem();
                    StringSelection stringSelection = new StringSelection(project1.getProtocol() + project1.getHost());
                    copyToClipboard(stringSelection);
                });
                MenuItem copyLogin = new MenuItem("Copy Login");
                copyLogin.setOnAction(event -> {
                    Project project1 = tableView.getSelectionModel().getSelectedItem();
                    StringSelection stringSelection = new StringSelection(project1.getUserName());
                    copyToClipboard(stringSelection);
                });
                MenuItem copyPassword = new MenuItem("Copy Password");
                copyPassword.setOnAction(event -> {
                    Project project1 = tableView.getSelectionModel().getSelectedItem();
                    StringSelection stringSelection = new StringSelection(project1.getUserPassword());
                    copyToClipboard(stringSelection);
                });
                rowMenu.getItems().addAll(copyUrl, copyLogin, copyPassword);


                // only display context menu for non-null items:
                row.contextMenuProperty().bind(
                        Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                .then(rowMenu)
                                .otherwise((ContextMenu) null));

                return row;
            }

            private void copyToClipboard(StringSelection stringSelection) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                typeLabelStatus(new CategoryValidator(), Validator.SUCCESS_COPIED);
            }
        });

        TableColumn<Project, String> projectName = new TableColumn<>("Name");
        TableColumn<Project, String> protocol = new TableColumn<>("Protocol");
        TableColumn<Project, String> host = new TableColumn<>("Host");
        TableColumn<Project, String> port = new TableColumn<>("Port");
        TableColumn<Project, String> userName = new TableColumn<>("Login");
        TableColumn<Project, String> userPassword = new TableColumn<>("Password");
        TableColumn operations = new TableColumn<>("");


        projectsData.addAll(projects);
        projectName.setCellValueFactory(new PropertyValueFactory<Project, String>("projectName"));
        projectName.setCellFactory(TextFieldTableCell.<Project>forTableColumn());
        projectName.setOnEditCommit((TableColumn.CellEditEvent<Project, String> event) -> {
            TablePosition<Project, String> pos = event.getTablePosition();
            String newProjectName = event.getNewValue();
            int row = pos.getRow();
            Project oldProject = event.getTableView().getItems().get(row);
            Validator validator = workSpaces.updateWorkSpace(user.getLogin(), category, oldProject, newProjectName);
            if (validator.isError()) {
                event.getTableView().getItems().set(row, oldProject);
                typeLabelStatus(new CategoryValidator(), validator);
            } else {
                typeLabelStatus(new CategoryValidator(), validator);
            }

        });


        protocol.setCellValueFactory(new PropertyValueFactory<Project, String>("protocol"));
        protocol.setCellFactory(TextFieldTableCell.<Project>forTableColumn());
        protocol.setOnEditCommit((TableColumn.CellEditEvent<Project, String> event) -> {
            updateCell("protocol", workSpaces, category, event);
        });
        host.setCellValueFactory(new PropertyValueFactory<Project, String>("host"));
        host.setCellFactory(TextFieldTableCell.<Project>forTableColumn());
        host.setOnEditCommit((TableColumn.CellEditEvent<Project, String> event) -> {
            updateCell("host", workSpaces, category, event);
        });
        port.setCellValueFactory(new PropertyValueFactory<Project, String>("port"));
        port.setCellFactory(TextFieldTableCell.<Project>forTableColumn());
        port.setOnEditCommit((TableColumn.CellEditEvent<Project, String> event) -> {
            updateCell("port", workSpaces, category, event);
        });
        userName.setCellValueFactory(new PropertyValueFactory<Project, String>("userName"));
        userName.setCellFactory(TextFieldTableCell.<Project>forTableColumn());
        userName.setOnEditCommit((TableColumn.CellEditEvent<Project, String> event) -> {
            updateCell("login", workSpaces, category, event);
        });
        userPassword.setCellValueFactory(new PropertyValueFactory<Project, String>("userPassword"));
        userPassword.setCellFactory(cell -> new PasswordLabelCell());
        userPassword.setOnEditCommit((TableColumn.CellEditEvent<Project, String> event) -> {
            updateCell("pass", workSpaces, category, event);
        });

        operations.setCellFactory(
                new Callback<TableColumn, TableCell>() {
                    @Override
                    public TableCell call(TableColumn param) {
                        return new TableCell() {
                            @Override
                            protected void updateItem(Object item, boolean empty) {
                                Button deleteButton = new Button();
                                ImageView imageView1 = Images.NSUCC.getImageView(14, 14);
                                deleteButton.setGraphic(imageView1);
                                deleteButton.setOnAction(event -> {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                            "Are you really want to delete that project? ",
                                            ButtonType.YES, ButtonType.NO);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.YES) {
                                        Project currentProject = (Project) this.getTableView().getItems().get(this.getIndex());
                                        Validator validator = workSpaces.deleteWorkSpace(user.getLogin(), category, currentProject);
                                        typeLabelStatus(new CategoryValidator(), validator);

                                        this.getTableView().getItems().remove(this.getIndex());
                                    }
                                });
                                super.updateItem(item, empty);
                                if (!empty)
                                    setGraphic(deleteButton);
                                else
                                    setGraphic(null);

                            }
                        };
                    }
                });

        tableView.getColumns().addAll(projectName, protocol, host, port, userName, userPassword, operations);

        tableView.setItems(projectsData);

        root.add(searchField, 0, 0);
        root.add(searchButton, 1, 0);
        root.add(resetButton, 2, 0);
        root.add(separator, 3, 0);
        root.add(addProjectButton, 4, 0);
        root.add(tableView, 0, 1, 5, 1);

        return root;
    }

    /**
     * @param type       protocol| port| host | login | pass
     * @param workSpaces Workspaces object
     * @param category   Category object
     * @param event      event
     */
    private void updateCell(String type, WorkSpaces workSpaces, Category
            category, TableColumn.CellEditEvent<Project, String> event) {
        TablePosition<Project, String> pos = event.getTablePosition();
        String newName = event.getNewValue();
        int row = pos.getRow();
        Project oldProject = event.getTableView().getItems().get(row);

        if (newName.trim().isEmpty()) {
            event.getTableView().getItems().set(row, oldProject);
            typeLabelStatus(new CategoryValidator(), Validator.ERROR_NO_DATA);
        }
        switch (type) {
            case "protocol":
                oldProject.setProtocol(newName);
                break;
            case "port":
                oldProject.setPort(newName);
                break;
            case "host":
                oldProject.setHost(newName);
                break;
            case "login":
                oldProject.setUserName(newName);
                break;
            case "pass":
                oldProject.setUserPassword(newName);
                break;
        }

        Validator validator = workSpaces.updateWorkSpace(user.getLogin(), category, oldProject);
        typeLabelStatus(new CategoryValidator(), validator);
    }

    public void generateTabs() {
        ArrayList<Tab> tabs = new ArrayList<>();
        categories.forEach((category, project) -> {
                    Tab tab = new Tab(category.getCategory());
                    tab.setContent(generateTab(category, project, tab));
                    tabs.add(tab);
                }
        );
        tabPane.getTabs().addAll(tabs);
    }

    @FXML
    public void handlerAddCategory(ActionEvent event) {
        Validator validator = executeAction("add", categoryTextField.getText(), new Category("add"));

        if (!validator.isError()) {
            Tab tab = new Tab(categoryTextField.getText());
            tab.setContent(generateTab(new Category(categoryTextField.getText()), new ArrayList<>(), tab));
            tabPane.getTabs().add(tab);
        }
    }

    /**
     * @param action       add/delete/update
     * @param categoryText text to category
     * @param category     category
     * @return Validator
     */
    private Validator executeAction(String action, String categoryText, Category category) {
        CategoryValidator categoryValidator = new CategoryValidator();
        WorkSpaces workSpaces = new WorkSpaces();
        Validator validator;
        switch (action) {
            case "add":
                validator = workSpaces.addWorkSpace(user.getLogin(), new Category(categoryText));
                break;
            case "update":
                validator = workSpaces.updateWorkSpace(user.getLogin(), new Category(categoryText), category);
                break;
            case "delete":
                validator = workSpaces.deleteWorkSpace(user.getLogin(), category);
                break;
            default:
                validator = null;
        }

        typeLabelStatus(categoryValidator, validator);

        return validator;
    }

    private void typeLabelStatus(InterfaceValidator iValidator, Validator validator) {
        successAddCategoryLabel.setTextFill(iValidator.getPaint(validator.isError()));
        successAddCategoryLabel.setText(validator.getMessage());
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> successAddCategoryLabel.setText(""));
        pause.play();
    }

}


class PasswordLabelCell extends TextFieldTableCell<Project, String> {
    private Label label;

    private TextField textField;


    public PasswordLabelCell() {
        label = new Label();
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setGraphic(null);
    }


    private String genDotString(int len) {
        String dots = "";

        for (int i = 0; i < len; i++) {
            dots += "\u2022";
        }

        return dots;
    }


    @Override
    public void updateSelected(boolean selected) {

        super.updateSelected(selected);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
    }

    @Override
    public void updateItem(String item, boolean empty) {
        setConverter(new DefaultStringConverter() {
        });

        super.updateItem(item, empty);
        if (!empty) {
            label.setText(genDotString(item.length()));
            setGraphic(label);
        } else {
            setGraphic(null);
        }
    }
}