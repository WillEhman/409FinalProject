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
 * @since April 6, 2018
 * @version 1.0
 * 
 */
public class ProfessorGUI extends PageNavigator {

	/**
	 * client currently in use
	 */
	private Client client;

	/**
	 * current Professor in use
	 */
	private Professor professor;

	/**
	 * buttons to add and remove courses
	 */
	private JButton add, remove;

	/**
	 * constructor for ProfessorGUI
	 * 
	 * @param user
	 *            is user of ProfessorGUI
	 * @param client
	 *            is client aggregated by ProfessorGUI
	 */
	public ProfessorGUI(User user, Client client) {
		super(client);
		this.client = client;
		// Set up GUI with professor name
		setCourseListener(new CourseListListener(this));
		setBoxListener(new BoxListener(this));
		professor = new Professor(user);
		super.setFrameText("Course Manager 2018: " + professor.getFirstName() + " " + professor.getLastName());

		// Retrieve course list for the professor
		System.out.println("Creating Message");
		Message<Professor> message = new Message<Professor>(professor, "COURSELIST");
		System.out.println("Sending Message");
		Message<?> recieve = client.communicate(message);
		setCourses((Vector<Course>) recieve.getObject());
		System.out.println("Got Message");

		// Create button to add new courses
		add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// New window and fields for when course is getting added
				JFrame options = new JFrame("New Course");
				JTextField cidF = new JTextField(5);
				JTextField cNameF = new JTextField(5);
				JPanel title = new JPanel();
				JPanel info = new JPanel();
				JPanel buttons = new JPanel();
				options.setLayout(new BorderLayout());
				title.add(new JLabel("Create a New Course"));
				info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
				info.add(new JLabel("Course ID:"));
				info.add(cidF);
				info.add(new JLabel("Course Name:"));
				info.add(cNameF);
				options.add("North", title);
				options.add("Center", info);

				// Button to confirm addition of new course
				JButton confirm = new JButton("Confirm");

				// Appending course list when new course is confirmed
				confirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							if (cidF.getText().length() == 3) {
								Course newCourse = new Course(Integer.parseInt(cidF.getText()), professor.getId(),
										cNameF.getText(), true);
								Message<Course> message = new Message<Course>(newCourse, "ADDCOURSE");
								Message<?> recieve = client.communicate(message);
								if (recieve.getQuery().equals("Success")) {
									setCourses((Vector<Course>) recieve.getObject());
									options.dispose();
								} else {
									JOptionPane.showMessageDialog(null, "Error Adding Course: Name or ID in Use");
								}
							} else {
								JOptionPane.showMessageDialog(null, "Invalid Course ID");
							}
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Invalid Course ID");
						}

					}
				});

				// Cancels addition of new course
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
				options.setSize(300, 200);
				options.setResizable(false);
				options.setLocationByPlatform(true);
				options.setVisible(true);
			}
		});

		// Button to remove selected course
		remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Course temp = getCurrentCourse();

					// confirmation window
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null, "Remove " + temp.toString() + "?",
							"Remove Course", dialogButton);

					// Remove the course
					if (dialogResult == 0) {
						Message<Course> message = new Message<Course>(temp, "REMOVECOURSE");
						Message<?> receive = client.communicate(message);
						setCourses((Vector<Course>) receive.getObject());
					}
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(null, "No Course Selected");
				} catch (ArrayIndexOutOfBoundsException e) {
					JOptionPane.showMessageDialog(null, "No Course Selected");
				}
			}
		});
		remove.setEnabled(false);
		remove.setAlignmentX(Component.CENTER_ALIGNMENT);
		add.setAlignmentX(Component.CENTER_ALIGNMENT);
		super.getCoursePanel().add(add);
		super.getCoursePanel().add(remove);
		super.frame.setLocationRelativeTo(null);
		super.setVisible(true);
	}

	/**
	 * @author William Ehman
	 * @author David Parkin
	 * @author Luke Kushneryk
	 * @since April 6, 2018
	 * @version 1.0 page displaying students currently enrolled in a course
	 */
	private class StudentPage extends JPanel {

		/**
		 * serial id
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * buttons, lists and panes for the window
		 */
		JPanel buttons;
		JList<String> info;
		JScrollPane scroll;
		JButton enroll, unenroll, search, refresh, eStudent, eAll;
		JRadioButton id, lName;
		ButtonGroup radios;

		/**
		 * Used to search for students
		 */
		JTextField searchBar;

		/**
		 * Vector of students
		 */
		private Vector<User> studentVector;

		/**
		 * currently selected student
		 */
		private User currentStudent;

		/**
		 * Constructor for StudentPage
		 * 
		 * @param c
		 *            client currently in use
		 * @param display
		 */
		public StudentPage(Client c, PageNavigator display) {

			// Initialize buttons
			buttons = new JPanel();
			radios = new ButtonGroup();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JList();
			scroll = new JScrollPane(info);

			// Button to enroll a new student in that course
			enroll = new JButton("Enroll");
			enroll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String ID;
					ID = JOptionPane.showInputDialog(null, "Student ID to Enroll?");
					try {
						Integer.parseInt(ID);
						Message<Course> message = new Message<Course>(getCurrentCourse(),
								"ENROLLSTUDENT.SPLITTER." + ID);
						Message<?> receive = c.communicate(message);
						if (receive.getQuery().equals("Success")) {
							setStudents((Vector<User>) receive.getObject());
						} else {
							JOptionPane.showMessageDialog(null, "Unable to enroll student with given ID");
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid ID");
					}
				}
			});

			// Unenrolls a student from a course
			unenroll = new JButton("Unenroll");
			unenroll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null, "Unenroll " + currentStudent.toString(),
							"Unenroll", dialogButton);
					if (dialogResult == 0) {
						Message<Course> message = new Message<Course>(getCurrentCourse(),
								"REMOVESTUDENT.SPLITTER." + currentStudent.getId());
						Message<?> receive = c.communicate(message);
						setStudents((Vector<User>) receive.getObject());
					}
				}
			});

			// Search for students in a course
			search = new JButton("Search");
			search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					// Search by id
					if (id.isSelected()) {
						try {
							Integer.parseInt(searchBar.getText());
							if (searchBar.getText() != null) {
								Message<Course> message = new Message<Course>(getCurrentCourse(),
										"SEARCHSTUDENTSID.SPLITTER." + searchBar.getText());
								Message<?> receive = c.communicate(message);
								setStudents((Vector<User>) receive.getObject());
							}
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Invalid ID");
						}
					} else if (lName.isSelected()) {
						// Search by name

						if (searchBar.getText() != null) {
							Message<Course> message = new Message<Course>(getCurrentCourse(),
									"SEARCHSTUDENTSLN.SPLITTER." + searchBar.getText());
							Message<?> receive = c.communicate(message);
							setStudents((Vector<User>) receive.getObject());
						}
					}
				}
			});

			// Shows all students after a search
			refresh = new JButton("Show All");
			refresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Message<Course> message = new Message<Course>(getCurrentCourse(), "STUDENTLIST");
					Message<?> receive = c.communicate(message);
					setStudents((Vector<User>) receive.getObject());
				}
			});

			// Allows for selection of an object
			info.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (info.getSelectedIndex() >= 0) {
						currentStudent = studentVector.get(info.getSelectedIndex());
						setButtons();
					}
				}
			});

			// Button to email selected student
			eStudent = new JButton("Email Student");
			eStudent.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mail(currentStudent.getEmailAddress(), professor.getEmailAddress(), c);
				}
			});

			// Button to email whole class
			eAll = new JButton("Email Class");
			eAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mail("All", professor.getEmailAddress(), c);
				}
			});

			// Clean up any changed variables, finish construction
			eStudent.setEnabled(false);
			unenroll.setEnabled(false);
			searchBar = new JTextField(10);
			id = new JRadioButton("ID");
			lName = new JRadioButton("LastName");
			id.setSelected(true);
			radios.add(id);
			radios.add(lName);
			buttons.add(id);
			buttons.add(lName);
			buttons.add(searchBar);
			buttons.add(search);
			buttons.add(refresh);
			buttons.add(enroll);
			buttons.add(unenroll);
			buttons.add(eStudent);
			buttons.add(eAll);
			this.add("South", buttons);
			this.add("Center", scroll);
			Message<Course> message = new Message<Course>(getCurrentCourse(), "STUDENTLIST");
			Message<?> receive = c.communicate(message);
			setStudents((Vector<User>) receive.getObject());
		}

		/**
		 * Sets students enrolled in class from list of users
		 * 
		 * @param v
		 *            is list of users
		 */
		public void setStudents(Vector<User> v) {
			studentVector = v;
			if (studentVector.size() > 0) {
				String[] temp = new String[v.size()];
				for (int i = 0; i < temp.length; i++) {
					temp[i] = v.get(i).toString();
					System.out.println(temp[i]);
				}
				info.setListData(temp);
				try {
					currentStudent = v.get(0);
				} catch (ArrayIndexOutOfBoundsException e) {
					currentStudent = null;
				}
			} else {
				String[] temp = { "No Students Found" };
				info.setListData(temp);
			}
		}

		/**
		 * enables/disables buttons
		 */
		public void setButtons() {
			if (currentStudent != null) {
				eStudent.setEnabled(true);
				unenroll.setEnabled(true);
			} else {
				eStudent.setEnabled(false);
				unenroll.setEnabled(false);
			}
		}

		/**
		 * Sends an email
		 * 
		 * @param to
		 *            is email address of receiver
		 * @param from
		 *            is email address of sender
		 * @param c
		 *            is client sending email to server
		 */
		public void mail(String to, String from, Client c) {
			// Button to send the email
			JFrame options = new JFrame("Send an Email");

			// Initialize text fields for email data
			JTextField subF = new JTextField(5);
			JTextField toF = new JTextField(to, 5);
			JTextField fromF = new JTextField(from, 5);
			JTextField contF = new JTextField(5);
			JPasswordField passF = new JPasswordField(5);

			// Additional GUI display panels
			JPanel title = new JPanel();
			JPanel info = new JPanel();
			JPanel buttons = new JPanel();

			// Layout and display panels in GUI
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

			// Button that sends email
			JButton send = new JButton();
			send.setText("Send");
			send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (subF.getText() != null && contF.getText() != null) {
						ArrayList<String> toArray = new ArrayList<String>();
						if (to.equals("All")) {
							for (int i = 0; i < studentVector.size(); i++) {
								toArray.add(studentVector.get(i).getEmailAddress());
							}
						} else {
							toArray.add(to);
						}
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

			// Button to cancel email
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

	/**
	 * 
	 * @author William Ehman
	 * @author David Parkin
	 * @author Luke Kushneryk
	 * @since April 6, 2018
	 * @version 1.0
	 * 
	 */
	private class AssignmentPage extends JPanel {

		/**
		 * serial id
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Standard panel and button setup
		 */
		JPanel buttons, main;
		JList<String> info, sub;
		JScrollPane aScroll, sScroll;
		JButton upload, active, downloadSub, grade;

		/**
		 * Selected assignment and submission
		 */
		private Assignment currentAssignment;
		private Submission currentSub;

		/**
		 * Vectors of all assignments and submissions for the course
		 */
		private Vector<Assignment> assignVector;
		private Vector<Submission> subVector;

		/**
		 * Displays assignments
		 * 
		 * @param c
		 *            is client in use
		 */
		public AssignmentPage(Client c) {
			// Set up buttons and panels in frame
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			main = new JPanel();
			main.setLayout(new BoxLayout(main, BoxLayout.X_AXIS));
			this.setLayout(new BorderLayout());
			info = new JList();
			sub = new JList();
			aScroll = new JScrollPane(info);
			sScroll = new JScrollPane(sub);

			// Button to upload assignment
			upload = new JButton("Upload Assignment");
			upload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// Frame to add assignment
					JFrame options = new JFrame("New Assignment");
					JTextField aidF = new JTextField(5);
					JTextField titleF = new JTextField(5);
					JTextField pathF = new JTextField(5);
					JTextField dueF = new JTextField(5);
					JPanel title = new JPanel();
					JPanel info = new JPanel();
					JPanel buttons = new JPanel();
					options.setLayout(new BorderLayout());
					title.add(new JLabel("Create a New Assignment"));
					info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
					info.add(new JLabel("Assignment Number:"));
					aidF.setText("" + (assignVector.size() + 1));
					aidF.setEnabled(false);
					info.add(aidF);
					info.add(new JLabel("Assignment Title:"));
					info.add(titleF);
					info.add(new JLabel("Due Date:"));
					info.add(dueF);
					info.add(new JLabel("File Path:"));
					info.add(pathF);
					options.add("North", title);
					options.add("Center", info);

					// Button to confirm new assignment upload
					JButton confirm = new JButton("Confirm");
					confirm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								if (aidF.getText().length() <= 10) {
									Assignment newAssign = new Assignment(Integer.parseInt(aidF.getText()),
											getCurrentCourse().getCourseId(), titleF.getText(), pathF.getText(), true,
											dueF.getText(), readFileContent(pathF.getText()));
									String[] path = pathF.getText().split("\\.(?=[^\\.]+$)");
									try {
										Message<Assignment> message = new Message<Assignment>(newAssign,
												"CREATEFILE.SPLITTER." + path[path.length - 2] + ".SPLITTER."
														+ path[path.length - 1]);
										Message<?> receive = c.communicate(message);
										if (receive.getQuery().equals("Success")) {
											setAssignments((Vector<Assignment>) receive.getObject());
											options.dispose();
										} else {
											JOptionPane.showMessageDialog(null, "Error Uploading File");
										}
									} catch (ArrayIndexOutOfBoundsException e) {
										e.printStackTrace();
									}
								} else {
									JOptionPane.showMessageDialog(null, "Invalid Course ID");
								}
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Invalid Course ID");
							} catch (IOException e) {
								JOptionPane.showMessageDialog(null, "Invalid File Path");
							} catch (NullPointerException e) {
								JOptionPane.showMessageDialog(null, "Invalid Input");
							}

						}
					});

					// Cancel addition of new assignment
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
					options.setSize(300, 300);
					options.setResizable(false);
					options.setLocationByPlatform(true);
					options.setVisible(true);
				}
			});

			// Select a list
			info.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (info.getSelectedIndex() >= 0) {
						currentAssignment = assignVector.get(info.getSelectedIndex());
						Message<Assignment> message = new Message<Assignment>(currentAssignment, "SUBMISSIONPROFLIST");
						Message<?> receive = c.communicate(message);
						setSubmissions((Vector<Submission>) receive.getObject());
						setButtons();
					}
				}
			});

			// Select a submission
			sub.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (sub.getSelectedIndex() >= 0) {
						currentSub = subVector.get(sub.getSelectedIndex());
						System.out.println("TEST");
					}
				}
			});

			// Switches course between being active and inactive
			active = new JButton("Toggle Active");
			active.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Change Assignment Active State to " + !currentAssignment.isActive(), "Toggle Active State",
							dialogButton);
					if (dialogResult == 0) {
						currentAssignment.setActive(!currentAssignment.isActive());
						Message<Assignment> message = new Message<Assignment>(currentAssignment, "ACTIVATEASSIGNMENT");
						try {
							Message<?> receive = c.communicate(message);
							setAssignments((Vector<Assignment>) receive.getObject());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			active.setEnabled(false);

			// Button to download submissions
			downloadSub = new JButton("Download Submission");
			downloadSub.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Message<Submission> message = new Message<Submission>(currentSub, "VIEWSUBMISSION");
					Message<?> receive = c.communicate(message);
					if (receive.getQuery().equals("Success")) {
						try {
							writeFileContent((byte[]) receive.getObject(), currentSub);
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null, "Error Downloading File");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Error Downloading File");
					}
				}
			});
			downloadSub.setEnabled(false);

			// Sets a grade for a submission
			grade = new JButton("Set Grade");
			grade.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// New frame for grade addition with comment
					JFrame options = new JFrame("New Course");
					JTextField gradeF = new JTextField("" + currentSub.getGrade(), 5);
					JTextField commF = new JTextField(currentSub.getComment(), 5);
					JPanel title = new JPanel();
					JPanel info = new JPanel();
					JPanel buttons = new JPanel();
					options.setLayout(new BorderLayout());
					title.add(new JLabel("Grade Submission"));
					info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
					info.add(new JLabel("Grade:"));
					info.add(gradeF);
					info.add(new JLabel("Comment:"));
					info.add(commF);
					options.add("North", title);
					options.add("Center", info);

					// Confirms grade submission and changes grade assiciated with assignment
					JButton confirm = new JButton("Confirm");
					confirm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								currentSub.setGrade(Integer.parseInt(gradeF.getText()));
								currentSub.setComment(commF.getText());
								Message<Submission> message = new Message<Submission>(currentSub, "SETGRADE");
								Message<?> receive = c.communicate(message);
								setSubmissions((Vector<Submission>) receive.getObject());
								options.dispose();
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Invalid Grade");
							}
						}
					});

					// Cancels grade change
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
					options.setSize(300, 200);
					options.setResizable(false);
					options.setLocationByPlatform(true);
					options.setVisible(true);
				}
			});

			// Changes GUI back to the way it was
			grade.setEnabled(false);
			buttons.add(upload);
			buttons.add(active);
			buttons.add(downloadSub);
			buttons.add(grade);
			main.add(aScroll);
			main.add(sScroll);
			this.add("South", buttons);
			this.add("Center", main);
			Message<Course> message = new Message<Course>(getCurrentCourse(), "ASSIGNMENTLISTPROF");
			Message<?> receive = c.communicate(message);
			setAssignments((Vector<Assignment>) receive.getObject());
		}

		/**
		 * Reads content of a file
		 * 
		 * @param path
		 * @return file content from file within path
		 * @throws IOException
		 * @throws FileNotFoundException
		 */
		public byte[] readFileContent(String path) throws IOException, FileNotFoundException {
			File selectedFile = new File(path);
			long length = selectedFile.length();
			byte[] content = new byte[(int) length];

			try {
				FileInputStream fis = new FileInputStream(selectedFile);
				BufferedInputStream bos = new BufferedInputStream(fis);
				bos.read(content, 0, (int) length);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Unable to Find File");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return content;

		}

		/**
		 * Writes to a file
		 * 
		 * @param input
		 * @param sub
		 * @throws IOException
		 */
		void writeFileContent(byte[] input, Submission sub) throws IOException {
			File newFile = new File(sub.getPath());
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

		/**
		 * Setters and getters
		 */
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
			if (currentSub != null) {
				downloadSub.setEnabled(true);
				grade.setEnabled(true);
			} else {
				downloadSub.setEnabled(false);
				grade.setEnabled(false);
			}
			if (currentAssignment != null) {
				active.setEnabled(true);
			} else {
				active.setEnabled(false);
			}
		}
	}

	/**
	 * 
	 * @author William Ehman
	 * @author David Parkin
	 * @author Luke Kushneryk
	 * @since April 6, 2018
	 * @version 1.0
	 * 
	 */
	public class HomePage extends JPanel {

		/**
		 * serial id
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * standard panel and buttons for homepage
		 */
		JPanel buttons;
		JTextArea info;
		JScrollPane scroll;
		JButton active;

		/**
		 * Constructor for HomePage
		 * 
		 * @param c
		 *            is client being used
		 */
		public HomePage(Client c) {
			display(c);
		}

		/**
		 * Displays panels and buttons
		 * 
		 * @param c
		 *            is client being used
		 */
		public void display(Client c) {
			// Buttons
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JTextArea();
			info.setText("Course: " + getCurrentCourse().getCourseName() + "\n" + "Course ID: "
					+ getCurrentCourse().getCourseId() + "\n" + "Active: " + getCurrentCourse().isActive());
			scroll = new JScrollPane(info);
			// Toggles course active
			active = new JButton("Toggle Active");
			active.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Course temp = getCurrentCourse();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Change Course Active State to " + !temp.isActive(), "Toggle Active State", dialogButton);
					if (dialogResult == 0) {
						temp.setActive(!temp.isActive());
						Message<Course> message = new Message<Course>(temp, "TOGGLECOURSE");
						try {
							Message<?> receive = c.communicate(message);
							getCourses().setSelectedIndex(getCourseIndex());
							setCourses((Vector<Course>) receive.getObject(), getCourseIndex());
							display(c);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			buttons.add(active);
			this.add("South", buttons);
			this.add("Center", scroll);
		}
	}

	/**
	 * 
	 * @author William Ehman
	 * @author David Parkin
	 * @author Luke Kushneryk
	 * @since April 6, 2018
	 * @version 1.0
	 * 
	 *          Updates current selected course
	 * 
	 */
	private class CourseListListener implements ListSelectionListener {

		/**
		 * Display of pagenavigator
		 */
		PageNavigator display;

		/**
		 * Constructor
		 * 
		 * @param disp
		 */
		public CourseListListener(PageNavigator disp) {
			display = disp;
		}

		/**
		 * changes course displayed if value of selected course changed
		 */
		public void valueChanged(ListSelectionEvent e) {
			setCurrentCourse();
			setCourseName(getCurrentCourse().getCourseName());
			setComboBox(0);
			HomePage p = new HomePage(display.getClient());
			displayPage(p);
			if (getCurrentCourse().isActive()) {
				String[] options = { "Home", "Students", "Assignments", "Chatroom" };
				setSelections(options);
			} else {
				String[] options = { "Home" };
				setSelections(options);
			}
			if (getCurrentCourse() != null) {
				remove.setEnabled(true);
			} else {
				remove.setEnabled(false);
			}
		}
	}

	/**
	 * 
	 * @author William Ehman
	 * @author David Parkin
	 * @author Luke Kushneryk
	 * @since April 6, 2018
	 * @version 1.0
	 * 
	 *          Drop down menu listener
	 * 
	 */
	private class BoxListener implements ActionListener {

		/**
		 * display
		 */
		PageNavigator display;

		/**
		 * Constructor for BoxListener
		 * 
		 * @param disp
		 */
		public BoxListener(PageNavigator disp) {
			display = disp;
		}

		/**
		 * changes display when drop down selection changes
		 */
		public void actionPerformed(ActionEvent e) {
			JComboBox c = (JComboBox) e.getSource();
			String selected = (String) c.getSelectedItem();
			if (selected.equals("Home")) {
				HomePage p = new HomePage(display.getClient());
				displayPage(p);
			} else if (selected.equals("Assignments")) {
				AssignmentPage p = new AssignmentPage(display.getClient());
				displayPage(p);
			} else if (selected.equals("Students")) {
				StudentPage p = new StudentPage(display.getClient(), display);
				displayPage(p);
			} else if (selected.equals("Chatroom")) {
				ChatroomPage p = new ChatroomPage(professor, client, getCurrentCourse());
				displayPage(p);
			}
		}

	}
}
