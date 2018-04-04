package frontend.pages;

import shared.*;

public class AssignmentPage extends Page {
	private Course course;
	private Assignment assignment;
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
}
