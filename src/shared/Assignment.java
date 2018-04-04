package shared;

import java.io.Serializable;

public class Assignment implements Serializable{

	private static final long serialVersionUID = 1L;
	private int assignId;
	private int courseId;
	private String title;
	private String path;
	private boolean active;
	private String dueDate;
	
	public Assignment(int aid, int cid, String title, String path, boolean a, String date) {
		assignId = aid;
		courseId = cid;
		this.title = title;
		this.path = path;
		active = a;
		dueDate = date;
	}

	public int getAssignId() {
		return assignId;
	}

	public void setAssignId(int assignId) {
		this.assignId = assignId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
}
