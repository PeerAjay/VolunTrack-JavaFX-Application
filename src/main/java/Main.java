import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.fxml.FXMLLoader;

import model.Model;
import controller.LoginController;

public class Main extends Application {
	private Model model;

	@Override
	public void init() {
		model = new Model();
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			model.setup();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
			
			// Customize controller instance
			LoginController loginController = new LoginController(primaryStage, model);

			loader.setController(loginController);

			GridPane root = loader.load();

			loginController.showStage(root);
		} catch (IOException | SQLException | RuntimeException e) {
			Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
			primaryStage.setTitle("Error");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
