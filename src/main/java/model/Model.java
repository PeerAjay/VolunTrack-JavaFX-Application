package model;

import java.sql.SQLException;

import dao.*;

public class Model {
	private UserDao userDao;
    private ProjectsDao projectDao;
    private CartItemsDao cartItemsDao;
	private User currentUser; 
	
	public Model() {
		userDao = new UserDaoImpl();
        projectDao = new ProjectsDaoImpl();
        cartItemsDao = new CartItemsDaoImpl();
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

    public CartItemsDao getCartItemsDao() {
        return cartItemsDao;
    }
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}
}
