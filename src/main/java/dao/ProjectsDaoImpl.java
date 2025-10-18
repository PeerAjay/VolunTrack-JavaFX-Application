package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

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
        String sql = "SELECT * FROM projects";


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

}
