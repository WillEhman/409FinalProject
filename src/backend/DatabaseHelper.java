package backend;

import shared.Course;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;

public class DatabaseHelper {

	PreparedStatement statement;
	Connection connection;
	String sql;
	public String databaseName = "school_master", studentsTable = "students", professorsTable = "professors",
			coursesTable = "courses";
	public String connectionInfo = "jdbc:mysql://localhost:3306/school_master", login = "student", password = "student";

	public DatabaseHelper() {
		try {
			// If this throws an error, make sure you have added the mySQL connector JAR to
			// the project
			Class.forName("com.mysql.jdbc.Driver");

			// If this fails make sure your connectionInfo and login/password are correct
			connection = DriverManager.getConnection(connectionInfo, login, password);
			System.out.println("Connected to: " + connectionInfo + "\n");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Selects and searches for a course id
	 * 
	 * @param courseID
	 *            to be searched for
	 * @return course or null if no course
	 */
	public Course preparedsearchCourses(int courseID) {
		String sql = "SELECT * FROM courses WHERE COURSENUMBER= ?";
		ResultSet course;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseID);
			course = statement.executeQuery();
			if (course.next()) {
				return new Course(course.getInt("COURSENUMBER"), course.getInt("PROFESSORID"), course.getString("COURSENAME"),  course.getBoolean("ACTIVE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * prints all items in database to console
	 */
	public void preparedprintTable() {
		try {
			String sql = "SELECT * FROM " + coursesTable;
			statement = connection.prepareStatement(sql);
			ResultSet course = statement.executeQuery(sql);
			System.out.println("Courses:");
			while (course.next()) {
				System.out.println(course.getInt("COURSENUMBER") + " " + course.getInt("PROFESSORID") + " " + course.getString("COURSENAME") + " " + course.getBoolean("ACTIVE"));
			}
			course.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//~~~~~~~~~~~~~FOR TESTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static void main(String args[]) {
		DatabaseHelper coursesDB = new DatabaseHelper();


		System.out.println("Reading all courses from the table:");
		coursesDB.preparedprintTable();

		System.out.println("\nSearching table for course 409: should return 'ENSF 409'");
		int courseID = 409;
		Course searchResult = coursesDB.preparedsearchCourses(courseID);
		if (searchResult == null)
			System.out.println("Search Failed to find a course matching ID: " + courseID);
		else
			System.out.println("Search Result: " + searchResult.toString());

		System.out.println("\nSearching table for tool 441: should fail to find a tool");
		courseID = 441;
		searchResult = coursesDB.preparedsearchCourses(courseID);
		if (searchResult == null)
			System.out.println("Search Failed to find a course matching ID: " + courseID);
		else
			System.out.println("Search Result: " + searchResult.toString());


		try {
			coursesDB.statement.close();
			coursesDB.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nThe program is finished running");
		}
	}
}
