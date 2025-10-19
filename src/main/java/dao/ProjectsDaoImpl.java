package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Registration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Project;

public class ProjectsDaoImpl implements ProjectsDao {
    private final String TABLE_NAME = "projects";

    public ProjectsDaoImpl(){}

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (title VARCHAR(10) NOT NULL,"
                    + "location VARCHAR(60) NOT NULL," + "day VARCHAR(20) NOT NULL," + " hourlyValue VARCHAR(40) NOT NULL,"
                    + "regSlots VARCHAR(40) NOT NULL,"
                    + "totalSlots VARCHAR(40) NOT NULL"
                    + ")";

            stmt.executeUpdate(sql);
        }
    }

    //Function to get all the projects from the database
    @Override
    public ObservableList<Project> loadProjects() throws SQLException{
        ObservableList<model.Project> projects = FXCollections.observableArrayList();

        //Select all from the projects table
        String sql = "SELECT * FROM projects WHERE isEnabled = true";


        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            //Interating through each result set and creating a project model and adding it to the output list
            while (rs.next()) {
                int projectId = rs.getInt("projectID");
                String title = rs.getString("title");
                String location = rs.getString("location");
                String day = rs.getString("day");
                int hourlyValue = rs.getInt("hourlyValue"); // Assuming you renamed the column
                String regSlots = rs.getString("regSlots");
                String totalSlots = rs.getString("totalSlots");

                Project project = new Project(projectId, title, location, day, hourlyValue, regSlots, totalSlots);
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching projects from database: " + e.getMessage());
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public void changeSlots(Registration registration) throws SQLException{
        String getSql = "SELECT regSlots, totalSlots FROM projects WHERE projectID = ?";
        String changeSql = "UPDATE projects SET regSlots = ?, totalSlots = ? WHERE projectID = ?";


        try (Connection conn = Database.getConnection()) {
            int currentRegSlots;
            int currentTotalSlots;

            //First, getting what the current values for the registered and total available slots are
            try (PreparedStatement selectStmt = conn.prepareStatement(getSql)) {
                selectStmt.setInt(1, registration.getProjectId());
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    currentRegSlots = rs.getInt("regSlots");
                    currentTotalSlots = rs.getInt("totalSlots");
                } else {
                    throw new SQLException("Project with ID " + registration.getProjectId() + " not found.");
                }
            }

            //Calculate what the new values will be based on the old ones
            int newRegSlots = currentRegSlots + registration.getRegSlots();
            int newTotalSlots = currentTotalSlots - registration.getRegSlots();
            if (newRegSlots > currentTotalSlots) {
                // Not enough slots available
                conn.rollback();
            }

            //Writing the changed values to the database
            try (PreparedStatement updateStmt = conn.prepareStatement(changeSql)) {
                updateStmt.setInt(1, newRegSlots);
                updateStmt.setInt(2, newTotalSlots);
                updateStmt.setInt(3, registration.getProjectId());
                updateStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw e;
        }

    }

    @Override
    public Map<String, List<Project>> getGroupedProjects() throws SQLException {
        List<Project> allProjects = loadProjects();

        Map<String, List<Project>> groupedProjects = allProjects.stream().collect(Collectors.groupingBy(Project::getTitle));

        return groupedProjects;
    }

    @Override
    public void enableDisableProject(int projectId, String isEnabled) throws SQLException {
        String sql = "UPDATE projects SET isEnabled = ? WHERE projectID = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isEnabled);
            pstmt.setInt(2, projectId);
            pstmt.executeUpdate();
        }
    }

}
