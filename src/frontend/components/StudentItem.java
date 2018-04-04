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
	
	public void	changeEnrolledStatus() {
		
	}
	
	public void changeBoxColor() {
		
	}
	
}
