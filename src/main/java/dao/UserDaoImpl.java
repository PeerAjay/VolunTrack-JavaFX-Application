package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

import model.User;

public class UserDaoImpl implements UserDao {
	private final String TABLE_NAME = "users";

	public UserDaoImpl() {
	}

	@Override
	public void setup() throws SQLException {
		try (Connection connection = Database.getConnection();
				Statement stmt = connection.createStatement();) {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (username VARCHAR(10) NOT NULL,"
					+ "password VARCHAR(60) NOT NULL," + "email VARCHAR(20) NOT NULL," + "fullName VARCHAR(40) NOT NULL," + "PRIMARY KEY (username))";
			stmt.executeUpdate(sql);
		} 
	}

    //Fetch a user from the database based on a given username
	@Override
	public User getUser(String username) throws SQLException {
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";

		try (Connection connection = Database.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					return user;
				}
				return null;
			} 
		}
	}

    //Creating a user in the database given email, full name, username and password
	@Override
	public User createUser(String email, String fullName, String username, String password) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); //Hashing the password

		String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?)";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, hashedPassword);
            stmt.setString(3, email);
            stmt.setString(4, fullName);

			stmt.executeUpdate();
			return new User(username, null);
		} 
	}

    //Checking if there is already a user with the same username in the database already
    @Override
    public boolean checkUsernameDuplicate(String username) throws SQLException{
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";

        try (Connection connection = Database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
    }

    //This is getting the hashed password from the database so that Authmanager can compare it with the plaintext
    @Override
    public String getHashedPassword(String username) throws SQLException {
        String sql = "SELECT password FROM " + TABLE_NAME + " WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
                return null;
            }
        }
    }

    //Changing the password of a user in the database using the username.
    @Override
    public boolean changePassword(String password, String username) throws SQLException{
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "UPDATE " + TABLE_NAME + " SET password = ? WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        }
    }

}
