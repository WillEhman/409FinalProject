package frontend;

import javax.swing.*;

/*
 * Should username and password have setters?
 */

public class LoginWindow {
	private JTextField username;
	private JTextField password;

	private void login() {

	}

	public JTextField getUsername() {
		return username;
	}

	public void setUsername(JTextField username) {
		this.username = username;
	}

	public JTextField getPassword() {
		return password;
	}

	public void setPassword(JTextField password) {
		this.password = password;
	}

}
