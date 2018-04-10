package frontend;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Vector;

import javax.swing.*;

import shared.*;
import javax.swing.UIManager.*;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 *
 */
public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField username;
	private JPasswordField password;

	private Client client;
	JFrame frame;

	public LoginWindow() throws IOException {

		// client = new Client("localhost", 9090);
		client = new Client("10.13.190.9", 9090);
		frame = new JFrame("Login");
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

		JButton register = new JButton("Register");
		register.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame options = new JFrame("New Account");
				JTextField userF = new JTextField(5);
				JPasswordField passF = new JPasswordField(5);
				JTextField fNameF = new JTextField(5);
				JTextField lNameF = new JTextField(5);
				JTextField emailF = new JTextField(5);
				JPasswordField adminF = new JPasswordField(5);
				JPanel title = new JPanel();
				JPanel info = new JPanel();
				JPanel buttons = new JPanel();
				JRadioButton prof, student;
				ButtonGroup radios = new ButtonGroup();
				prof = new JRadioButton("Professor");
				student = new JRadioButton("Student");
				student.setSelected(true);
				radios.add(prof);
				radios.add(student);
				options.setLayout(new BorderLayout());
				title.add(new JLabel("Create a New Account"));
				info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
				info.add(new JLabel("First Name:"));
				info.add(fNameF);
				info.add(new JLabel("Last Name:"));
				info.add(lNameF);
				info.add(new JLabel("Email:"));
				info.add(emailF);
				info.add(new JLabel("Username:"));
				info.add(userF);
				info.add(new JLabel("Password:"));
				info.add(passF);
				info.add(new JLabel("Admin Password:"));
				info.add(adminF);
				options.add("North", title);
				options.add("Center", info);
				JButton confirm = new JButton("Confirm");
				confirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							if (student.isSelected()) {
								Student newStudent = new Student(999999, fNameF.getText(), lNameF.getText(),
										emailF.getText());
								Message<Student> message = new Message<Student>(newStudent,
										"ADDUSER.SPLITTER." + userF.getText() + ".SPLITTER." + passF.getText()
												+ ".SPLITTER." + adminF.getText());
								Message<?> recieve = client.communicate(message);
								if (recieve.getQuery().equals("Success")) {
									JOptionPane.showMessageDialog(null, "Successfully added new user!");
									options.dispose();
								} else {
									JOptionPane.showMessageDialog(null, "Error when adding user, contact Admin");
								}
							} else {
								Professor newProf = new Professor(999999, fNameF.getText(), lNameF.getText(),
										emailF.getText());
								Message<Professor> message = new Message<Professor>(newProf,
										"ADDUSER.SPLITTER." + userF.getText() + ".SPLITTER." + passF.getText()
												+ ".SPLITTER." + adminF.getText());
								Message<?> recieve = client.communicate(message);
								if (recieve.getQuery().equals("Success")) {
									JOptionPane.showMessageDialog(null, "Successfully added new user!");
									options.dispose();
								} else {
									JOptionPane.showMessageDialog(null, "Error when adding user, contact Admin");
								}
							}
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Invalid Course ID");
						}

					}
				});
				JButton cancel = new JButton("Cancel");
				cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						options.dispose();
					}
				});
				buttons.setLayout(new FlowLayout());
				buttons.add(student);
				buttons.add(prof);
				buttons.add(confirm);
				buttons.add(cancel);
				options.add("South", buttons);
				options.setSize(350, 450);
				options.setResizable(false);
				options.setLocationByPlatform(true);
				options.setVisible(true);
			}
		});

		buttons.add(b);
		buttons.add(register);
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
			JOptionPane.showMessageDialog(this, "Student Login confirmed");
			frame.dispose();
			System.out.println("Student Login confirmed");
			System.out.println("Student ID: " + user.getId());
			StudentGUI p = new StudentGUI(user, client);

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
