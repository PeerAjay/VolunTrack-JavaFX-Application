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

    private Map<String, List<Project>> groupedProjects; //A map variable to map the titles of the project to a list of grouped projects

    @FXML private ListView<String> projectTitlesListView; //This corresponds to the listView on the left of the borderbane
    @FXML private TableView<Project> projectDetailsTableView; //This corresponds to the tableView showing the actual projects in the middle section of the borderbane
    @FXML private TableColumn<Project, Void> enableDisable; //This corresponds to the column for the enable/disable buttons for each project
    @FXML private TableColumn<Project, Void> modify; //This corresponds to the modify column for the modify buttons

    @FXML private Button logout; //This corressponds to the logout button
    @FXML private Button allRegistrations; //This corressponds to the " all registrations " button
    @FXML private Button createProject; //This corressponds to the "create project" button
    @FXML private Button refresh; //This corressponds to the "refresh" button

    public AdminHomeController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {

        try {
            //Gets the mapped list for the projects from the database with the name string as a key and the projects as values
            groupedProjects = model.getProjectsDao().getGroupedProjects();

            //Get just the titles and put them in an observable list to be displayed
            ObservableList<String> titles = FXCollections.observableArrayList(groupedProjects.keySet());
            projectTitlesListView.setItems(titles);

            //Add a listener to the list view on the left to check if the user selects a title
            projectTitlesListView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldSelection, newSelection) -> {
                        //When a title is selected then get a list of the projects for the title that was selected and set the tableviews items to that
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

                                //Checks to see if the selected project is enabled already or disabled
                                String newStatus = "";
                                if(project.getIsEnabled().equals("true")){
                                    //If the project is enabled set the new status to disabled
                                    newStatus = "false";
                                } else if(project.getIsEnabled().equals("false")) {
                                    //If the project is disabled set the new status to enabled
                                    newStatus = "true";
                                }
                                else {
                                    System.out.println("Error");
                                }

                                try {
                                    //Set the new status in the database and refresh
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

            //Using callback to add a button to the button column, basically a blueprint telling tableview what  to add in the cells in that column
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
                                    //Get the project for the button that was pressed
                                    Project project = getTableView().getItems().get(getIndex());

                                    //Loading the modify project view
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

            //Setting the columns cell factory to show the buttons
            enableDisable.setCellFactory(cellFactory);
            modify.setCellFactory(modifyCellFactory);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //When the "view all registrations" button is pressed
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

        //When the "create a project" button is pressed
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

        //Logout button is pressed
        logout.setOnAction(event ->{
            SessionManager.getInstance().clearSession();
            stage.close();
            parentStage.show();
        });

        //When the refresh button is pressed
        refresh.setOnAction(event->{
            stage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminHomeView.fxml"));

                AdminHomeController newAdminHomeController = new AdminHomeController(parentStage, model);
                loader.setController(newAdminHomeController);

                Pane root = loader.load();
                newAdminHomeController.showStage(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 1000, 400);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Home");
        stage.show();
    }

}
