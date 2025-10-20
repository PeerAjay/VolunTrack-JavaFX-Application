package controller;

import java.awt.*;
import java.sql.SQLException;

import dao.CartItemsDao;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CartItem;
import model.Model;
import model.Project;
import model.User;
import util.AuthenticationManager;

import java.util.List;
import java.util.ArrayList;

public class addProjectPopupController {
    @FXML
    private Spinner<Integer> numSlotsInput; //Corresponds to the number of slots field
    @FXML
    private Spinner<Integer> numHoursInput; //Corresponds to the number of hours field
    @FXML
    private Button addInputToCart; //Corresponds to add to cart button
    @FXML
    private Button cancel; //Corresponds to the cancel button

    private Stage stage;
    private Stage parentStage;
    private Model model;

    private CartItem cartItem = new CartItem();

    public addProjectPopupController(){
    }

    public addProjectPopupController(Stage parentStage, Model model){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        addInputToCart.setOnAction(event ->{
            cartItem.setNumSlots(numSlotsInput.getValue());
            cartItem.setHoursPerSlot(numHoursInput.getValue());

            try {
                //System.out.println("ADDED ITEM HOURLY VALUE: " + cartItem.getHourlyValue());
                model.getCartItemsDao().addProject(cartItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //close the stage automatically after adding to cart
            stage.close();
        });

        //Close the popup when the cancel button is pressed
        cancel.setOnAction(event ->{
            stage.close();
        });

    }

    public void setUserProjectValue(String username, Project project){
        cartItem.setUsername(username);
        cartItem.setProjectID(project.getProjectId());
        cartItem.setHourlyValue(project.getHourlyValue());
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Add To Cart");
        stage.show();
    }

}
