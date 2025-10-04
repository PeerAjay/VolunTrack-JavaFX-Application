package util;

import java.util.List;
import java.util.ArrayList;

public class AuthenticationManager {

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

    public static List<String> existsCheck(final String fullName, final String email, final String userName) {
        List<String> errors = new ArrayList<>();

        if (fullName == null) {
            errors.add("Please enter your full name");
        }
        if (email == null) {
            errors.add("Please enter your email");
        }
        if (userName == null) {
            errors.add("Please enter your username");
        }

        return errors;
    }

}
