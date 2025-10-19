package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Model;
import model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import util.AuthenticationManager;
import util.SessionManager;

import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminHomeController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    private Map<String, List<Project>> groupedProjects;

    @FXML private ListView<String> projectTitlesListView;
    @FXML private TableView<Project> projectDetailsTableView;
    @FXML private TableColumn<Project, Void> enableDisable;
    @FXML private TableColumn<Project, Void> modify;

    @FXML private Button logout;
    @FXML private Button allRegistrations;
    @FXML private Button createProject;

    public AdminHomeController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {

        try {
            groupedProjects = model.getProjectsDao().getGroupedProjects();

            ObservableList<String> titles = FXCollections.observableArrayList(groupedProjects.keySet());
            projectTitlesListView.setItems(titles);

            projectTitlesListView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            List<Project> projectsForTitle = groupedProjects.get(newSelection);
                            projectDetailsTableView.setItems(FXCollections.observableArrayList(projectsForTitle));
                        }
                    }
            );


            //Using callback to add a button to the button column, basically a blueprint telling tableview what  to add in the cells in that column
            Callback<TableColumn<Project, Void>, TableCell<Project, Void>> cellFactory = new Callback<>() {

                //Method that iss called by callback each time it needs to make a new cell
                @Override
                public TableCell<Project, Void> call(final TableColumn<Project, Void> param) {
                    final TableCell<Project, Void> cell = new TableCell<>() {

                        //Adding a button to the cell
                        private final Button btn = new Button();

                        {
                            //When the cell button is pressed
                            btn.setOnAction(event -> {
                                // Getting the Project object for the row
                                Project project = getTableView().getItems().get(getIndex());

                                String newStatus = "";
                                if(project.getIsEnabled().equals("true")){
                                    newStatus = "false";
                                } else if(project.getIsEnabled().equals("false")) {
                                    newStatus = "true";
                                }
                                else {
                                    System.out.println("Error");
                                }

                                try {
                                    model.getProjectsDao().enableDisableProject(project.getProjectId(), newStatus);
                                    projectDetailsTableView.getItems().get(getIndex()).setIsEnabled(newStatus);
                                    projectDetailsTableView.refresh();

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            });
                        }

                        // This method is called by callback to update the cell's content
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                // If the row is empty, don't show the button
                                setGraphic(null);
                            } else {
                                // If the row is not empty, show the button
                                Project project = getTableView().getItems().get(getIndex());
                                // Dynamically set the button text based on the project's status
                                if (project.getIsEnabled().equals("true")) {
                                    btn.setText("Disable");
                                } else {
                                    btn.setText("Enable");
                                }
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }

            };

            Callback<TableColumn<Project, Void>, TableCell<Project, Void>> modifyCellFactory = new Callback<>() {

                //Method that iss called by callback each time it needs to make a new cell
                @Override
                public TableCell<Project, Void> call(final TableColumn<Project, Void> param) {
                    final TableCell<Project, Void> cell = new TableCell<>() {

                        //Adding a button to the cell
                        private final Button btn = new Button("Modify");

                        {
                            //When the cell button is pressed
                            btn.setOnAction(event -> {
                                try {
                                    Project project = getTableView().getItems().get(getIndex());

                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProjectView.fxml"));
                                    ModifyProjectController modifyProjectController = new ModifyProjectController(stage, model);

                                    modifyProjectController.setProject(project);

                                    loader.setController(modifyProjectController);
                                    VBox root = loader.load();

                                    modifyProjectController.showStage(root);

                                    stage.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            });
                        }

                        // This method is called by callback to update the cell's content
                        @Override
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                // If the row is empty, don't show the button
                                setGraphic(null);
                            } else {
                                // If the row is not empty, show the button
                                setGraphic(btn);
                            }
                        }
                    };
                    return cell;
                }

            };

            enableDisable.setCellFactory(cellFactory);
            modify.setCellFactory(modifyCellFactory);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        allRegistrations.setOnAction(event->{
            try {
                //Switch to the view AllRegistrations page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllRegistrationsView.fxml"));

                // Customize controller instance
                AllRegistrationsController allRegistrationsController =  new AllRegistrationsController(stage, model);

                loader.setController(allRegistrationsController);
                VBox root = loader.load();

                allRegistrationsController.showStage(root);

                stage.close();
            } catch (IOException e) {
                System.out.println("View History Error");
            }

        });

        createProject.setOnAction(event->{
            try {
                //Switch to the view Create Project page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProjectView.fxml"));

                // Customize controller instance
                AddProjectController addProjectController =  new AddProjectController(stage, model);

                loader.setController(addProjectController);
                VBox root = loader.load();

                addProjectController.showStage(root);

                stage.close();
            } catch (IOException e) {
                System.out.println("View History Error");
            }
        });

        logout.setOnAction(event ->{
            SessionManager.getInstance().clearSession();
            stage.close();
            parentStage.show();
        });

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 780, 400);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Home");
        stage.show();
    }

}
