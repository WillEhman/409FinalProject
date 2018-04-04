package shared;

import java.io.Serializable;

public class Submission implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int assignId;
	private int studentId;
	private String path;
	private int grade;
	private String comment;
	private String title;
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
