package frontend.pages;

import java.util.*;

import frontend.*;
import frontend.components.*;
import shared.*;

// T extends Box U????

public class Page <T, U> {
	protected boolean isProfessor;
	protected ArrayList<U> itemList;
	protected BoxList<T> itemDisplay;
	protected StudentGUI student;
	protected ProfessorGUI professor;

	public boolean isProfessor() {
		return isProfessor;
	}

	public void setProfessor(boolean isProfessor) {
		this.isProfessor = isProfessor;
	}

	public ArrayList<U> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<U> itemList) {
		this.itemList = itemList;
	}

	public BoxList<T> getItemDisplay() {
		return itemDisplay;
	}

	public void setItemDisplay(BoxList<T> itemDisplay) {
		this.itemDisplay = itemDisplay;
	}

	public StudentGUI getStudent() {
		return student;
	}

	public void setStudent(StudentGUI student) {
		this.student = student;
	}

	public ProfessorGUI getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorGUI professor) {
		this.professor = professor;
	}

}
