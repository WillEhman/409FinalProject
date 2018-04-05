package shared;

import java.io.Serializable;

/**
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Contains StudentEnrollment Class
 */

public class StudentEnrollment implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * unique id of enrollment
	 */
	private int id;

	/**
	 * id of enrolling student
	 */
	private int studentId;

	/**
	 * id of course to be enrolled in
	 */
	private int courseId;

	/**
	 * true if enrolling
	 */
	private boolean enrolling;

	/**
	 * Constructor for StudentEnrollment
	 * 
	 * @param id
	 * @param studentId
	 * @param courseId
	 * @param enrolling
	 */
	public StudentEnrollment(int id, int studentId, int courseId, boolean enrolling) {
		this.id = id;
		this.studentId = studentId;
		this.courseId = courseId;
		this.enrolling = enrolling;
	}

	/**
	 * getters and setters
	 * 
	 */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isEnrolling() {
		return enrolling;
	}

	public void setEnrolling(boolean enrolling) {
		this.enrolling = enrolling;
	}

}
