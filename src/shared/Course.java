package shared;

public class Course {
	
	private int courseId;
	private String profName;
	private String courseName;
	private boolean active;
	
	public Course(int courseId, String profName, String courseName, boolean active) {
		this.courseId = courseId;
		this.profName = profName;
		this.courseName = courseName;
		this.active = active;
	}

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
