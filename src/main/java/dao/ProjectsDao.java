package dao;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import model.Project;

public interface ProjectsDao {
    void setup() throws SQLException;
    ObservableList<Project> loadProjects() throws SQLException;

}
