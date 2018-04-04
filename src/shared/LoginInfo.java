package shared;

import java.io.Serializable;

public class LoginInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private int username;
	private String password;
	
	public LoginInfo(int username, String password) {
		this.username = username;
		this.password = password;
	}

	public int getUsername() {
		return username;
	}

	public void setUsername(int username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
