package shared;

public class Grade {
	
	private int studentId;
	private int grade;
	private String studentName;
	private String assignName;
	
	public Grade(int studentId, int grade, String studentName, String assignName) {
		this.studentId = studentId;
		this.grade = grade;
		this.studentName = studentName;
		this.assignName = assignName;
	}
	
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
