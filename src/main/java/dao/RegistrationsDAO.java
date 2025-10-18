package dao;
import controller.CheckoutController;

import javafx.collections.ObservableList;
import model.Registration;
import model.RegistrationView;

import java.sql.SQLException;
import java.util.List;

public interface RegistrationsDAO {
    void setup() throws SQLException;
    void addCartItems(Registration registration) throws SQLException;
    ObservableList<RegistrationView> getRegistrationHistory(String username) throws SQLException;
}
