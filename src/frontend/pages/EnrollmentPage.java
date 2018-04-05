package frontend.pages;

import java.util.*;
import shared.*;

public class EnrollmentPage extends Page {
	private ArrayList<Student> enrolledStudentList;
	private Course course;

	public ArrayList<Student> getEnrolledStudentList() {
		return enrolledStudentList;
	}

	public void setEnrolledStudentList(ArrayList<Student> enrolledStudentList) {
		this.enrolledStudentList = enrolledStudentList;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
}
