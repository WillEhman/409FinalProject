package frontend.pages;

import shared.*;

/**
 * @author Luke Kushneryk
 * @since April 4 2018
 * @version 1.0
 * 
 *          GradePage class
 */

public class GradePage extends Page {
	/**
	 * course used by GradePage
	 */
	private Course course;

	/**
	 * getters and setters
	 * @return
	 */
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
