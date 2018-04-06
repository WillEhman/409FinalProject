package shared;

import java.io.File;
import java.io.Serializable;

/**
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          class for submitted assignments
 */
public class Assignment implements Serializable {

	/**
	 * serial id for the class
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * unique id for each assignment
	 */
	private int assignId;

	/**
	 * associated course id
	 */
	private int courseId;

	/**
	 * title of assignment
	 */
	private String title;

	/**
	 * path of assignment file
	 */
	private String path;

	/**
	 * tells whether or not assignment is active
	 */
	private boolean active;

	/**
	 * due date of assignment
	 */
	private String dueDate;
	
	private String fileData;

	public Assignment() {
		assignId = 0;
		courseId = 0;
		this.title = null;
		this.path = null;
		active = false;
		dueDate = null;
		this.fileData = null;
	}
	/**
	 * constructor for assignment
	 * 
	 * @param aid
	 *            assignment id
	 * @param cid
	 *            course id
	 * @param title
	 *            String with assignment title
	 * @param path
	 * @param a
	 *            sets active boolean
	 * @param date
	 *            due date for assignment
	 */
	public Assignment(int aid, int cid, String title, String path, boolean a, String date, String file) {
		assignId = aid;
		courseId = cid;
		this.title = title;
		this.path = path;
		active = a;
		dueDate = date;
		this.fileData = file;
	}

	//This code for splitting inspired by https://stackoverflow.com/questions/4545937/splitting-the-filename-into-a-base-and-extension
	public String getFileQuery() {
//		System.out.println(path);
		String[] split = path.split("\\.(?=[^\\.]+$)");
//		System.out.println(split[0]);
//		System.out.println(split[1]);
		String returnString = fileData+".SPLITTER."+split[0]+".SPLITTER."+split[1];
//		System.out.println(returnString);
		return returnString;
	}
	/**
	 * getters and setters
	 * 
	 */
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
	
	public String getFileData() {
		return fileData;
	}

	public void setFileData(String file) {
		this.fileData = file;
	}

}
