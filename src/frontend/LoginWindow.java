package frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/*
 * Should username and password have setters?
 */

public class LoginWindow {
	private JTextField username;
	private JTextField password;

	private LoginWindow() {
		JFrame frame = new JFrame("Login");
		JPanel title = new JPanel();
		JPanel main = new JPanel();
		JPanel buttons = new JPanel();
		
		frame.setLayout(new BorderLayout());
		buttons.setLayout(new FlowLayout());
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		title.setLayout(new FlowLayout());
		
		JLabel titleLabel = new JLabel("Please Enter Your Username and Password");
		title.add(titleLabel);
		
		username = new JTextField();
		password = new JPasswordField();
		JLabel userLabel = new JLabel("Username: ");
		JLabel passLabel = new JLabel("Password: ");
		main.add(userLabel);
		main.add(username);
		main.add(passLabel);
		main.add(password);
		
		buttons.add(new JButton("Submit")); //TODO: add action listener to send username and pass to server
		
		frame.add("North", title);
		frame.add("Center", main);
		frame.add("South", buttons);
		frame.setResizable(false);
		frame.setSize(300,200);
		frame.setVisible(true);
	}
	
	public void login() {
		System.out.println("TEST");
	}

	public String getUsername() {
		return username.getText();
	}

	public String getPassword() {
		return password.getText();
	}
	
	public static void main(String[] args) {
		LoginWindow l = new LoginWindow();
		l.login();
	}

}
