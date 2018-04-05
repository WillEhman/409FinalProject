package frontend;

import shared.*;
import java.awt.Component;
import java.util.Vector;

import javax.swing.*;
import frontend.components.*;

public class ProfessorGUI extends PageNavigator {

	private Client client;
	private Professor professor;
	private boolean isProfessor;

	public ProfessorGUI(User user, Client client) {
		super();
		professor = new Professor(user);
		isProfessor = true;
		Message<Professor> message = new Message<Professor>(professor, "COURSE");
		Message<?> recieve = client.communicate(message);
		JButton add = new JButton("add");
	    add.setAlignmentX(Component.CENTER_ALIGNMENT);
	    super.setCourses((Vector) recieve.getObject());
	    super.getCoursePanel().add(add);
	    super.setVisible(true);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public boolean isProfessor() {
		return isProfessor;
	}

	public void setProfessor(boolean isProfessor) {
		this.isProfessor = isProfessor;
	}
	
	public static void main(String[] args) {
		ProfessorGUI n = new ProfessorGUI(new Professor(0, null, null, null), null);
	}

}
