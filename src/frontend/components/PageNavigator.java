package frontend.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.*;

public class PageNavigator {

	private JFrame frame;
	private JPanel pageHolder;
	private JPanel coursePanel;
	private JPanel infoPanel;
	private JPanel title;
	private JScrollPane courses;
	private JButton add;
	private JPanel infoTitle;
	private JLabel courseName;
	private JComboBox<String> selection;

	public PageNavigator() {
		//Initialize the panels and frame
		frame = new JFrame("Welcome");
		coursePanel = new JPanel();
		infoPanel = new JPanel();
		title = new JPanel();
		infoTitle = new JPanel();
		pageHolder = new JPanel();
		
		//Set layouts
		frame.setLayout(new BorderLayout());
		title.setLayout(new FlowLayout());
		infoPanel.setLayout(new BorderLayout());
		infoTitle.setLayout(new FlowLayout());
		coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
		pageHolder.setLayout(new FlowLayout());
		
		//Setup Courses Panel
		courses = new JScrollPane();
		add = new JButton("Add");
		coursePanel.add(courses);
		coursePanel.add(add);
		//coursePanel.setp;
		
		//Setup Info Panel
		courseName = new JLabel("");
		String[] options = {"Students","Assignments","Grades"};
		selection = new JComboBox<String>(options);
		infoTitle.add(selection);
		infoTitle.add(courseName);
		infoPanel.add("North", infoTitle);
		
		frame.add("North", title);
		frame.add("West", coursePanel);
		frame.add("Center", infoPanel);
		frame.setSize(1000,600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
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
