package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;
import model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import util.AuthenticationManager;
import util.SessionManager;

public class AddProjectController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML private Button confirmChange; //corresponds to the confirm button
    @FXML private Button back; //Corresponds to the back button to go back to the home page
    @FXML private TextField titleField; //Corresponds to the titleField field
    @FXML private TextField locationField; //Corresponds to the locationField field
    @FXML private TextField dayField; //Corresponds to the dayField field
    @FXML private TextField hourlyValueField; //Corresponds to the hourlyValueField field
    @FXML private TextField totalSlotsField; //Corresponds to the totalSlotsField field
    @FXML private Label status; //Corresponds to the error status label


    public AddProjectController(Stage parentStage, Model model){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        confirmChange.setOnAction(event->{
            List<String> errors = new ArrayList<>();

            try {
                errors = AuthenticationManager.validateProgramAddition(titleField.getText(), locationField.getText(), dayField.getText(),
                        Integer.parseInt(hourlyValueField.getText()), Integer.parseInt(totalSlotsField.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



        });

    }
}
