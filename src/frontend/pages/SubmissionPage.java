package frontend.pages;

import shared.*;

/**
 * @author Luke Kushneryk
 * @since April 4 2018
 * @version 1.0
 * 
 *          Contains SubmissionPage class
 */
public class SubmissionPage<T,U> extends Page<T,U> {

	/**
	 * Submission associated with Page
	 */
	private Submission submission;

	/**
	 * Assignment associated with Page
	 */
	private Assignment assignment;

	/**
	 * Course associated with Page
	 */
	private Course course;

	/**
	 * getters and setters
	 * 
	 */
	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
