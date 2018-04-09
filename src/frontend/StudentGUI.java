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
	private boolean isProfessor;

	public StudentGUI(User user, Client client) {
		super(client);
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
		isProfessor = false;
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
		JButton active;

		public HomePage(Client c) {
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JTextArea();
			info.setText("Course: " + getCurrentCourse().getCourseName() + "\n" + "Course ID: "
					+ getCurrentCourse().getCourseId() + "\n" + "Active: " + getCurrentCourse().isActive());
			scroll = new JScrollPane(info);
			active = new JButton("Toggle Active");
			active.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Course temp = getCurrentCourse();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Change Course Active State to " + !temp.isActive(), "Toggle Active State", dialogButton);
					if (dialogResult == 0) {
						temp.setActive(!temp.isActive());
						Message<Course> message = new Message<Course>(temp, "UPDATECOURSE");
						try {
							Message<?> receive = c.communicate(message);
							setCourses((Vector<Course>) receive.getObject());
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
	
	private class AssignmentPage extends JPanel {

		private static final long serialVersionUID = 1L;
		JPanel buttons, main;
		JList<String> info, sub;
		JScrollPane aScroll, sScroll;
		JButton upload, active, download, grade;
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
			upload = new JButton("Upload Assignment");
			upload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFrame options = new JFrame("New Submission");
					JTextField aidF = new JTextField(5);
					JTextField titleF = new JTextField(5);
					JTextField pathF = new JTextField(5);
					JTextField dueF = new JTextField(5);
					JPanel title = new JPanel();
					JPanel info = new JPanel();
					JPanel buttons = new JPanel();
					options.setLayout(new BorderLayout());
					title.add(new JLabel("Submit a file"));
					info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
					info.add(new JLabel("Submission ID:"));
					info.add(aidF);
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
								if (aidF.getText().length() <= 10) {
									Submission newSub = new Submission(getCurrentCourse().getCourseId(),
											currentAssignment.getAssignId(), student.getId(), pathF.getText(), 0, null,
											dueF.getText(), titleF.getText(), readFileContent(pathF.getText()));
									String[] path = pathF.getText().split("\\.(?=[^\\.]+$)");
									Message<Submission> message = new Message<Submission>(newSub,
											"CREATEFILE.SPLITTER." + path[path.length - 2] + ".SPLITTER."
													+ path[path.length - 1]);
									Message<?> receive = c.communicate(message);
									setSubmissions((Vector<Submission>) receive.getObject());
									options.dispose();
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
			info.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (info.getSelectedIndex() >= 0) {
						currentAssignment = assignVector.get(info.getSelectedIndex());
						Message<Assignment> message = new Message<Assignment>(currentAssignment, "SUBMISSIONLIST");
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
						System.out.println("TEST");
					}
				}
			});
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
			download = new JButton("Download Submission");
			download.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Message<Submission> message = new Message<Submission>(currentSub, "VIEWSUBMISSION");
					Message<?> receive = c.communicate(message);
					try {
						writeFileContent((byte[]) receive.getObject(), currentSub);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			download.setEnabled(false);
			grade = new JButton("Set Grade");
			grade.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFrame options = new JFrame("New Course");
					JTextField gradeF = new JTextField(""+currentSub.getGrade(), 5);
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
					JButton confirm = new JButton("Confirm");
					confirm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								currentSub.setGrade(Integer.parseInt(gradeF.getText()));
								currentSub.setComment(commF.getText());
								Message<Submission> message = new Message<Submission>(currentSub,"SETGRADE");
								Message<?> receive = c.communicate(message);
								setSubmissions((Vector<Submission>) receive.getObject());
								options.dispose();
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Invalid Grade");
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
					options.setSize(300, 200);
					options.setResizable(false);
					options.setLocationByPlatform(true);
					options.setVisible(true);
				}
			});
			grade.setEnabled(false);
			buttons.add(upload);
			buttons.add(active);
			buttons.add(download);
			buttons.add(grade);
			main.add(aScroll);
			main.add(sScroll);
			this.add("South", buttons);
			this.add("Center", main);
			Message<Course> message = new Message<Course>(getCurrentCourse(), "ASSIGNMENTLISTPROF");
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
			if (currentSub != null) {
				download.setEnabled(true);
				grade.setEnabled(true);
			} else {
				download.setEnabled(false);
				grade.setEnabled(false);
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
			} else if (selected.equals("Students")) {
//				StudentPage p = new StudentPage(display.getClient(), display);
//				displayPage(p);
			}
		}

	}
}
