package frontend;

import shared.*;
import java.awt.Component;

import javax.swing.*;
import frontend.components.*;

public class ProfessorGUI extends PageNavigator {

	private Client client;
	private Professor professor;
	private boolean isProfessor;

	public ProfessorGUI() {
		super();
	    JButton add = new JButton("add");
	    add.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		ProfessorGUI n = new ProfessorGUI();
	}

}
