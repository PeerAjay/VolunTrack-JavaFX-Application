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

public class passwordChangeController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML private Button confirmChange; //corresponds to the confirm button
    @FXML private Button back; //Corresponds to the back button to go back to the home page
    @FXML private TextField oldPassword; //Corresponds to the old password field
    @FXML private TextField newPassword; //Corresponds to the new password field
    @FXML private Label status; //Corresponds to the error status label

    public passwordChangeController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        //Get the current user for the session from the sessionManager
        User user = SessionManager.getInstance().getCurrentUser();

        //When the confirm button is pressed
        confirmChange.setOnAction(event ->{
            //create a list to store all the errors
            List<String> errors = new ArrayList<>();
            //If there are any errors, add them to the list using authenthication manager
            errors = AuthenticationManager.getPasswordErrors(oldPassword.getText(), newPassword.getText());

            //If the list of errors is empty the input is valid so change the password
            if(errors.isEmpty()){
                try {
                    //Using the userdao to change the password in the database
                    model.getUserDao().changePassword(newPassword.getText(), user.getUsername());
                    //changing the error message to tell the user the password change was successful
                    String errorMessage = "Password Changed!";
                    status.setText(errorMessage);
                    status.setTextFill(Color.GREEN);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                //If there are errors in the list, display them on the error label
                String errorMessage = String.join("\n", errors);
                status.setText(errorMessage);
                status.setTextFill(Color.RED);
            }

        });

        //go back to the home page when the back button is pressed
        back.setOnAction(event ->{
            stage.close();
            parentStage.show();
        });

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Change Password");
        stage.show();
    }

}
