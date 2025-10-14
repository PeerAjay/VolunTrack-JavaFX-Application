package model;

import java.sql.SQLException;

import dao.UserDao;
import dao.UserDaoImpl;

import dao.ProjectsDao;
import dao.ProjectsDaoImpl;

public class Model {
	private UserDao userDao;
    private ProjectsDao projectDao;
	private User currentUser; 
	
	public Model() {
		userDao = new UserDaoImpl();
        projectDao = new ProjectsDaoImpl();
	}
	
	public void setup() throws SQLException {
		userDao.setup();
        projectDao.setup();
	}

	public UserDao getUserDao() {
		return userDao;
	}

    public ProjectsDao getProjectsDao() {
        return projectDao;
    }
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}
}
