package frontend;

import shared.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 *
 */
public class StudentGUI extends PageNavigator {

	private Client client;
	private Student student;
	private Professor currentProf;

	public StudentGUI(User user, Client client) {
		super(client);
		this.client = client;
		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Message<Student> message = new Message<Student>(student, "COURSELIST");
				Message<?> recieve = client.communicate(message);
				setCourses((Vector<Course>) recieve.getObject());
			}
		});
		setCourseListener(new CourseListListener(this));
		setBoxListener(new BoxListener(this));
		student = new Student(user);
		super.setFrameText("Course Manager 2018: " + student.getFirstName() + " " + student.getLastName());
		System.out.println("Creating Message");
		Message<Student> message = new Message<Student>(student, "COURSELIST");
		System.out.println("Sending Message");
		Message<?> recieve = client.communicate(message);
		setCourses((Vector<Course>) recieve.getObject());
		System.out.println("Got Message");
		refresh.setAlignmentX(Component.CENTER_ALIGNMENT);
		super.getCoursePanel().add(refresh);
		super.frame.setLocationRelativeTo(null);
		super.setVisible(true);
	}

	public class HomePage extends JPanel {

		private static final long serialVersionUID = 1L;
		JPanel buttons;
		JTextArea info;
		JScrollPane scroll;
		JButton email;

		public HomePage(Client c) {
			Message<Course> message = new Message<Course>(getCurrentCourse(), "GETPROF");
			Message<?> receive = c.communicate(message);
			currentProf = (Professor) receive.getObject();
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JTextArea();
			info.setText("Course: " + getCurrentCourse().getCourseName() + "\n" + "Course ID: "
					+ getCurrentCourse().getCourseId() + "\n" + "Professor: Dr. "
					+ currentProf.getFirstName() + " " + currentProf.getLastName());
			scroll = new JScrollPane(info);
			email = new JButton("Email Professor");
			email.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					 mail(currentProf.getEmailAddress(), student.getEmailAddress(), c);
				}
			});
			buttons.add(email);
			this.add("South", buttons);
			this.add("Center", scroll);
		}

		public void mail(String to, String from, Client c) {
			JFrame options = new JFrame("Send an Email");
			JTextField subF = new JTextField(5);
			JTextField toF = new JTextField(to, 5);
			JTextField fromF = new JTextField(from, 5);
			JTextField contF = new JTextField(5);
			JPasswordField passF = new JPasswordField(5);
			JPanel title = new JPanel();
			JPanel info = new JPanel();
			JPanel buttons = new JPanel();
			options.setLayout(new BoxLayout(options.getContentPane(), BoxLayout.Y_AXIS));
			title.add(new JLabel("Send a New Email"));
			info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
			info.add(new JLabel("Subject:"));
			info.add(subF);
			info.add(new JLabel("To:"));
			info.add(toF);
			info.add(new JLabel("From:"));
			info.add(fromF);
			info.add(new JLabel("Content:"));
			info.add(contF);
			info.add(new JLabel("Email Password:"));
			info.add(passF);
			options.add(title);
			options.add(info);
			JButton send = new JButton();
			send.setText("Send");
			send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (subF.getText() != null && contF.getText() != null) {
						ArrayList<String> toArray = new ArrayList<String>();
						 toArray.add(to);
						Email temp = new Email(from, toArray, subF.getText(), contF.getText(), passF.getText());
						Message<Email> message = new Message<Email>(temp, "SENDEMAIL");
						Message<?> recieve = c.communicate(message);
						String check = (String) recieve.getQuery();
						if (check.equals("Success")) {
							JOptionPane.showMessageDialog(null, "Message Sent");
							options.dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Message Failed to Send");
						}
					}
				}
			});
			JButton cancel = new JButton();
			cancel.setText("Cancel");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					options.dispose();
				}
			});
			toF.setEnabled(false);
			fromF.setEnabled(false);
			buttons.setLayout(new FlowLayout());
			buttons.add(send);
			buttons.add(cancel);
			options.add(buttons);
			options.setSize(400, 400);
			options.setVisible(true);
		}
	}

	private class AssignmentPage extends JPanel {

		private static final long serialVersionUID = 1L;
		JPanel buttons, main;
		JList<String> info, sub;
		JScrollPane aScroll, sScroll;
		JButton upload, download;
		private Assignment currentAssignment;
		private Submission currentSub;
		private Vector<Assignment> assignVector;
		private Vector<Submission> subVector;

		public AssignmentPage(Client c) {
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
			this.setLayout(new BorderLayout());
			info = new JList();
			sub = new JList();
			aScroll = new JScrollPane(info);
			sScroll = new JScrollPane(sub);
			upload = new JButton("Upload Submission");
			upload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFrame options = new JFrame("New Submission");
					JTextField titleF = new JTextField(5);
					JTextField pathF = new JTextField(5);
					JTextField dueF = new JTextField(5);
					JPanel title = new JPanel();
					JPanel info = new JPanel();
					JPanel buttons = new JPanel();
					options.setLayout(new BorderLayout());
					title.add(new JLabel("Submit a file"));
					info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
					info.add(new JLabel("Submission Title:"));
					info.add(titleF);
					info.add(new JLabel("Date Submitted:"));
					dueF.setText(LocalDateTime.now().toString());
					dueF.setEnabled(false);
					info.add(dueF);
					info.add(new JLabel("File Path:"));
					info.add(pathF);
					options.add("North", title);
					options.add("Center", info);
					JButton confirm = new JButton("Confirm");
					confirm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								Submission newSub = new Submission(getCurrentCourse().getCourseId(),
										currentAssignment.getAssignId(), student.getId(), pathF.getText(), 0, " ",
										titleF.getText(), dueF.getText(), readFileContent(pathF.getText()));
								String[] path = pathF.getText().split("\\.(?=[^\\.]+$)");
								Message<Submission> message = new Message<Submission>(newSub,
										"UPLOADSUBMISSION.SPLITTER." + path[path.length - 2] + ".SPLITTER."
												+ path[path.length - 1]);
								Message<?> receive = c.communicate(message);
								setSubmissions((Vector<Submission>) receive.getObject());
								setButtons();
								options.dispose();
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Invalid Course ID");
							} catch (IOException e) {
								JOptionPane.showMessageDialog(null, "Invalid File Path");
							} catch (NullPointerException e) {
								JOptionPane.showMessageDialog(null, "Invalid Input");
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
					buttons.add(confirm);
					buttons.add(cancel);
					options.add("South", buttons);
					options.setSize(300, 250);
					options.setResizable(false);
					options.setLocationByPlatform(true);
					options.setVisible(true);
				}
			});
			info.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (info.getSelectedIndex() >= 0) {
						currentAssignment = assignVector.get(info.getSelectedIndex());
						Message<Assignment> message = new Message<Assignment>(currentAssignment,
								"SUBMISSIONLIST.SPLITTER." + student.getId());
						Message<?> receive = c.communicate(message);
						setSubmissions((Vector<Submission>) receive.getObject());
						setButtons();
					}
				}
			});
			sub.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (sub.getSelectedIndex() >= 0) {
						currentSub = subVector.get(sub.getSelectedIndex());
						setButtons();
					}
				}
			});
			download = new JButton("Download Assignment");
			download.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Message<Assignment> message = new Message<Assignment>(currentAssignment, "READFILE");
					Message<?> receive = c.communicate(message);
					try {
						writeFileContent((byte[]) receive.getObject(), currentAssignment);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			download.setEnabled(false);
			upload.setEnabled(false);
			buttons.add(download);
			buttons.add(upload);
			main.add(aScroll);
			main.add(sScroll);
			this.add("South", buttons);
			this.add("Center", main);
			Message<Course> message = new Message<Course>(getCurrentCourse(), "ASSIGNMENTLISTSTUDENT");
			Message<?> receive = c.communicate(message);
			setAssignments((Vector<Assignment>) receive.getObject());
		}

		public void setAssignments(Vector<Assignment> v) {
			assignVector = v;
			String[] temp = new String[v.size()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = v.get(i).toString();
				System.out.println(temp[i]);
			}
			info.setListData(temp);
			try {
				currentAssignment = v.get(0);
			} catch (ArrayIndexOutOfBoundsException e) {
				currentAssignment = null;
			}
		}

		public void setSubmissions(Vector<Submission> v) {
			subVector = v;
			String[] temp = new String[v.size()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = v.get(i).toString();
				System.out.println(temp[i]);
			}
			sub.setListData(temp);
			try {
				currentSub = v.get(0);
			} catch (ArrayIndexOutOfBoundsException e) {
				currentSub = null;
			}
		}

		public void setButtons() {
			if (currentAssignment != null) {
				download.setEnabled(true);
				upload.setEnabled(true);
			} else {
				download.setEnabled(false);
				upload.setEnabled(false);
			}
		}

		public byte[] readFileContent(String path) throws IOException, FileNotFoundException {
			File selectedFile = new File(path);
			long length = selectedFile.length();
			byte[] content = new byte[(int) length];

			try {
				FileInputStream fis = new FileInputStream(selectedFile);
				BufferedInputStream bos = new BufferedInputStream(fis);
				bos.read(content, 0, (int) length);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return content;

		}

		void writeFileContent(byte[] input, Assignment assign) throws IOException {
			File newFile = new File(assign.getPath());
			try {
				if (!newFile.exists()) {
					newFile.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(newFile);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(input);
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class CourseListListener implements ListSelectionListener {

		PageNavigator display;

		public CourseListListener(PageNavigator disp) {
			display = disp;
		}

		public void valueChanged(ListSelectionEvent e) {
			setCurrentCourse();
			setCourseName(getCurrentCourse().getCourseName());
			setComboBox(0);
			HomePage p = new HomePage(display.getClient());
			displayPage(p);
			if (getCurrentCourse().isActive()) {
				String[] options = { "Home", "Assignments" };
				setSelections(options);
			} else {
				String[] options = { "Home" };
				setSelections(options);
			}
		}
	}

	private class BoxListener implements ActionListener {

		PageNavigator display;

		public BoxListener(PageNavigator disp) {
			display = disp;
		}

		public void actionPerformed(ActionEvent e) {
			JComboBox c = (JComboBox) e.getSource();
			String selected = (String) c.getSelectedItem();
			if (selected.equals("Home")) {
				HomePage p = new HomePage(display.getClient());
				displayPage(p);
			} else if (selected.equals("Assignments")) {
				AssignmentPage p = new AssignmentPage(display.getClient());
				displayPage(p);
			} 
		}
	}
}
