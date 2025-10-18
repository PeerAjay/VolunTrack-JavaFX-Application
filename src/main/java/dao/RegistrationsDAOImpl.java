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

    @Override
    public List<RegistrationView> getRegistrationHistory(String username) throws SQLException{
        List<RegistrationView> registrations = new ArrayList<>();

        String sql = "SELECT r.registrationID, r.slotsRegistered, r.hoursPerSlot, r.totalContribution, r.timestamp, " +
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

                registrations.add(new RegistrationView(regId, slots, hours, contribution, timestamp, title, location, day));
            }
        }

        return registrations;
    }

}
