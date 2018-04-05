package frontend.pages;

import shared.*;

/**
 * @author Luke Kushneryk
 * @since April 4 2018
 * @version 1.0
 * 
 *          AssignmentPage class
 */

public class AssignmentPage extends Page {
	/**
	 * course associated with assignment
	 */
	private Course course;
	
	/**
	 * assignment displayed on page
	 */
	private Assignment assignment;
	
	/**
	 * getters and setters
	 * 
	 */
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
