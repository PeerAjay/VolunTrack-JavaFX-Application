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
                    " userID INTEGER," +
                    " projectID INTEGER," +
                    " slotsToRegister INTEGER," +
                    " hoursPerSlot INTEGER)";

            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void addProject(CartItem cartItem) throws SQLException{

        String sql = "INSERT INTO cart_items (userID, projectID, slotsToRegister, hoursPerSlot) VALUES (?, ?, ?, ?)";

            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, cartItem.getUsername());
                pstmt.setInt(2, cartItem.getProjectID());
                pstmt.setInt(3, cartItem.getNumSlots());
                pstmt.setInt(4, cartItem.getHoursPerSlot());

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
        String sql = "SELECT p.title, p.location, p.day, p.hourlyValue, c.slotsToRegister, c.hoursPerSlot " +
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

                //Adding to the list of cartEntries to display onto the cart table
                cartEntries.add(new CartEntry(title, location, day, hourlyValue, slotsToRegister, hoursPerSlot));
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

}
