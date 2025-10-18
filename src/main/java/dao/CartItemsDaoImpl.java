package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CartEntry;
import model.CartItem;
import model.Project;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemsDaoImpl implements CartItemsDao{
    private String TABLE_NAME = "cart_items";
    public CartItemsDaoImpl(){};

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (cartItemID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " userID STRING," +
                    " projectID INTEGER," +
                    " slotsToRegister INTEGER," +
                    " hoursPerSlot INTEGER," +
                    " hourlyValue INTEGER)";

            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void addProject(CartItem cartItem) throws SQLException{

        String sql = "INSERT INTO cart_items (userID, projectID, slotsToRegister, hoursPerSlot, hourlyValue) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, cartItem.getUsername());
                pstmt.setInt(2, cartItem.getProjectID());
                pstmt.setInt(3, cartItem.getNumSlots());
                pstmt.setInt(4, cartItem.getHoursPerSlot());
                pstmt.setInt(5, cartItem.getHourlyValue());

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    //Function to get all the cartEntries for the current user
    @Override
    public ObservableList<CartEntry> getCartEntries(String username) throws SQLException{
        ObservableList<CartEntry> cartEntries = FXCollections.observableArrayList();

        //Using a join swl query to get values from both the project table and the cart_items table with the same username
        String sql = "SELECT p.title, p.location, p.day, p.hourlyValue, c.slotsToRegister, c.hoursPerSlot, c.cartItemID " +
                "FROM cart_items c " +
                "JOIN projects p ON c.projectID = p.projectID " +
                "WHERE c.userID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String location = rs.getString("location");
                String day = rs.getString("day");
                int hourlyValue = rs.getInt("hourlyValue");
                int slotsToRegister = rs.getInt("slotsToRegister");
                int hoursPerSlot = rs.getInt("hoursPerSlot");
                int cartId = rs.getInt("cartItemID");

                //Adding to the list of cartEntries to display onto the cart table
                cartEntries.add(new CartEntry(title, location, day, hourlyValue, slotsToRegister, hoursPerSlot, cartId));
            }
        }

        return cartEntries;
    }

    public void modifyEntry(int itemID, int newNumSlots, int newHoursPerSlot) throws SQLException{

        String sql = "UPDATE " + TABLE_NAME + " SET slotsToRegister = ?, hoursPerSlot = ? WHERE cartItemID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newNumSlots);
            pstmt.setInt(2, newHoursPerSlot);
            pstmt.setInt(3, itemID);

            pstmt.executeUpdate();
        }

    }

    public void deleteItem(int itemID) throws SQLException{
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE cartItemID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemID);

            stmt.executeUpdate();
        }

    }

    public void clear(String username) throws SQLException{
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE userID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            stmt.executeUpdate();
        }
    }

    public ObservableList<CartItem> getCartItems(String username) throws SQLException{
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE userID = ?";
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            System.out.println("DAO: Querying cart for user: " + username);

            while (rs.next()) {
                String usernameId = rs.getString("userID");
                int projectID = rs.getInt("projectID");
                int numSlots = rs.getInt("slotsToRegister");
                int hoursPerSlot = rs.getInt("hoursPerSlot");
                int hourlyValue = rs.getInt("hourlyValue");

                cartItems.add(new CartItem(usernameId, projectID, numSlots, hoursPerSlot, hourlyValue));
            }

        }

        return cartItems;
    }

}
