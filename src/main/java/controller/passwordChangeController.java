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

    @FXML private Button confirmChange;
    @FXML private Button back;
    @FXML private TextField oldPassword;
    @FXML private TextField newPassword;
    @FXML private Label status;

    public passwordChangeController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();

        confirmChange.setOnAction(event ->{
            List<String> errors = new ArrayList<>();
            errors = AuthenticationManager.getPasswordErrors(oldPassword.getText(), newPassword.getText());

            if(errors.isEmpty()){
                try {
                    model.getUserDao().changePassword(newPassword.getText(), user.getUsername());
                    String errorMessage = "Password Changed!";
                    status.setText(errorMessage);
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

        //TO DO: IMPLEMENT LATER
//        back.setOnAction(event ->{
//
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
//                // Customize controller instance
//                HomeController homeController =  new homeController(stage, model);
//
//                loader.setController(homeController);
//                VBox root = loader.load();
//
//                homeController.showStage(root);
//
//                stage.close();
//            } catch (IOException e) {
//                System.out.println("home change error");
//            }
//
//        });

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 600, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Change Password");
        stage.show();
    }

}
