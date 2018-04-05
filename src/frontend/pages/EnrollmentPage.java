package frontend.pages;

import java.util.*;
import shared.*;

/**
 * @author Luke Kushneryk
 * @since April 4 2018
 * @version 1.0
 * 
 *          EnrollmentPage class
 */

public class EnrollmentPage<T,U> extends Page<T,U> {
	/**
	 * list of students currently enrolled
	 */
	private ArrayList<Student> enrolledStudentList;

	/**
	 * course enrollment page is looking at
	 */
	private Course course;

	/**
	 * getters and setters
	 * 
	 */
	public ArrayList<Student> getEnrolledStudentList() {
		return enrolledStudentList;
	}

	public void setEnrolledStudentList(ArrayList<Student> enrolledStudentList) {
		this.enrolledStudentList = enrolledStudentList;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
