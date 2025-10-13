package dao;

import java.sql.SQLException;

import model.User;

/**
 * A data access object (DAO) is a pattern that provides an abstract interface 
 * to a database or other persistence mechanism. 
 * the DAO maps application calls to the persistence layer and provides some specific data operations 
 * without exposing details of the database. 
 */
public interface UserDao {
	void setup() throws SQLException;
	User getUser(String username) throws SQLException;
	User createUser(String email, String fullName, String username, String password) throws SQLException;
    boolean checkUsernameDuplicate(String username) throws SQLException;
    String getHashedPassword(String username) throws SQLException;
    boolean changePassword(String password, String username) throws SQLException;
}
