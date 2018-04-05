package frontend.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.*;

import shared.Course;

public class PageNavigator {

	private JFrame frame;
	private JPanel pageHolder;
	private JPanel coursePanel;
	private JList<String> courses;
	private JLabel courseName;
	private JComboBox<String> selection;
	private Course course;

	public PageNavigator() {
		//Initialize the panels and frame
		frame = new JFrame("Course Manager 2018");
		coursePanel = new JPanel();
		JPanel infoPanel = new JPanel();
		JPanel title = new JPanel();
		JPanel infoTitle = new JPanel();
		pageHolder = new JPanel();
		
		//Set layouts
		frame.setLayout(new BorderLayout());
		title.setLayout(new FlowLayout());
		infoPanel.setLayout(new BorderLayout());
		infoTitle.setLayout(new BorderLayout());
		coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
		pageHolder.setLayout(new FlowLayout());
		
		//Setup Courses Panel
		JLabel cTitle = new JLabel("Your Courses");
		courses = new JList();
		JScrollPane cScroll = new JScrollPane(courses);
		cTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		coursePanel.add(cTitle);
		coursePanel.add(cScroll);
		coursePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		
		//Setup Info Panel
		pageHolder.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		courseName = new JLabel("TEMP TITLE");
		String[] options = {"Home","Students","Assignments"};
		selection = new JComboBox<String>(options);
		infoTitle.add("West", selection);
		infoTitle.add("Center", courseName);
		infoPanel.add("North", infoTitle);
		infoPanel.add("Center", pageHolder);
		infoPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		
		//Display the frame
		frame.add("North", title);
		frame.add("West", coursePanel);
		frame.add("Center", infoPanel);
		frame.setSize(1000,600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public void displayPage(JPanel p) {
		pageHolder = p;
	}

	public JPanel getCoursePanel() {
		return coursePanel;
	}
	
	public void setCourses(Vector v) {
		courses.setListData(v);
	}

	public void showPage(String page) {
		// TODO
	}

	public void addPage(JPanel page, String name) {
		// TODO
	}

	public void removePage(String page) {
		// TODO
	}

	public JPanel searchPage(String page) {
		return null; // TODO: Set later
	}

	public JPanel getPageHolder() {
		return pageHolder;
	}

//	public static void main(String[] args) {
//		PageNavigator n = new PageNavigator();
//	}
}
