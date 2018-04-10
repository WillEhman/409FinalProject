package shared;

import java.io.Serializable;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Contains login information class
 */

public class LoginInfo implements Serializable {

	/**
	 * serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * username of user
	 */
	private String username;

	/**
	 * password of user
	 */
	private String password;

	/**
	 * Constructor for loginInfo
	 */
	public LoginInfo() {
		this.username = null;
		this.password = null;
	}

	/**
	 *
	 * Constructor for loginInfo
	 * 
	 * @param username
	 * @param password
	 */

	public LoginInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * getters and setters
	 */
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
