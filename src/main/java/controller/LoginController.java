package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Model;
import model.User;
import util.AuthenticationManager;
import util.SessionManager;

public class LoginController {
	@FXML
	private TextField name; //Corresponds to the name text field
	@FXML
	private PasswordField password; //Corresponds to the password field
	@FXML
	private Label message; //Corresponds to the error message label
	@FXML
	private Button login; //Corresponds to the login button
	@FXML
	private Button signup; //Corresponds to the sign in button

	private Model model;
	private Stage stage;
	
	public LoginController(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
	}
	
	@FXML
	public void initialize() {

        //When the login button is pressed
		login.setOnAction(event -> {
            boolean valid = false;

            //If the name and password fields are filled in
			if (!name.getText().isEmpty() && !password.getText().isEmpty()) {
				User user;
				try {
                    //Calling the authentication manager to verify the password against the username
                    valid = AuthenticationManager.loginVerify(name.getText(), password.getText());
					if (valid) {
                        //if its valid, create a new user for the session and set it as the current user for the session
                        //  using sessionmanager to set the current user on login
                        user = model.getUserDao().getUser(name.getText());
                        SessionManager.getInstance().setCurrentUser(user);

                        //check if the user is null
                        if (user == null) {
                            System.out.println("USER NULL");
                        }

						model.setCurrentUser(user);

                        //If the role of the user is admin
                        if(user.getRole().equals("admin")){
                            try { //loading the admin home view
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminHomeView.fxml"));
                                AdminHomeController adminHomeController = new AdminHomeController(stage, model);

                                loader.setController(adminHomeController);
                                BorderPane root = loader.load();

                                adminHomeController.showStage(root);

                                message.setText("");
                                stage.close();
                            } catch (IOException e) {
                                message.setText(e.getMessage());
                            }

                        }
                        else {
                            try { //loading the home view
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
                                HomeController homeController = new HomeController(stage, model);

                                loader.setController(homeController);
                                VBox root = loader.load();

                                homeController.showStage(root);

                                message.setText("");
                                stage.close();
                            } catch (IOException e) {
                                message.setText(e.getMessage());
                            }
                        }
						
					} else {
						message.setText("Wrong username or password");
						message.setTextFill(Color.RED);
					}
				} catch (SQLException e) {
					message.setText(e.getMessage());
					message.setTextFill(Color.RED);
				}
				
			} else {
				message.setText("Empty username or password");
				message.setTextFill(Color.RED);
			}
			name.clear();
			password.clear();
		});

        //WHen the signup button is pressed
		signup.setOnAction(event -> {
			try {
                //load the sign-up page
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignupView.fxml"));
				
				// Customize controller instance
				SignupController signupController =  new SignupController(stage, model);

				loader.setController(signupController);
				VBox root = loader.load();
				
				signupController.showStage(root);

                //clear the fields for this page
				message.setText("");
				name.clear();
				password.clear();

                //close this page
				stage.close();
			} catch (IOException e) {
				message.setText(e.getMessage());
			}
        });
	}
	
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 500, 300);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setTitle("Login");
		stage.show();
	}
}

