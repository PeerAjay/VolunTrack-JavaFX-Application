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

}
