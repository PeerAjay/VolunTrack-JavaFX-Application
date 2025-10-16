package dao;

import model.CartEntry;
import model.CartItem;
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

    @Override
    public List<CartEntry> getCartEntries(String username) throws SQLException{
        List<CartEntry> cartEntries = new ArrayList<>();

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

                cartEntries.add(new CartEntry(title, location, day, hourlyValue, slotsToRegister, hoursPerSlot));
            }
        }

        return cartEntries;
    }

}
