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
import javafx.util.Callback;
import model.Model;
import model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
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
    @FXML private Button viewCart; //Corresponds to the "viewCart" button in HomeView.fxml

    //These link the fields from the project object to the columns in the table view
    @FXML private TableView<Project> projectTableView;
    @FXML private TableColumn<Project, String> titleColumn;
    @FXML private TableColumn<Project, String> locationColumn;
    @FXML private TableColumn<Project, String> dayColumn;
    @FXML private TableColumn<Project, String> hourlyValueColumn;
    @FXML private TableColumn<Project, String> regSlotsColumn;
    @FXML private TableColumn<Project, String> totalSlotsColumn;
    @FXML private TableColumn<Project, Void> addToCart;


    public HomeController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = model;
	}

    @FXML
    public void initialize() throws SQLException {
        //Loading the csv data using the loadCSVData helper function
        ObservableList<Project> projects = loadProjectsfromDB();
        projectTableView.setItems(projects);
        User user = SessionManager.getInstance().getCurrentUser();

        //Making logout menu a label so it can act as a button
        Label logoutLabel = new Label("logout");

        //Using callback to add a button to the button column, basically a blueprint telling tableview what  to add in the cells in that column
        Callback<TableColumn<Project, Void>, TableCell<Project, Void>> cellFactory = new Callback<>() {

            //Method that iss called by callback each time it needs to make a new cell
            @Override
            public TableCell<Project, Void> call(final TableColumn<Project, Void> param) {
                final TableCell<Project, Void> cell = new TableCell<>() {

                    //Adding a button to the cell
                    private final Button btn = new Button("Add to Cart");

                    {
                        //When the cell button is pressed
                        btn.setOnAction(event -> {
                            // Getting the Project object for the row
                            Project project = getTableView().getItems().get(getIndex());

                            try {
                                //load the sign-up page
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addProjectPopup.fxml"));

                                // Customize controller instance
                                addProjectPopupController addProjectPopupController =  new addProjectPopupController(stage, model);

                                loader.setController(addProjectPopupController);
                                VBox root = loader.load();

                                //Call setters in projectpopup controller to set the project and user of the cartItem object
                                addProjectPopupController.setUserProjectValue(user.getUsername(), project);

                                addProjectPopupController.showStage(root);

                            } catch (IOException e) {
                                //message.setText(e.getMessage());
                                System.out.println("AHHHHHHHHHHHHH");
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

        addToCart.setCellFactory(cellFactory);

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
            SessionManager.getInstance().clearSession();
            stage.close();
            parentStage.show();

        });

        //When the viewCart button is pressed go to the cart menu
        viewCart.setOnAction(Event -> {
            try {
                //Switch to the view Cart page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml"));

                // Customize controller instance
                CartController cartController =  new CartController(stage, model);

                loader.setController(cartController);
                VBox root = loader.load();

                cartController.showStage(root);

                stage.close();
            } catch (IOException e) {
                System.out.println("Viewcart Error");
            }
        });

    }

    //Loading the projects from the DB table
    private ObservableList<Project> loadProjectsfromDB() throws SQLException {
        ObservableList<Project> projects = FXCollections.observableArrayList();

        projects = model.getProjectsDao().loadProjects();

        return projects;
    }

    //Set username function sets the welcome label to the username
    public void setUsername(String username) {
        welcomeLabel.setText("Welcome, " + username);
    }

    //showing stage
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 700, 400);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Home");
		stage.show();
	}
}
