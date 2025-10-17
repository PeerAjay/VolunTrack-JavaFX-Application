package controller;

import java.awt.*;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;
import model.User;
import util.AuthenticationManager;

import java.util.List;
import java.util.ArrayList;

public class CheckoutController {
    @FXML
    private TextField code; //Corresponds to the 4-digit code field
    @FXML
    private Button register;//Corresponds to the register button
    @FXML
    private Button cancel; //Corresponds to the cancel button
    @FXML
    private Label status; //Corresponds to the error status label
    @FXML
    private Label totalContribution; //Corresponds to the total contribution label

    private Stage stage;
    private Stage parentStage;
    private Model model;

    public CheckoutController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        //When the register user button is pressed
        register.setOnAction(event->{
            String conCode = code.getText();

            //If the code isn't a valid 6-digit code give error message and do nothing
            if (conCode == null || !conCode.matches("\\d{6}")) {
                status.setText("Please enter a valid 6-digit code");
            }
            else{
                //TODO MAKE THE CODE FOR THE ADDING OF THE PRODUCTS TO REGISTRATION TABLE
            }

        });

        //When the cancel button is pressed go back to the login page
        cancel.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Checkout");
        stage.show();
    }
}
