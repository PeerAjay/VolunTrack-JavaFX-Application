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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import util.AuthenticationManager;
import util.SessionManager;

public class RegistrationHistoryController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML private Button backToHome; //This corresponds to the back to home button

    @FXML private TableView<RegistrationView> registrationHistory;

    public RegistrationHistoryController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {
        ObservableList<RegistrationView> registrations = loadRegistrationsFromDB();

        registrationHistory.setItems(registrations);

        backToHome.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

    }

    public ObservableList<RegistrationView> loadRegistrationsFromDB() throws SQLException{
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        ObservableList<RegistrationView> registrations = FXCollections.observableArrayList();

        registrations = model.getRegistrationsDoa().getRegistrationHistory(username);

        return registrations;
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 720, 300);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Cart");
        stage.show();
    }

}

