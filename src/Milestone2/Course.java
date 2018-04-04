
public class Course {
	
	private int courseId;
	private int profId;
	private String courseName;
	private boolean active;
	
	public Course(int cid, int pid, String name, boolean a) {
		courseId = cid;
		profId = pid;
		courseName = name;
		active = a;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public int getProfId() {
		return profId;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setCourseId(int cid) {
		courseId = cid;
	}
	
	public void setProfId(int pid) {
		profId = pid;
	}

	public void setCourseName(String name) {
		courseName = name;
	}
	
	public void setActive(boolean a) {
		active = a;
	}
}
