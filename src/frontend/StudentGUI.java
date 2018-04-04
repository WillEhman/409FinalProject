package frontend;

import shared.*;
import java.awt.CardLayout;
import javax.swing.JPanel;
import frontend.components.*;

public class StudentGUI extends PageNavigator {

	private Client client;
	private Student student;
	private boolean isStudent;

	public StudentGUI(JPanel pageHolder, CardLayout cardLayout) {
		super(pageHolder, cardLayout);
		// TODO Auto-generated constructor stub
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public boolean isStudent() {
		return isStudent;
	}

	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
}
