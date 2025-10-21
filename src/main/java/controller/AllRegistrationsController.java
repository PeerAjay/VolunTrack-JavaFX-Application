package controller;

import dao.RegistrationsDAO;
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
import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import util.AuthenticationManager;
import util.SessionManager;

public class AllRegistrationsController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML private Button backToHome; //This corresponds to the back to home button

    @FXML private TableView<RegistrationView> registrationHistory; //This corresponds to the tableview to show the history of registrations

    public AllRegistrationsController(Stage parentStage, Model model){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {
        //Load all the registrations from all users into an observable list
        ObservableList<RegistrationView> registrations = loadRegistrationsFromDB();

        //Set the items of the tableView to the observable list
        registrationHistory.setItems(registrations);

        //When the "back to home" button is pressed
        backToHome.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

    }

    //Method to load all the registrations from all the users into a list
    public ObservableList<RegistrationView> loadRegistrationsFromDB() throws SQLException{
        ObservableList<RegistrationView> registrations = FXCollections.observableArrayList();

        registrations = model.getRegistrationsDoa().getRegistrationHistory();

        return registrations;
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 925, 300);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("All Registrations");
        stage.show();
    }
}
