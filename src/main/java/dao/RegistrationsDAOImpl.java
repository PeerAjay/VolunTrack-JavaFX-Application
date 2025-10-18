package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Registration;
import model.Project;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        String sql = "INSERT INTO registrations (userID, projectID, slotsRegistered, hoursPerSlot, totalContribution, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            stmt.setString(1, registration.getUserId());
            stmt.setInt(2, registration.getProjectId());
            stmt.setInt(3, registration.getRegSlots());
            stmt.setInt(4, registration.getHoursPerSlot());
            stmt.setInt(5, registration.getTotalContribution());
            stmt.setString(6, LocalDateTime.now().format(formatter));

            stmt.executeUpdate(sql);
        }

    }
}
