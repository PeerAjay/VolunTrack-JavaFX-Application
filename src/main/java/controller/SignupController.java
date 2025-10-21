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

public class SignupController {
    @FXML
    private TextField fullName; //Corresponds to the name field
	@FXML
	private TextField username; //Corresponds to the username field
	@FXML
	private TextField password; //Corresponds to password field
    @FXML
    private TextField email; //Corresponds to email field
	@FXML
	private Button createUser;//Corresponds to the create user button
	@FXML
	private Button close; //Corresponds to the close button
	@FXML
	private Label status; //Corresponds to the error status label
	
	private Stage stage;
	private Stage parentStage;
	private Model model;
	
	public SignupController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = model;
	}

	@FXML
	public void initialize() {
        //When the create user button is pressed
		createUser.setOnAction(event -> {
            //get the list of password errors if any using authentication manager
            List<String> errors = AuthenticationManager.getPasswordErrors(password.getText());

            //Adding some more errors if any
            errors.addAll(AuthenticationManager.existsCheck(fullName.getText(), email.getText(), username.getText(), model.getUserDao()));

            //If the list is empty there are no errors so create the user
			if (errors.isEmpty()) {
				User user;
				try {
                    //using userDAO to add the new user to the database
					user = model.getUserDao().createUser(email.getText(), fullName.getText(), username.getText(), password.getText());
					if (user != null) {
						status.setText("Created " + user.getUsername());
						status.setTextFill(Color.GREEN);
					} else {
						status.setText("Cannot create user");
						status.setTextFill(Color.RED);
					}
				} catch (SQLException e) {
					status.setText(e.getMessage());
					status.setTextFill(Color.RED);
				}
				
			} else {
                String errorMessage = String.join("\n", errors);
                status.setText(errorMessage);
				status.setTextFill(Color.RED);
			}
		});

        //When the close button is pressed go back to the login page
		close.setOnAction(event -> {
			stage.close();
			parentStage.show();
		});
	}
	
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setTitle("Sign up");
		stage.show();
	}
}
