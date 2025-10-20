import dao.UserDao;
import model.User;
import org.junit.jupiter.api.Test;
import util.AuthenticationManager;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class tests {
    private static class FakeUserDao implements UserDao {
        @Override
        public boolean checkUsernameDuplicate(String username) throws SQLException {
            return "existinguser".equals(username);
        }

        @Override public void setup() throws SQLException {}
        @Override public User getUser(String username) throws SQLException { return null; }
        @Override public User createUser(String email, String fullName, String username, String password) throws SQLException { return null; }
        @Override public String getHashedPassword(String username) throws SQLException { return null; }
        @Override public boolean changePassword(String password, String username) throws SQLException { return false; }
    }


    @Test
    void testPasswordValidation() {
        assertTrue(AuthenticationManager.getPasswordErrors("ValidPass1@").isEmpty(),
                "A correct password should produce no errors.");

        List<String> errors = AuthenticationManager.getPasswordErrors("short");
        assertFalse(errors.isEmpty(), "An invalid password should produce errors.");
        assertTrue(errors.contains("Password must be at least 8 characters long."),
                "Error message for short password should be present.");

        assertFalse(AuthenticationManager.getPasswordErrors("nouppercase1@").isEmpty());
        assertFalse(AuthenticationManager.getPasswordErrors("NOLOWERCASE1@").isEmpty());
        assertFalse(AuthenticationManager.getPasswordErrors("NoNumber@").isEmpty());
        assertFalse(AuthenticationManager.getPasswordErrors("NoSpecialChar1").isEmpty());
    }

    @Test
    void testUsernameUniqueness() {
        UserDao mockDao = new FakeUserDao();

        List<String> errorsForExistingUser = AuthenticationManager.existsCheck("Test User", "test@test.com", "existinguser", mockDao);
        assertFalse(errorsForExistingUser.isEmpty(), "Should return errors for a duplicate username.");
        assertTrue(errorsForExistingUser.contains("Username already taken"), "Should contain the 'username taken' error message.");

        List<String> errorsForNewUser = AuthenticationManager.existsCheck("Test User", "test@test.com", "newuser", mockDao);
        assertTrue(errorsForNewUser.isEmpty(), "Should return no errors for a new, valid username.");
    }

    @Test
    void testConfirmationCodeValidation() {
        java.util.function.Predicate<String> isValidCode = code -> code != null && code.matches("\\d{6}");

        assertTrue(isValidCode.test("123456"), "A valid 6-digit code should return true.");
        assertFalse(isValidCode.test("12345"), "Should be invalid: less than 6 digits.");
        assertFalse(isValidCode.test("1234567"), "Should be invalid: more than 6 digits.");
        assertFalse(isValidCode.test("abcdef"), "Should be invalid: contains letters.");
        assertFalse(isValidCode.test("12.345"), "Should be invalid: contains symbols.");
        assertFalse(isValidCode.test(null), "A null code should be invalid.");
        assertFalse(isValidCode.test(""), "An empty code should be invalid.");
    }

    @Test
    void testProjectDateRestriction() {
        int todayDayValue = 3; // THIS IS WEDNESDAY

        int pastDayValue = getDayValue("Mon");
        assertFalse(pastDayValue >= todayDayValue, "Should be invalid: cannot register for a past day (Monday).");

        int sameDayValue = getDayValue("Wed");
        assertTrue(sameDayValue >= todayDayValue, "Should be valid: can register for the same day (Wednesday).");

        int futureDayValue = getDayValue("Fri");
        assertTrue(futureDayValue >= todayDayValue, "Should be valid: can register for a future day (Friday).");
    }

    private int getDayValue(String day) {
        switch (day.toLowerCase()) {
            case "mon": return 1;
            case "tue": return 2;
            case "wed": return 3;
            case "thu": return 4;
            case "fri": return 5;
            case "sat": return 6;
            case "sun": return 7;
            default: return 0;
        }
    }


}
