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

public class ModifyRegistrationController {
    @FXML
    private Spinner<Integer> numSlotsInput; //Corresponds to the number of slots field
    @FXML
    private Spinner<Integer> numHoursInput; //Corresponds to the number of hours field
    @FXML
    private Button modify; //Corresponds to add to cart button
    @FXML
    private Button cancel; //Corresponds to the cancel button

    private Stage stage;
    private Stage parentStage;
    private Model model;

    private int cartItemID;


    public ModifyRegistrationController(){
    }

    public ModifyRegistrationController(Stage parentStage, Model model){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        modify.setOnAction(event ->{
            try {
                model.getCartItemsDao().modifyEntry(cartItemID, numSlotsInput.getValue(), numHoursInput.getValue());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //close the stage automatically after modify
            stage.close();
        });

        //Close the popup when the cancel button is pressed
        cancel.setOnAction(event ->{
            stage.close();
        });

    }

    public void setCartItemID(int cartItemID){
        this.cartItemID = cartItemID;
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Add To Cart");
        stage.show();
    }

}
