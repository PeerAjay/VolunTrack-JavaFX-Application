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

public class RegistrationHistoryController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML private Button backToHome; //This corresponds to the back to home button
    @FXML private Button export; //This corresponds to the export button
    @FXML private Label status; //This corresponds to the status label

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

        export.setOnAction(event->{
            if(registrationHistory.getItems().isEmpty()){
                status.setText("No Registrations");
                status.setTextFill(Color.RED);
            }
            else {
                export();
            }
        });

    }

    public ObservableList<RegistrationView> loadRegistrationsFromDB() throws SQLException{
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        ObservableList<RegistrationView> registrations = FXCollections.observableArrayList();

        registrations = model.getRegistrationsDoa().getRegistrationHistory(username);

        return registrations;
    }

    //export helper method formats and write all the registrations for the user to a file using filewriter
    public void export(){
        String username = SessionManager.getInstance().getCurrentUser().getUsername();
        String filename = "history_" + username + ".txt"; //Setting the name of the file

        try {
            List<RegistrationView> history = registrationHistory.getItems(); //getting a list of the registrations for the filewriter

            //Create a new filewriter and print out each field in a formatted way
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println(username + "'s Participation History");
                writer.println("==============================================");

                for (RegistrationView entry : history) {
                    writer.println("Registration ID: " + entry.getFormattedRegistrationID());
                    writer.println("Date and Time: " + entry.getFormattedTimestamp());
                    writer.println("Project Title: " + entry.getTitle());
                    writer.println("Location: " + entry.getLocation());
                    writer.println("Day: " + entry.getDay());
                    writer.println("Slots Registered: " + entry.getSlotsRegistered());
                    writer.println("Hours Per Slot: " + entry.getHoursPerSlot());
                    writer.println("Total Contribution Value: " + entry.getTotalContribution());
                    writer.println("----------------------------------------------");
                }
            }

            status.setText("Successfully Exported!");
            status.setTextFill(Color.GREEN);

        } catch (IOException e) {
            e.printStackTrace();
            status.setText("Export error");
            status.setTextFill(Color.RED);
        }

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 825, 300);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Registration History");
        stage.show();
    }

}

