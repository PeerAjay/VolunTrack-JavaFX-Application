package dao;

import java.sql.SQLException;

import javafx.collections.ObservableList;
import model.Project;
import model.ProjectAdd;
import model.Registration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface ProjectsDao {
    void setup() throws SQLException;
    ObservableList<Project> loadProjects() throws SQLException;
    ObservableList<Project> loadProjectsAdmin() throws SQLException;
    void changeSlots(Registration registration) throws SQLException;
    Map<String, List<Project>> getGroupedProjects() throws SQLException;
    void enableDisableProject(int projectId, String isEnabled) throws SQLException;
    boolean projectExists(String title, String location, String day) throws SQLException;
    void addProject(ProjectAdd projectAdd) throws SQLException;
    void updateProject(Project project) throws SQLException;

}
