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
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.ArrayList;

public class SignupController {
    @FXML
    private TextField fullName;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
    @FXML
    private TextField email;
	@FXML
	private Button createUser;
	@FXML
	private Button close;
	@FXML
	private Label status;
	
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
		createUser.setOnAction(event -> {
            List<String> errors = AuthenticationManager.getPasswordErrors(password.getText());

            errors.addAll(AuthenticationManager.existsCheck(fullName.getText(), email.getText(), username.getText()));

			if (errors.isEmpty()) {
				User user;
				try {
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

		close.setOnAction(event -> {
			stage.close();
			parentStage.show();
		});
	}
	
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Sign up");
		stage.show();
	}
}
