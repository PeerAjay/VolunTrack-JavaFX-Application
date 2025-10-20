package util;

import dao.UserDao;
import dao.UserDaoImpl;

import dao.ProjectsDao;
import dao.ProjectsDaoImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import model.Model;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

//This is a java class that helps with data validation and authentication

public class AuthenticationManager {

    //Check if the password is correct for the given username on login
    public static boolean loginVerify(String username, String passwordPlain){
        UserDao userDao = new UserDaoImpl();
        try {
            //Hashing the plaintext password to compare it
            String hashedPassword = userDao.getHashedPassword(username);

            //Using bcrypt to compare the hashed password from the database to the hashed input password
            if (hashedPassword != null && BCrypt.checkpw(passwordPlain, hashedPassword)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Get the errors for data validation on the creationof a password
    public static List<String> getPasswordErrors(final String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.length() < 8) {
            errors.add("Password must be at least 8 characters long.");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one number.");
        }
        if (!password.matches(".*[@$!%*?&].*")) {
            errors.add("Password must contain at least one special character (@$!%*?&).");
        }

        return errors;
    }

    //Overloaded method used for when the password is being changed not created, has some extra checks
    public static List<String> getPasswordErrors(String oldPassword, final String newPassword) {
        List<String> errors = new ArrayList<>();

        User user = SessionManager.getInstance().getCurrentUser();
        String hashedPasswordFrom = user.getPassword();

        if (newPassword == null || newPassword.length() < 8) {
            errors.add("Password must be at least 8 characters long.");
        }
        if (!newPassword.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter.");
        }
        if (!newPassword.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter.");
        }
        if (!newPassword.matches(".*\\d.*")) {
            errors.add("Password must contain at least one number.");
        }
        if (!newPassword.matches(".*[@$!%*?&].*")) {
            errors.add("Password must contain at least one special character (@$!%*?&).");
        }
        if (oldPassword == null || oldPassword.isEmpty()){
            errors.add("oldPassword field is empty");
        }
        if (BCrypt.checkpw(newPassword, hashedPasswordFrom)){
            errors.add("New password cannot be the same as the old password");
        }
        if (!BCrypt.checkpw(oldPassword, hashedPasswordFrom)){
            errors.add("Wrong Old Password");
        }

        return errors;
    }

    //Another data validation check that checks whether the various fields exists
    public static List<String> existsCheck(final String fullName, final String email, final String userName, UserDao userDao) {
        //UserDao userDao = new UserDaoImpl();
        List<String> errors = new ArrayList<>();

        if (fullName == null || fullName.isBlank()) {
            errors.add("Please enter your full name");
            //System.out.println("AUTHMANAGER FULLNAME");
        }
        if (email == null || email.isBlank()) {
            errors.add("Please enter your email");
            //System.out.println("AUTHMANAGER EMAIL");
        }
        if (userName == null || userName.isBlank()) {
            errors.add("Please enter your username");
            //System.out.println("AUTHMANAGER USERNAME");
        }

        try {
            if (userDao.checkUsernameDuplicate(userName)){
                errors.add("Username already taken");
            }
        } catch (SQLException e) {
            errors.add("ERROR checking username");
            e.printStackTrace();
        }

        return errors;
    }

    //Checking if the old password is correct when changing password
    public static boolean correctOldPassword(String password){
        //using sessionmanager to get the current logged in user
        User user = SessionManager.getInstance().getCurrentUser();

        String hashedPasswordFrom = user.getPassword();

        //Using bcrypt to check the password with th hashed password from the current user
        return BCrypt.checkpw(password, hashedPasswordFrom);
    }

    public static List<String> validateProgramAddition(String title, String location, String day, String hourlyValue, String totalSlots, ProjectsDao projectsDao) throws SQLException {
        List<String> errors = new ArrayList<>();
        Boolean hourlyValueInt = false;
        Boolean totalSlotsInt = false;

        try {
            Integer.parseInt(hourlyValue);
            hourlyValueInt = true;
        } catch (NumberFormatException e) {
            hourlyValueInt =  false;
        }

        try {
            Integer.parseInt(totalSlots);
            totalSlotsInt = true;
        } catch (NumberFormatException e) {
            totalSlotsInt =  false;
        }


        if (title == null || title.isBlank()) {
            errors.add("Please enter a title");
        }
        if (title.length() > 30) {
            errors.add("Title too long, must be below 30 characters");
        }
        if (location == null || location.isBlank()) {
            errors.add("Please enter a location");
        }
        if (location.length() > 30) {
            errors.add("Location too long, must be below 30 characters");
        }
        if (day == null || day.isBlank() || !checkDay(day)) {
            errors.add("Please enter valid a day (e.g: Wed)");
        }

        if (hourlyValue == null || hourlyValue.isBlank() || !hourlyValueInt) {
            errors.add("Hourly NUMBER must be between 0 and 100");
        } else if ( Integer.parseInt(hourlyValue) < 1 ||  Integer.parseInt(hourlyValue) > 100) {
            errors.add("Hourly value must be between 0 and 100");
        }
        if (totalSlots == null || totalSlots.isBlank() || !totalSlotsInt) {
            errors.add("Total slots must be a NUMBER between 0 and 100");
        } else if ( Integer.parseInt(totalSlots) < 1 ||  Integer.parseInt(totalSlots) > 100) {
            errors.add("Total Slots must be between 0 and 100");
        }

        if (location.length() > 30) {
            errors.add("Location too long, must be below 30 characters");
        }
        if(projectsDao.projectExists(title, location, day)){
            errors.add("Project already exists");
        }

        return errors;
    }


    public static boolean checkDay(String day){
        if(!day.equals("Mon") && !day.equals("Tue") && !day.equals("Wed") && !day.equals("Thu") && !day.equals("Fri") && !day.equals("Sat") && !day.equals("Sun")){
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkDuplicateProject(String title, String location, String day) throws SQLException {
        ProjectsDao projectsDao = new ProjectsDaoImpl();

        return projectsDao.projectExists(title, location, day);
    }
}
