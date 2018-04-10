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
 *          contains Grade class
 */
public class Grade implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * id of student who has the grade
	 */
	private int studentId;

	/**
	 * grade on a scale from 1 to 100
	 */
	private int grade;

	/**
	 * name of student who is associated with grade
	 */
	private String studentName;

	/**
	 * name of assignment which the grade is associated with
	 */
	private String assignName;

	/**
	 * Constructor for grade
	 * 
	 * @param studentId
	 * @param grade
	 * @param studentName
	 * @param assignName
	 */
	public Grade(int studentId, int grade, String studentName, String assignName) {
		this.studentId = studentId;
		this.grade = grade;
		this.studentName = studentName;
		this.assignName = assignName;
	}

	/**
	 * getters and setters
	 * 
	 */
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getAssignName() {
		return assignName;
	}

	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}

}
