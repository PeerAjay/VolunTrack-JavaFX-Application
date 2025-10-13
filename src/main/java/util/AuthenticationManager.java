package util;

import dao.UserDao;
import dao.UserDaoImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import model.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationManager {

    public static boolean loginVerify(String username, String passwordPlain){
        UserDao userDao = new UserDaoImpl();
        try {
            String hashedPassword = userDao.getHashedPassword(username);

            if (hashedPassword != null && BCrypt.checkpw(passwordPlain, hashedPassword)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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

    public static List<String> existsCheck(final String fullName, final String email, final String userName) {
        UserDao userDao = new UserDaoImpl();
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

    public static boolean correctOldPassword(String password){
        User user = SessionManager.getInstance().getCurrentUser();

        String hashedPasswordFrom = user.getPassword();

        return BCrypt.checkpw(password, hashedPasswordFrom);
    }

}
