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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 780, 400);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Home");
        stage.show();
    }

}
