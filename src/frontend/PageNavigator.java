package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import frontend.Client;
import shared.Course;
import shared.User;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 *
 *          class to display and navigate pages within GUIs
 */
public class PageNavigator {

	/**
	 * JFrames and JPanels to be displayed
	 */
	protected JFrame frame;
	private JPanel pageHolder;
	private JPanel coursePanel;
	private JPanel infoPanel;

	/**
	 * list of courses to be displayed
	 */
	private JList<String> courses;

	/**
	 * label of current course
	 */
	private JLabel courseName;

	/**
	 * currently selected String on display
	 */
	private JComboBox<String> selection;

	/**
	 * vector of courses to be displayed
	 */
	private Vector<Course> vectorOfCourses;

	/**
	 * currently selected course
	 */
	private Course currentCourse;

	/**
	 * Client currently in use with PageNavigator
	 */
	private Client client;

	/**
	 * Constructor for PageNavigator
	 * 
	 * @param c
	 *            is client in use
	 */
	public PageNavigator(Client c) {
		// Initialize the panels and frame
		client = c;
		frame = new JFrame();
		coursePanel = new JPanel();
		infoPanel = new JPanel();
		JPanel title = new JPanel();
		JPanel infoTitle = new JPanel();
		pageHolder = new JPanel();

		// Set layouts
		frame.setLayout(new BorderLayout());
		// frame.getContentPane().setBackground(Color.CYAN); TODO make this work maybe
		title.setLayout(new FlowLayout());
		infoPanel.setLayout(new BorderLayout());
		infoTitle.setLayout(new BorderLayout());
		coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
		pageHolder.setLayout(new FlowLayout());

		// Setup Courses Panel
		JLabel cTitle = new JLabel("Your Courses");
		courses = new JList();
		JScrollPane cScroll = new JScrollPane(courses);
		cTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		coursePanel.add(cTitle);
		coursePanel.add(cScroll);
		coursePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

		// Setup Info Panel
		pageHolder.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		courseName = new JLabel("      Select a Course");
		String[] options = { "Home" };
		selection = new JComboBox<String>(options);
		infoTitle.add("West", selection);
		infoTitle.add("Center", courseName);
		infoPanel.add("North", infoTitle);
		infoPanel.add("Center", pageHolder);
		infoPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

		// Display the frame
		frame.add("North", title);
		frame.add("West", coursePanel);
		frame.add("Center", infoPanel);
		frame.setSize(1000, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Displays a page
	 * 
	 * @param p
	 *            is page being displayed
	 */
	public void displayPage(JPanel p) {
		infoPanel.remove(pageHolder);
		pageHolder.removeAll();
		pageHolder = p;
		pageHolder.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		infoPanel.add(pageHolder);
		frame.setVisible(true);
	}

	/**
	 * Getters and setters
	 * 
	 * @param s
	 */
	public void setFrameText(String s) {
		frame.setTitle(s);
	}

	public Client getClient() {
		return client;
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	public JPanel getCoursePanel() {
		return coursePanel;
	}

	public void setCourses(Vector<Course> v) {
		vectorOfCourses = v;
		String[] temp = new String[v.size()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = v.get(i).toString();
			System.out.println(temp[i]);
		}
		courses.setListData(temp);
		try {
			currentCourse = vectorOfCourses.get(0);
		} catch (ArrayIndexOutOfBoundsException e) {
			currentCourse = null;
		}
	}
	
	public void setCourses(Vector<Course> v, int index) {
		vectorOfCourses = v;
		String[] temp = new String[v.size()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = v.get(i).toString();
			System.out.println(temp[i]);
		}
		courses.setListData(temp);
		try {
			currentCourse = vectorOfCourses.get(index);
		} catch (ArrayIndexOutOfBoundsException e) {
			currentCourse = null;
		}
	}
	
	public JList<String> getCourses() {
		return courses;
	}

	public void setCourseListener(ListSelectionListener l) {
		courses.addListSelectionListener(l);
	}

	public void setBoxListener(ActionListener a) {
		selection.addActionListener(a);
	}

	public void setCurrentCourse() {
		if (courses.getSelectedIndex() >= 0) {
			currentCourse = vectorOfCourses.get(courses.getSelectedIndex());
		}
	}
	
	public void setCurrentCourse(Course c) {
		currentCourse = c;
	}

	public Course getCurrentCourse() {
		return currentCourse;
	}

	public void setCourseName(String name) {
		courseName.setText("      " + name);
	}

	public void setComboBox(int i) {
		selection.setSelectedIndex(i);
	}

	public void setSelections(String[] s) {
		DefaultComboBoxModel model = new DefaultComboBoxModel(s);
		selection.setModel(model);
	}
	
	public int getCourseIndex() {
		for (int i = 0; i < vectorOfCourses.size(); i++) {
			if (currentCourse.getCourseId() == vectorOfCourses.get(i).getCourseId()) {
				return i;
			}
		}
		return 0;
	}
}
