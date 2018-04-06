package frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.*;

import shared.*;
import javax.swing.UIManager.*;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField username;
	private JPasswordField password;

	private Client client;
	JFrame frame = new JFrame("Login");

	public LoginWindow() throws IOException {

		 client = new Client("localhost", 9090);
//		client = new Client("10.13.170.186", 9090);
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

		JButton b = new JButton("Submit");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		buttons.add(b); // TODO: add action listener to send username and pass to server

		frame.add("North", title);
		frame.add("Center", main);
		frame.add("South", buttons);
		frame.setResizable(false);
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true);
	}

	private void login() {
		System.out.println("Logging in");
		LoginInfo info = new LoginInfo(username.getText(), password.getText());
		Message<LoginInfo> message = new Message<LoginInfo>(info, "LOGIN");
		Message<?> recieve = client.communicate(message);
		User user = (User) recieve.getObject();

		if (user == null) {
			JOptionPane.showMessageDialog(this, "Login failed to authenticate");
		} else if (user.getType().equalsIgnoreCase("P")) {
			JOptionPane.showMessageDialog(this, "Professor Login confirmed");
			frame.dispose();
			System.out.println("Professor Login confirmed");
			System.out.println("Professor ID: " + user.getId());
			ProfessorGUI p = new ProfessorGUI(user, client);
		} else if (user.getType().equalsIgnoreCase("S")) {
			// TODO
			// Create student gui
			JOptionPane.showMessageDialog(this, "Student Login confirmed");
			frame.dispose();
			System.out.println("Student Login confirmed");
			System.out.println("Student ID: " + user.getId());

		} else {
			// close window
		}
	}

	public static void main(String[] args) throws IOException {
            // Set cross-platform Java L&F (also called "Metal")
		

		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}

		LoginWindow l = new LoginWindow();
	}
}
