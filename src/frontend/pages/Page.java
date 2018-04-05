package frontend.pages;

import java.util.*;

import frontend.*;
import frontend.components.*;

/**
 * @author Luke Kushneryk
 * @since April 4 2018
 * @version 1.0
 * 
 *          top level Page
 */

public class Page<T, U> {

	/**
	 * true if professor
	 */
	protected boolean isProfessor;

	/**
	 * list of information displayed on the page
	 */
	protected ArrayList<U> itemList;

	/**
	 * list of menu items
	 */
	protected BoxList<T> itemDisplay;

	/**
	 * student GUI
	 */
	protected StudentGUI student;

	/**
	 * professor GUI
	 */
	protected ProfessorGUI professor;

	/**
	 * getters and setters
	 * 
	 */
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
