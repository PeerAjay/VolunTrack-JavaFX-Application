package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;
import model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import util.AuthenticationManager;
import util.SessionManager;


public class HomeController {
	private Model model;
	private Stage stage;
	private Stage parentStage;
	@FXML private MenuItem viewProfile; // Corresponds to the Menu item "viewProfile" in HomeView.fxml
	@FXML private MenuItem changePassword;
    @FXML private Button logout;// // Corresponds to the Menu item "logout" in HomeView.fxml
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
        //Loading the csv data using the loadCSVData helper function
        ObservableList<Project> projects = loadCSVData();
        projectTableView.setItems(projects);

        //Making logout menu a label so it can act as a button
        Label logoutLabel = new Label("logout");

        //On 'change password' button press
        changePassword.setOnAction(event ->{
            try {
                //Switch to the password change page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/passwordChange.fxml"));

                // Customize controller instance
                passwordChangeController passwordChangeController =  new passwordChangeController(stage, model);

                loader.setController(passwordChangeController);
                VBox root = loader.load();

                passwordChangeController.showStage(root);

                stage.close();
            } catch (IOException e) {
                System.out.println("password change error");
            }
        });

        logout.setOnAction(event ->{
            System.out.println("Logout action");
            SessionManager.getInstance().clearSession();
            stage.close();
            parentStage.show();

        });

    }

    //Function to load each row from the csv into Project objects
    private ObservableList<Project> loadCSVData() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        String csvFile = "src/projects.csv"; //setting the file to the projects csv
        String line;
        String cvsSplitBy = ",";

        //reading the csv file
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

    //showing stage
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 600, 300);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Home");
		stage.show();
	}
}
