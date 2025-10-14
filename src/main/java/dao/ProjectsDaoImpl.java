package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
                    + "location VARCHAR(60) NOT NULL," + "day VARCHAR(20) NOT NULL," + " hourlyValue VARCHAR(40) NOT NULL,";
            stmt.executeUpdate(sql);
        }
    }

    @Override
    public ObservableList<Project> loadProjects() throws SQLException{
        ObservableList<model.Project> projects = FXCollections.observableArrayList();


        return projects;
    }

}
