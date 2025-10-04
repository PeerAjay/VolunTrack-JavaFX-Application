package util;

import java.util.List;
import java.util.ArrayList;

public class PasswordManager {

    public static List<String> getValidationErrors(final String password) {
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

}
