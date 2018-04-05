package frontend.components;

import javax.swing.JButton;
import shared.*;

public class StudentItem {
	private Student student;
	private boolean enrolled;
	private JButton enrollButton;

	public StudentItem(Student student, boolean enrolled, JButton enrollButton) {
		this.student = student;
		this.enrolled = enrolled;
		this.enrollButton = enrollButton;
	}

	public void changeEnrolledStatus() {
		// TODO: remove for getters and setters?
	}

	public void changeBoxColor() {
		// TODO: something to do with dropboxes?
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public boolean isEnrolled() {
		return enrolled;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}

	public JButton getEnrollButton() {
		return enrollButton;
	}

	public void setEnrollButton(JButton enrollButton) {
		this.enrollButton = enrollButton;
	}

}
