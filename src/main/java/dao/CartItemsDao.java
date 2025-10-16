package dao;

import model.CartEntry;
import model.CartItem;

import java.sql.SQLException;
import java.util.List;

public interface CartItemsDao {
    void setup() throws SQLException;
    void addProject(CartItem cartItem) throws SQLException;
    List<CartEntry> getCartEntries(String username) throws SQLException;
}
