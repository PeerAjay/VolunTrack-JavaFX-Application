package util;

import model.User;

//This is a java class that I am using to manage user sessions when they log in so that I dont have to
//  keep passing a user object around
public class SessionManager {
    private static SessionManager instance;

    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is logged in.");
        }
        return currentUser;
    }

    public void clearSession() {
        currentUser = null;
    }

}
