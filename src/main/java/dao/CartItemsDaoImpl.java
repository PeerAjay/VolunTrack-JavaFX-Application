package dao;

import model.CartItem;
import model.User;

import java.sql.*;

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
    public boolean addProject(CartItem cartItem) throws SQLException{

        String sql = "INSERT INTO cart_items (userID, projectID, slotsToRegister, hoursPerSlot) VALUES (?, ?, ?, ?)";

            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, cartItem.getUsername());
                pstmt.setInt(2, cartItem.getProjectID());
                pstmt.setInt(3, cartItem.getNumSlots());
                pstmt.setInt(4, cartItem.getHoursPerSlot());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return false;

            //TO DO COMPLETE THE ADDING OF THE PROJECT TO THE DATABASE TABLE ITS SPAGETTHI CODE
    }

}
