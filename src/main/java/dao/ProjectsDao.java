package dao;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import model.Project;
import model.Registration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ProjectsDao {
    void setup() throws SQLException;
    ObservableList<Project> loadProjects() throws SQLException;
    void changeSlots(Registration registration) throws SQLException;
    Map<String, List<Project>> getGroupedProjects() throws SQLException;

}
