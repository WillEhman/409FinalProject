package frontend;

import shared.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import frontend.components.*;

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
					// JOptionPane.showMessageDialog(null, "No Course Selected");
				}
			}
		});
		remove.setAlignmentX(Component.CENTER_ALIGNMENT);
		add.setAlignmentX(Component.CENTER_ALIGNMENT);
		super.getCoursePanel().add(add);
		super.getCoursePanel().add(remove);
		super.setVisible(true);
		// StudentPage p = new StudentPage();
		// super.displayPage(p);
		// AssignmentPage p1 = new AssignmentPage();
		// super.displayPage(p1);
		// HomePage p2 = new HomePage();
		// super.displayPage(p2);
	}

	private class StudentPage extends JPanel {

		private static final long serialVersionUID = 1L;
		JPanel buttons;
		JList<String> info;
		JScrollPane scroll;
		JButton enroll, unenroll, search, refresh;
		JTextField searchBar;
		JRadioButton id, lName;
		private Vector<User> studentVector;
		private String command;

		public StudentPage(Client c) {
			command = "ID";
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JList();
			scroll = new JScrollPane(info);
			enroll = new JButton("Enroll");
			unenroll = new JButton("Unenroll");
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
							// TODO: handle exception
						}
					} else if (lName.isSelected()) {
						try {
							if (searchBar.getText() != null) {
								Message<Course> message = new Message<Course>(getCurrentCourse(),
										"SEARCHSTUDENTSLN.SPLITTER." + searchBar.getText());
								Message<?> receive = c.communicate(message);
								setStudents((Vector<User>) receive.getObject());
							}
						} catch (Exception e) {
							// TODO: handle exception
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
			searchBar = new JTextField(20);
			id = new JRadioButton("ID");
			lName = new JRadioButton("LastName");
			id.setSelected(true);
			buttons.add(id);
			buttons.add(lName);
			buttons.add(searchBar);
			buttons.add(search);
			buttons.add(refresh);
			buttons.add(enroll);
			buttons.add(unenroll);
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
		}

		public void setCommand(String s) {
			command = s;
		}
	}

	private class AssignmentPage extends JPanel {

		private static final long serialVersionUID = 1L;
		JPanel buttons;
		JTextArea info;
		JScrollPane scroll;
		JButton upload;

		public AssignmentPage(Client c) {
			buttons = new JPanel();
			buttons.setLayout(new FlowLayout());
			this.setLayout(new BorderLayout());
			info = new JTextArea();
			scroll = new JScrollPane(info);
			upload = new JButton("Upload");
			buttons.add(upload);
			this.add("South", buttons);
			this.add("Center", scroll);
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
			active = new JButton("Set Active");
			active.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Course temp = getCurrentCourse();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Change Course Active to " + !temp.isActive(), "Set Active", dialogButton);
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
				StudentPage p = new StudentPage(display.getClient());
				displayPage(p);
			}
		}

	}
}
