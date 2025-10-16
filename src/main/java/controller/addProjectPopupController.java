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
import model.CartItem;
import model.Model;
import model.Project;
import model.User;
import util.AuthenticationManager;

import java.util.List;
import java.util.ArrayList;

public class addProjectPopupController {
    @FXML
    private TextField numSlotsInput; //Corresponds to the number of slots field
    @FXML
    private TextField numHoursInput; //Corresponds to the number of hours field
    @FXML
    private Button addInputToCart; //Corresponds to add to cart button
    @FXML
    private Button cancel; //Corresponds to the cancel button

    private Stage stage;
    private Stage parentStage;
    private Model model;

    private CartItem cartItem = new CartItem();

    public addProjectPopupController(){
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {


    }

    public void setUserProject(String username, Project project){
        cartItem.setUsername(username);
        cartItem.setProjectID(project.getProjectId());
    }

}
