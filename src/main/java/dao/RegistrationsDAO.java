package dao;
import controller.CheckoutController;

import model.Registration;

import java.sql.SQLException;
import java.util.List;

public interface RegistrationsDAO {
    void setup() throws SQLException;
    void addCartItems(Registration registration) throws SQLException;
}
