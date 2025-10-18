package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    @FXML private Button refresh;
    @FXML private Label status;
    @FXML private Button checkout;

    @FXML private TableColumn<CartEntry, Void> modify;
    @FXML private TableColumn<CartEntry, Void> delete;
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

        //Using callback to add a button to the button column, basically a blueprint telling tableview what  to add in the cells in that column
        Callback<TableColumn<CartEntry, Void>, TableCell<CartEntry, Void>> cellFactory = new Callback<>() {

            //Method that iss called by callback each time it needs to make a new cell
            @Override
            public TableCell<CartEntry, Void> call(final TableColumn<CartEntry, Void> param) {
                final TableCell<CartEntry, Void> cell = new TableCell<>() {

                    //Adding a button to the cell
                    private final Button btn = new Button("Modify");

                    {
                        //When the cell button is pressed
                        btn.setOnAction(event -> {
                            // Getting the Project object for the row
                            //Project project = getTableView().getItems().get(getIndex());

                            try {
                                //Getting the cartEntry the button being pressed is in
                                CartEntry selectedEntry = getTableView().getItems().get(getIndex());

                                //load the sign-up page
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyRegistrationView.fxml"));

                                // Customize controller instance
                                ModifyRegistrationController modifyRegistrationController =  new ModifyRegistrationController(stage, model);

                                loader.setController(modifyRegistrationController);
                                VBox root = loader.load();

                                //Set the cart item ID
                                modifyRegistrationController.setCartItemID(selectedEntry.getCartItemID());

                                modifyRegistrationController.showStage(root);

                            } catch (IOException e) {
                                //message.setText(e.getMessage());
                                System.out.println("AHHHHHHHHHHHHH");
                            }
                        });
                    }

                    // This method is called by callback to update the cell's content
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            // If the row is empty, don't show the button
                            setGraphic(null);
                        } else {
                            // If the row is not empty, show the button
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        Callback<TableColumn<CartEntry, Void>, TableCell<CartEntry, Void>> deleteCellFactory = new Callback<>() {

            //Method that iss called by callback each time it needs to make a new cell
            @Override
            public TableCell<CartEntry, Void> call(final TableColumn<CartEntry, Void> param) {
                final TableCell<CartEntry, Void> cell = new TableCell<>() {

                    //Adding a button to the cell
                    private final Button btn = new Button("Delete");

                    {
                        //When the cell button is pressed
                        btn.setOnAction(event -> {
                            CartEntry selectedEntry = getTableView().getItems().get(getIndex());

                            try {
                                model.getCartItemsDao().deleteItem(selectedEntry.getCartItemID());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            status.setText("Successfully Deleted, Please refresh");
                            status.setTextFill(Color.GREEN);

                        });
                    }

                    // This method is called by callback to update the cell's content
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            // If the row is empty, don't show the button
                            setGraphic(null);
                        } else {
                            // If the row is not empty, show the button
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        modify.setCellFactory(cellFactory);
        delete.setCellFactory(deleteCellFactory);

        refresh.setOnAction(event ->{
            stage.close();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml")); // Make sure this path is correct

                CartController newCartController = new CartController(parentStage, model);
                loader.setController(newCartController);

                Pane root = loader.load();
                newCartController.showStage(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        checkout.setOnAction(event -> {

            if(cartEntries.isEmpty()){
                status.setText("Cart Is Empty");
                status.setTextFill(Color.RED);
            }
            else {
                try {
                    //load the checkout page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/checkoutView.fxml"));

                    // Customize controller instance
                    CheckoutController checkoutController = new CheckoutController(stage, model);

                    loader.setController(checkoutController);
                    VBox root = loader.load();

                    checkoutController.showStage(root);

                    //close this page
                    stage.close();
                } catch (IOException e) {
                    status.setText(e.getMessage());
                }
            }


        });


        //When the back to home button is pressed go back to home
        backToHome.setOnAction(event ->{
            stage.close();
            parentStage.show();
        });

    }

    //Loading the cart's from the database into a list
    public ObservableList<CartEntry> loadCartData() throws SQLException {
        User user = SessionManager.getInstance().getCurrentUser();
        ObservableList<CartEntry> cartEntries = FXCollections.observableArrayList();

        cartEntries = model.getCartItemsDao().getCartEntries(user.getUsername());

        return cartEntries;

    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 720, 300);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Cart");
        stage.show();
    }
}
