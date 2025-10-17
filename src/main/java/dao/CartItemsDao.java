package dao;

import controller.CartController;
import javafx.collections.ObservableList;
import model.CartEntry;
import model.CartItem;
import model.Project;

import java.sql.SQLException;
import java.util.List;

public interface CartItemsDao {
    void setup() throws SQLException;
    void addProject(CartItem cartItem) throws SQLException;
    ObservableList<CartEntry> getCartEntries(String username) throws SQLException;
    void modifyEntry(int itemID, int newNumSlots, int newHoursPerSlot) throws SQLException;
    void deleteItem(int itemID) throws SQLException;
}
