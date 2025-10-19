package dao;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import model.Project;
import model.Registration;

public interface ProjectsDao {
    void setup() throws SQLException;
    ObservableList<Project> loadProjects() throws SQLException;
    void changeSlots(Registration registration) throws SQLException;

}
