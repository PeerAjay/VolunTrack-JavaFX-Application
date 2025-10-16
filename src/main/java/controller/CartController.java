package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CartEntry;
import model.Model;
import model.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import util.AuthenticationManager;
import util.SessionManager;

public class CartController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    @FXML private Button backToHome; //This corresponds to the back to home button

    @FXML  private TableView<CartEntry> cartTableView; //This corresponds to the cart table

    public CartController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {
        ObservableList<CartEntry> cartEntries = loadCartData(); //Get the cart items
        cartTableView.setItems(cartEntries); //Load the items onto the table

        //When the back to home button is pressed go back to home
        backToHome.setOnAction(event ->{
            stage.close();
            parentStage.show();
        });

    }

    //Loading the cart's from the database into a list
    private ObservableList<CartEntry> loadCartData() throws SQLException {
        User user = SessionManager.getInstance().getCurrentUser();
        ObservableList<CartEntry> cartEntries = FXCollections.observableArrayList();

        cartEntries = model.getCartItemsDao().getCartEntries(user.getUsername());

        return cartEntries;

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 600, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Cart");
        stage.show();
    }

}
