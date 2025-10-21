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


public class HomeController {
	private Model model;
	private Stage stage;
	private Stage parentStage;
	@FXML private MenuItem viewProfile; // Corresponds to the Menu item "viewProfile" in HomeView.fxml
	@FXML private MenuItem changePassword;
    @FXML private Button logout;// // Corresponds to the Menu item "logout" in HomeView.fxml
    @FXML private Label welcomeLabel; // // Corresponds to the Menu item "welcomeLabel" in HomeView.fxml
    @FXML private Label status; // // Corresponds to the Menu item "status" in HomeView.fxml
    @FXML private Button viewCart; //Corresponds to the "viewCart" button in HomeView.fxml
    @FXML private Button viewHistory; //Corresponds to the "view history" button in the home view
    @FXML private Button refresh; // Corresponds to the refresh button

    @FXML private TableView<Project> projectTableView; //This corresponds to the tableView to show the projects
//    @FXML private TableColumn<Project, String> titleColumn;
//    @FXML private TableColumn<Project, String> locationColumn;
//    @FXML private TableColumn<Project, String> dayColumn;
//    @FXML private TableColumn<Project, String> hourlyValueColumn;
//    @FXML private TableColumn<Project, String> regSlotsColumn;
//    @FXML private TableColumn<Project, String> totalSlotsColumn;
    @FXML private TableColumn<Project, Void> addToCart; //This corresponds to the add to cart column for the add to cart buttons


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

        //Setting the current user
        User user = SessionManager.getInstance().getCurrentUser();

        //Setting the current users username to the welcome text
        welcomeLabel.setText("Welcome, " + user.getUsername());


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

                            //Get the current day of the week
                            int todayDay = LocalDate.now().getDayOfWeek().getValue();
                            int projectDay = getDayValue(project.getDay());

                            //If the add to cart button is pressed on a day that is after the project day is give an error
                            if (projectDay < todayDay){
                                status.setText("Project has passed");
                                status.setTextFill(Color.RED);
                                return;
                            } else if(Integer.parseInt(project.getTotalSlots()) == 0) {
                                status.setText("No more slots remaining");
                                status.setTextFill(Color.RED);
                                return;
                            }

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

        //Setting the cell factory of the column to display the buttons
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

        //When the view history button is pressed
        viewHistory.setOnAction(event->{
            try {
                //Switch to the view History page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegistrationHistoryView.fxml"));

                // Customize controller instance
                RegistrationHistoryController registrationHistoryController =  new RegistrationHistoryController(stage, model);

                loader.setController(registrationHistoryController);
                VBox root = loader.load();

                registrationHistoryController.showStage(root);

                stage.close();
            } catch (IOException e) {
                System.out.println("View History Error");
                e.printStackTrace();
            }
        });

        //When the refresh button is pressed
        refresh.setOnAction(event->{
            stage.close();

            //Load everything again
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));

                HomeController newHomeController = new HomeController(parentStage, model);
                loader.setController(newHomeController);

                Pane root = loader.load();
                newHomeController.showStage(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    //Loading the projects from the DB table
    private ObservableList<Project> loadProjectsfromDB() throws SQLException {
        ObservableList<Project> projects = FXCollections.observableArrayList();

        projects = model.getProjectsDao().loadProjects();

        return projects;
    }


    //Convert the string value of the day into an int so it's easy to compare
    private int getDayValue(String day) {
        switch (day.toLowerCase()) {
            case "mon": return 1;
            case "tue": return 2;
            case "wed": return 3;
            case "thu": return 4;
            case "fri": return 5;
            case "sat": return 6;
            case "sun": return 7;
            default: return 0; // Should not happen with valid data
        }
    }

    //showing stage
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 700, 400);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setTitle("Home");
		stage.show();
	}
}
