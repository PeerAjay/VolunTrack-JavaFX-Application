package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Registration;
import model.Project;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationsDAOImpl implements RegistrationsDAO{
    private String TABLE_NAME = "registrations";

    public RegistrationsDAOImpl(){};

    @Override
    public void setup() throws SQLException{
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (registrationID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " userId STRING," +
                    " projectID INTEGER," +
                    " slotsRegistered INTEGER," +
                    " hoursPerSlot INTEGER, " +
                    " totalContribution INTEGER, " +
                    " timestamp STRING)";

            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void addCartItems(Registration registration) throws SQLException {

    }
}
