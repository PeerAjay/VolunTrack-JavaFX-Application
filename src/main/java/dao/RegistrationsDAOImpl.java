package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Registration;
import model.Project;
import model.RegistrationView;
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

    //Adding the cart item registration into the regisrtation database
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

            stmt.executeUpdate();
        }

    }

    //Getting the history of registrations for a specific user
    @Override
    public ObservableList<RegistrationView> getRegistrationHistory(String username) throws SQLException{
        ObservableList<RegistrationView> registrations = FXCollections.observableArrayList();

        String sql = "SELECT r.registrationID, r.userId, r.slotsRegistered, r.hoursPerSlot, r.totalContribution, r.timestamp, " +
                "p.title, p.location, p.day " +
                "FROM registrations r " +
                "JOIN projects p ON r.projectID = p.projectID " +
                "WHERE r.userID = ? " +
                "ORDER BY r.timestamp DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            while (rs.next()) {
                int regId = rs.getInt("registrationID");
                int slots = rs.getInt("slotsRegistered");
                int hours = rs.getInt("hoursPerSlot");
                int contribution = rs.getInt("totalContribution");
                LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"), formatter);
                String title = rs.getString("title");
                String location = rs.getString("location");
                String day = rs.getString("day");
                String usernameID = rs.getString("userId");

                registrations.add(new RegistrationView(regId, slots, hours, contribution, timestamp, title, location, day, usernameID));
            }
        }

        return registrations;
    }

    //Getting the history of registrations for ALL users for admin viewing
    @Override
    public ObservableList<RegistrationView> getRegistrationHistory() throws SQLException{
        ObservableList<RegistrationView> registrations = FXCollections.observableArrayList();

        String sql = "SELECT r.registrationID, r.userId, r.slotsRegistered, r.hoursPerSlot, r.totalContribution, r.timestamp, " +
                "p.title, p.location, p.day " +
                "FROM registrations r " +
                "JOIN projects p ON r.projectID = p.projectID " +
                "ORDER BY r.timestamp DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            while (rs.next()) {
                int regId = rs.getInt("registrationID");
                int slots = rs.getInt("slotsRegistered");
                int hours = rs.getInt("hoursPerSlot");
                int contribution = rs.getInt("totalContribution");
                LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"), formatter);
                String title = rs.getString("title");
                String location = rs.getString("location");
                String day = rs.getString("day");
                String username = rs.getString("userId");

                registrations.add(new RegistrationView(regId, slots, hours, contribution, timestamp, title, location, day, username));
            }
        }

        return registrations;
    }

}
