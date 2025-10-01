package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Model;
import model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;


public class HomeController {
	private Model model;
	private Stage stage;
	private Stage parentStage;
	@FXML private MenuItem viewProfile; // Corresponds to the Menu item "viewProfile" in HomeView.fxml
	@FXML private MenuItem updateProfile; // // Corresponds to the Menu item "updateProfile" in HomeView.fxml
    @FXML private Label welcomeLabel; // // Corresponds to the Menu item "welcomeLabel" in HomeView.fxml

    //These link the fields from the project object to the columns in the table view
    @FXML private TableView<Project> projectTableView;
    @FXML private TableColumn<Project, String> titleColumn;
    @FXML private TableColumn<Project, String> locationColumn;
    @FXML private TableColumn<Project, String> dayColumn;
    @FXML private TableColumn<Project, String> hourlyValueColumn;
    @FXML private TableColumn<Project, String> regSlotsColumn;
    @FXML private TableColumn<Project, String> totalSlotsColumn;


    public HomeController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = model;
	}

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));

        ObservableList<Project> projects = loadCSVData();
        projectTableView.setItems(projects);
    }

    //Function to load each row from the csv into Project objects
    private ObservableList<Project> loadCSVData() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String csvFile = "src/projects.csv";
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skipping header
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(cvsSplitBy);
                Project project = new Project(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
                projects.add(project);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projects;
    }

    //Set username function
    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username);
    }

	public void showStage(Pane root) {
		Scene scene = new Scene(root, 600, 300);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Home");
		stage.show();
	}
}
