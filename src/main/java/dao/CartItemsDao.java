package dao;

import model.CartItem;

import java.sql.SQLException;

public interface CartItemsDao {
    void setup() throws SQLException;
    boolean addProject(CartItem cartItem) throws SQLException;
}
