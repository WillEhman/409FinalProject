package frontend.components;

import javax.swing.JLabel;
import shared.*;


public class CourseItem {
	
	private Course course;
	private JLabel title;
	
	public CourseItem(Course course, JLabel title) {
		this.course = course;
		this.title = title;
	}
	
	public void changetextColor() {
		// TODO
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public JLabel getTitle() {
		return title;
	}

	public void setTitle(JLabel title) {
		this.title = title;
	}

	
}
