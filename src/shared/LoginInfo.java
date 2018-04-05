package shared;

<<<<<<< HEAD
public class LoginInfo {
	
	private String username;
=======
import java.io.Serializable;

public class LoginInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private int username;
>>>>>>> origin/master
	private String password;
	
	public LoginInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
