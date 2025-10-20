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


}
