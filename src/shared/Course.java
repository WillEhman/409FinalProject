package shared;

import java.io.Serializable;
/**
 * 
 * @author William Ehman
 * @author David Parkin
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
	private int profId;

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
	public Course() {
		this.courseId = 0;
		this.profId = 0;
		this.courseName = null;
		this.active = false;
	}
	/**
	 * Constructor for course
	 * 
	 * @param courseId
	 * @param profName
	 * @param courseName
	 * @param active
	 */
	public Course(int courseId, int profId, String courseName, boolean active) {
		this.courseId = courseId;
		this.profId = profId;
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

	public int getProfId() {
		return profId;
	}

	public void setProfId(int profId) {
		this.profId = profId;
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
	
	public String toString() {
		return this.courseId + ": " + this.courseName;
	}

}
