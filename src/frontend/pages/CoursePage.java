package frontend.pages;

import shared.Course;

/**
 * @author Luke Kushneryk
 * @since April 4 2018
 * @version 1.0
 * 
 *          CoursePage class
 */

public class CoursePage extends Page {
	
	/**
	 * course for CoursePage
	 */
	private Course course;

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
	
}
