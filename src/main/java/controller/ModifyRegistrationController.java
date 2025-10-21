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
import model.*;
import util.AuthenticationManager;

import java.util.List;
import java.util.ArrayList;

public class ModifyRegistrationController {
    @FXML private Spinner<Integer> numSlotsInput; //Corresponds to the number of slots field
    @FXML private Spinner<Integer> numHoursInput; //Corresponds to the number of hours field
    @FXML private Button modify; //Corresponds to add to cart button
    @FXML private Button cancel; //Corresponds to the cancel button
    @FXML private Label status; //Corresponds to the status label

    private Stage stage;
    private Stage parentStage;
    private Model model;

    private CartEntry cartEntry;
    private int slotsRemaining;
    private int projectID;
    private int remainingSlots;


    public ModifyRegistrationController(){
    }

    public ModifyRegistrationController(Stage parentStage, Model model){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {
        projectID = cartEntry.getProjectID();

        remainingSlots =  model.getProjectsDao().getNumSlotsRemaining(Integer.toString(projectID));

        //When the modify button is pressed
        modify.setOnAction(event ->{

            if(numSlotsInput.getValue() > remainingSlots){
                status.setText("Not enough slots left");
                status.setTextFill(Color.RED);
            }
            else {
                //Modify the cart item corresponding to the id with the nre values
                try {
                    model.getCartItemsDao().modifyEntry(cartEntry.getCartItemID(), numSlotsInput.getValue(), numHoursInput.getValue());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //close the stage automatically after modify
                stage.close();
            }
        });

        //Close the popup when the cancel button is pressed
        cancel.setOnAction(event ->{
            stage.close();
        });

    }

    //Helper method to set the cartId of the item to be modified
    public void setCartEntry(CartEntry cartItem){
        this.cartEntry = cartItem;
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Modify Registration");
        stage.show();
    }

}
