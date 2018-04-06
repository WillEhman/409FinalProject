package frontend;

import shared.*;
import java.awt.CardLayout;
import javax.swing.JPanel;
//import frontend.components.*;

public class StudentGUI extends PageNavigator {

	private Client client;
	private Student student;
	private boolean isStudent;

	public StudentGUI(Client client) {
		super(client);
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
