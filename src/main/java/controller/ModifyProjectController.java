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
import model.ProjectAdd;
import model.User;
import util.AuthenticationManager;
import util.SessionManager;

public class ModifyProjectController {
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

    private Project modifyProject;


    public ModifyProjectController(Stage parentStage, Model model){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        titleField.setText(modifyProject.getTitle());
        locationField.setText(modifyProject.getLocation());
        dayField.setText(modifyProject.getDay());
        hourlyValueField.setText(Integer.toString(modifyProject.getHourlyValue()));
        totalSlotsField.setText(modifyProject.getTotalSlots());

        confirmChange.setOnAction(event->{
            List<String> errors = new ArrayList<>();

            try {
                errors = AuthenticationManager.validateProgramAddition(titleField.getText(), locationField.getText(), dayField.getText(),
                        hourlyValueField.getText(), totalSlotsField.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if(errors.isEmpty()){
                Project modProject = new Project(modifyProject.getProjectId(), titleField.getText(), locationField.getText(), dayField.getText(),
                        Integer.parseInt(hourlyValueField.getText()), "0", totalSlotsField.getText(), "true");

                try {
                    model.getProjectsDao().updateProject(modProject);
                    status.setText("Successfully Updated!");
                    status.setTextFill(Color.GREEN);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                String errorMessage = String.join("\n", errors);
                status.setText(errorMessage);
                status.setTextFill(Color.RED);
            }

        });

        back.setOnAction(event->{
            stage.close();
            parentStage.show();
        });
    }

    public void setProject(Project project){
        modifyProject = project;
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 780, 700);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Modify Project");
        stage.show();
    }

}
