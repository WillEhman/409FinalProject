package shared;

import java.io.Serializable;

/**
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Contains Submission Class
 */

public class Submission implements Serializable{
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * id of submission
	 */
	private int id;
	
	/**
	 * id of assignment associated with the submission
	 */
	private int assignId;
	
	/**
	 * id of student submitting
	 */
	private int studentId;
	
	/**
	 * path of submitted file
	 */
	private String path;
	
	/**
	 * grade of submission
	 */
	private int grade;
	
	/**
	 * comment on submission
	 */
	private String comment;
	
	/**
	 * title of submission
	 */
	private String title;
	
	/**
	 * time of submission
	 */
	private String timeStamp;
	
	public Submission(int id, int assignId, int studentId, String path, int grade, String comment, String title, String timeStamp) {
		this.id = id;
		this.assignId = assignId;
		this.studentId = studentId;
		this.path = path;
		this.grade = grade;
		this.comment = comment;
		this.title = title;
		this.timeStamp = timeStamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssignId() {
		return assignId;
	}

	public void setAssignId(int assignId) {
		this.assignId = assignId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
