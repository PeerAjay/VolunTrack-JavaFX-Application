package model;

public class User {
	private String username;
	private String password;
    private String role;

	public User() {
	}
	
	public User(String username, String password, String role) {
		this.username = username;
		this.password = password;
        this.role = role;
	}

    //Getters and setters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

    public String getRole() {
        return role;
    }

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public void setRole(String role) {
        this.role = role;
    }
}
