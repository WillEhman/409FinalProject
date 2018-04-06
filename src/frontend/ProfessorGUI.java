package frontend;

import shared.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;
import frontend.components.*;

public class ProfessorGUI extends PageNavigator {

	private Client client;
	private Professor professor;
	private boolean isProfessor;
	private Vector<Course> courses;
	private JButton add, remove;

	public ProfessorGUI(User user, Client client) {
		super();
		professor = new Professor(user);
		isProfessor = true;
		System.out.println("Creating Message");
		Message<Professor> message = new Message<Professor>(professor, "COURSELIST");
		System.out.println("Sending Message");
		Message<?> recieve = client.communicate(message);
		courses = (Vector<Course>) recieve.getObject();
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
							Course newCourse = new Course(Integer.parseInt(cidF.getText()), professor.getId(), cNameF.getText(),true);
							Message message = new Message<Course>(newCourse, "ADDCOURSE");
							Message recieve = client.communicate(message);
							courses = (Vector<Course>) recieve.getObject();
							refreshCourses(courses);
							options.dispose();
						} catch (NumberFormatException e) {
							//TODO: warning message
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
				options.setSize(300,200);
				options.setResizable(false);
				options.setLocationByPlatform(true);
				options.setVisible(true);
			}
		});
	    remove = new JButton("Remove");
	    remove.setAlignmentX(Component.CENTER_ALIGNMENT);
	    add.setAlignmentX(Component.CENTER_ALIGNMENT);
	    refreshCourses(courses);
	    super.getCoursePanel().add(add);  
	    super.getCoursePanel().add(remove);
	    super.setVisible(true);
	    StudentPage p = new StudentPage();
	    super.displayPage(p);
	    AssignmentPage p1 = new AssignmentPage();
	    super.displayPage(p1);
	    HomePage p2 = new HomePage();
	    super.displayPage(p2);
	}
	
	public void refreshCourses(Vector<Course> v) {
		super.setCourses(v);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public boolean isProfessor() {
		return isProfessor;
	}

	public void setProfessor(boolean isProfessor) {
		this.isProfessor = isProfessor;
	}

	private class StudentPage extends JPanel{
		
		private static final long serialVersionUID = 1L;
		JPanel buttons;
		JTextArea info;
	    JScrollPane scroll;
	    JButton enroll;
	    JButton search;
	    JTextField searchBar;
		
		public StudentPage() {
		    buttons = new JPanel();
		    buttons.setLayout(new FlowLayout());
		    this.setLayout(new BorderLayout());
		    info = new JTextArea();
		    scroll = new JScrollPane(info);
		    enroll = new JButton("Enroll");
		    search = new JButton("Search");
		    searchBar = new JTextField(20);
		    buttons.add(searchBar);
		    buttons.add(search);
		    buttons.add(enroll);
		    this.add("South", buttons);
		    this.add("Center", scroll);
		}
	}
	
	private class AssignmentPage extends JPanel{
		
		private static final long serialVersionUID = 1L;
		JPanel buttons;
		JTextArea info;
	    JScrollPane scroll;
	    JButton download;
	    
		public AssignmentPage() {
		    buttons = new JPanel();
		    buttons.setLayout(new FlowLayout());
		    this.setLayout(new BorderLayout());
		    info = new JTextArea();
		    scroll = new JScrollPane(info);
		    download = new JButton("Download");
		    buttons.add(download);
		    this.add("South", buttons);
		    this.add("Center", scroll);
		}
	}
	
	private class HomePage extends JPanel{

		private static final long serialVersionUID = 1L;
		JTextArea info;
	    JScrollPane scroll;
	    
		public HomePage() {
		    this.setLayout(new BorderLayout());
		    info = new JTextArea();
		    scroll = new JScrollPane(info);
		    this.add("Center", scroll);
		}
	}
}
