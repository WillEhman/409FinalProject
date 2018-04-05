package frontend.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.*;

public class PageNavigator {

	private JFrame frame;
	private JPanel pageHolder;
	private JList<String> courses;
	private JButton add;
	private JLabel courseName;
	private JComboBox<String> selection;

	public PageNavigator() {
		//Initialize the panels and frame
		frame = new JFrame("Course Manager 2018");
		JPanel coursePanel = new JPanel();
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
		add = new JButton("Add");
		add.setAlignmentX(Component.CENTER_ALIGNMENT);
		cTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		coursePanel.add(cTitle);
		coursePanel.add(cScroll);
		coursePanel.add(add);
		
		//Setup Info Panel
		courseName = new JLabel("TEMP TITLE");
		String[] options = {"Students","Assignments","Grades"};
		selection = new JComboBox<String>(options);
		infoTitle.add("West", selection);
		infoTitle.add("Center", courseName);
		infoPanel.add("North", infoTitle);
		infoPanel.add("Center", pageHolder);
		
		//Display the frame
		frame.add("North", title);
		frame.add("West", coursePanel);
		frame.add("Center", infoPanel);
		frame.setSize(1000,600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void displayPage(JPanel p) {
		pageHolder = p;
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

	public void setPageHolder(JPanel pageHolder) {
		this.pageHolder = pageHolder;
	}

	public static void main(String[] args) {
		PageNavigator n = new PageNavigator();
	}
}
