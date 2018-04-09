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
 *
 */
public class ProfessorGUI extends PageNavigator {

	private Client client;
	private Professor professor;
	private boolean isProfessor;
	private JButton add, remove;

	public ProfessorGUI(User user, Client client) {
		super(client);
		setCourseListener(new CourseListListener(this));
		setBoxListener(new BoxListener(this));
		professor = new Professor(user);
		isProfessor = true;
		super.setFrameText("Course Manager 2018: " + professor.getFirstName() + " " + professor.getLastName());
		System.out.println("Creating Message");
		Message<Professor> message = new Message<Professor>(professor, "COURSELIST");
		System.out.println("Sending Message");
		Message<?> recieve = client.communicate(message);
		setCourses((Vector<Course>) recieve.getObject());
		System.out.println("Got Message");
		add = new JButton("Add");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				JButton confirm = new JButton("Confirm");
				confirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							if (cidF.getText().length() == 3) {
								Course newCourse = new Course(Integer.parseInt(cidF.getText()), professor.getId(),
										cNameF.getText(), true);
								Message<Course> message = new Message<Course>(newCourse, "ADDCOURSE");
								Message<?> recieve = client.communicate(message);
								setCourses((Vector<Course>) recieve.getObject());
								options.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Invalid Course ID");
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
				buttons.add(confirm);
				buttons.add(cancel);
				options.add("South", buttons);
				options.setSize(300, 200);
				options.setResizable(false);
				options.setLocationByPlatform(true);
				options.setVisible(true);
			}
		});
		remove = new JButton("Remove");
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Course temp = getCurrentCourse();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null, "Remove " + temp.toString() + "?",
							"Remove Course", dialogButton);
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

	private class StudentPage extends JPanel {

		private static final long serialVersionUID = 1L;
		JPanel buttons;
		JList<String> info;
		JScrollPane scroll;
		JButton enroll, unenroll, search, refresh, eStudent, eAll;
		JTextField searchBar;
		JRadioButton id, lName;
		ButtonGroup radios;
		private Vector<User> studentVector;
		private User currentStudent;

		public StudentPage(Client c, PageNavigator display) {
			buttons = new JPanel();
			radios = new ButtonGroup();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JList();
			scroll = new JScrollPane(info);
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
						setStudents((Vector<User>) receive.getObject());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid ID");
					}
				}
			});
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
			search = new JButton("Search");
			search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
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
						if (searchBar.getText() != null) {
							Message<Course> message = new Message<Course>(getCurrentCourse(),
									"SEARCHSTUDENTSLN.SPLITTER." + searchBar.getText());
							Message<?> receive = c.communicate(message);
							setStudents((Vector<User>) receive.getObject());
						}
					}
				}
			});
			refresh = new JButton("Show All");
			refresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Message<Course> message = new Message<Course>(getCurrentCourse(), "STUDENTLIST");
					Message<?> receive = c.communicate(message);
					setStudents((Vector<User>) receive.getObject());
				}
			});
			info.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent arg0) {
					if (info.getSelectedIndex() >= 0) {
						currentStudent = studentVector.get(info.getSelectedIndex());
						setButtons();
					}
				}
			});
			eStudent = new JButton("Email Student");
			eStudent.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mail(currentStudent.getEmailAddress(), professor.getEmailAddress(), c);
				}
			});
			eAll = new JButton("Email Class");
			eAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mail("All", professor.getEmailAddress(), c);
				}
			});
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

		public void setStudents(Vector<User> v) {
			studentVector = v;
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
		}
		
		public void setButtons() {
			if (currentStudent != null) {
				eStudent.setEnabled(true);
				unenroll.setEnabled(true);
			} else {
				eStudent.setEnabled(false);
				unenroll.setEnabled(false);
			}
		}
		
		public void mail(String to, String from, Client c) {
			JFrame options = new JFrame("Send an Email");
			JTextField subF = new JTextField(5);
			JTextField toF = new JTextField(to, 5);
			JTextField fromF = new JTextField(from, 5);
			JTextField contF = new JTextField(5);
			JTextField passF = new JTextField(5);
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
						if (to.equals("All")) {
							for (int i = 0; i < studentVector.size(); i++) {
								toArray.add(studentVector.get(i).getEmailAddress());
							}
						} else {
							toArray.add(to);
						}
						Email temp = new Email(from, toArray, subF.getText(), contF.getText(), passF.getText());
						Message<Email> message = new Message<Email>(temp, "SENDEMAIL");
						c.communicate(message);
						options.dispose();
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
		JButton upload, active, downloadSub, grade;
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
					info.add(new JLabel("Assignment ID:"));
					info.add(aidF);
					info.add(new JLabel("Assignment Title:"));
					info.add(titleF);
					info.add(new JLabel("Due Date:"));
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
									Assignment newAssign = new Assignment(Integer.parseInt(aidF.getText()),
											getCurrentCourse().getCourseId(), titleF.getText(), pathF.getText(), true,
											dueF.getText(), readFileContent(pathF.getText()));
									String[] path = pathF.getText().split("\\.(?=[^\\.]+$)");
									Message<Assignment> message = new Message<Assignment>(newAssign,
											"CREATEFILE.SPLITTER." + path[path.length - 2] + ".SPLITTER."
													+ path[path.length - 1]);
									Message<?> receive = c.communicate(message);
									setAssignments((Vector<Assignment>) receive.getObject());
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
			downloadSub = new JButton("Download Submission");
			downloadSub.addActionListener(new ActionListener() {
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
			downloadSub.setEnabled(false);
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
				String[] options = { "Home", "Students", "Assignments" };
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
				StudentPage p = new StudentPage(display.getClient(), display);
				displayPage(p);
			}
		}

	}
}
