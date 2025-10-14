package util;

import model.User;

//This is a java class that I am using to manage user sessions when they log in so that I dont have to
//  keep passing a user object around
public class SessionManager {
    private static SessionManager instance; //instance of session manager that will stay from program start till program exit

    private User currentUser;

    private SessionManager() {} //private constructor so it cant be called by any other method

    //Method to access the current instance of the session manager
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    //Set the current user of the current session
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    //Get the current user of the current session
    public User getCurrentUser() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is logged in.");
        }
        return currentUser;
    }

    //Clear the current user when the user logs out ending the session
    public void clearSession() {
        currentUser = null;
    }

}
