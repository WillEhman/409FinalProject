package shared;

public class StudentEnrollment {
	
	private int id;
	private int studentId;
	private int courseId;
	private boolean enrolling;
	
	public StudentEnrollment(int id, int studentId, int courseId, boolean enrolling) {
		this.id = id;
		this.studentId = studentId;
		this.courseId = courseId;
		this.enrolling = enrolling;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isEnrolling() {
		return enrolling;
	}

	public void setEnrolling(boolean enrolling) {
		this.enrolling = enrolling;
	}
	
}
