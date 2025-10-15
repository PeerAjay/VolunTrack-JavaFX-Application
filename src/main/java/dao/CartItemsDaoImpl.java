package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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


}
