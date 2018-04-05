package frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.*;

import shared.*;

/*
 * Should username and password have setters?
 */

public class LoginWindow extends JFrame {

	private JTextField username;
	private JTextField password;

	private Client client;
	JFrame frame = new JFrame("Login");

	public LoginWindow() throws IOException {

		client = new Client("localhost", 9090);
		// Client client = new Client("10.13.166.195", 9090);
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
		password = new JTextField();
		JLabel userLabel = new JLabel("Username: ");
		JLabel passLabel = new JLabel("Password: ");
		main.add(userLabel);
		main.add(username);
		main.add(passLabel);
		main.add(password);

		JButton b = new JButton("Submit");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				l.setUsername(username.getText());
//				l.setPassword(password.getText());
				// System.out.println(l.getUsername());
				// System.out.println(l.getPassword());
//				disposeFrame();
				login();
			}
		});
		buttons.add(b); // TODO: add action listener to send username and pass to server

		frame.add("North", title);
		frame.add("Center", main);
		frame.add("South", buttons);
		frame.setResizable(false);
		frame.setSize(300, 200);
		// while (username.getText() != null) {
		// frame.dispose();
		// }
		frame.setVisible(true);
	}

	
	private void login()
    {
		System.out.println("Logging in");
    	LoginInfo info = new LoginInfo(username.getText(), password.getText());
    	Message<LoginInfo> message = new Message<LoginInfo>(info, "LOGIN");
    	Message<?> recieve = client.communicate(message);
    	User user = (User) recieve.getObject();
//    	System.out.println(user);
//    	System.out.println(user.getType());
    	
    	
    	if(user == null)
    	{
    		JOptionPane.showMessageDialog(this,"Login failed to authenticate");
    	}
    	else if(user.getType().equalsIgnoreCase("P"))
    	{
    		//TODO
            //Create professor gui
    		JOptionPane.showMessageDialog(this,"Professor Login confirmed");
    		System.out.println("Professor Login confirmed");
    	}
    	else if(user.getType().equalsIgnoreCase("S"))
    	{
    		//TODO
            //Create student gui
    		JOptionPane.showMessageDialog(this,"Student Login confirmed");
    		System.out.println("Student Login confirmed");

    	}
    	else
    	{
    		//close window
    	}
    }
	
	public static void main(String [] args) throws IOException{
        LoginWindow l= new LoginWindow();
    }
	// public LoginInfo getLoginInfo() {
	// return l;
	// }

//	public void disposeFrame() {
//		frame.dispose();
//	}

	// public static void main(String[] args) {
	// LoginWindow l = new LoginWindow();
	// }

}
