package shared;

import java.io.Serializable;

/**
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          contains Course object
 */
public class Course implements Serializable {

	/**
	 * Serial id for object
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * unique course id
	 */
	private int courseId;

	/**
	 * name of course professor
	 */
	private String profName;

	/**
	 * name of course
	 */
	private String courseName;

	/**
	 * true if currently active
	 */
	private boolean active;

	/**
	 * Constructor for course
	 * 
	 * @param courseId
	 * @param profName
	 * @param courseName
	 * @param active
	 */
	public Course(int courseId, String profName, String courseName, boolean active) {
		this.courseId = courseId;
		this.profName = profName;
		this.courseName = courseName;
		this.active = active;
	}

	/**
	 * getters and setters
	 * 
	 */
	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getProfName() {
		return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
