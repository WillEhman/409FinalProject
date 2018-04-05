package backend;

import shared.Course;

import java.sql.*;

public class DatabaseHelper {

	private PreparedStatement statement;
	private Connection connection;
	private String sql;
	public String databaseName = "school_master", usersTable = "users", coursesTable = "courses";
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
				return new Course(course.getInt("COURSENUMBER"), course.getString("PROFESSORNAME"),
						course.getString("COURSENAME"), course.getBoolean("ACTIVE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isValidStudentLogin(String username, String password) {
		String sql = "SELECT * FROM users WHERE USERNAME= ?";
		ResultSet user;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			user = statement.executeQuery();

			if (user.next()) {
				if (password.equals(user.getString("PASSWORD")) && user.getString("TYPE").equals("s")) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean isValidProfLogin(String username, String password) {
		String sql = "SELECT * FROM users WHERE USERNAME= ?";
		ResultSet user;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			user = statement.executeQuery();

			if (user.next()) {
				if (password.equals(user.getString("PASSWORD")) && user.getString("TYPE").equals("p")) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}


	// ~~~~~~~~~~~~~FOR_TESTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static void main(String args[]) {
		DatabaseHelper masterDB = new DatabaseHelper();

		System.out.println("Reading all courses from the table:");
		masterDB.preparedprintCourses();

		System.out.println("\nSearching table for course 409: should return 'SoftwareDesign'");
		int courseID = 409;
		Course searchResult = masterDB.preparedsearchCourses(courseID);
		if (searchResult == null)
			System.out.println("Search Failed to find a course matching ID: " + courseID);
		else
			System.out.println("Search Result: " + searchResult.getCourseName());

		System.out.println("\nSearching table for tool 441: should fail to find a course");
		courseID = 441;
		searchResult = masterDB.preparedsearchCourses(courseID);
		if (searchResult == null)
			System.out.println("Search Failed to find a course matching ID: " + courseID);
		else
			System.out.println("Search Result: " + searchResult.toString());

		System.out.println("\nThe program is finished running through the courses");

		System.out.println("Reading all users from the table:");
		masterDB.preparedprintUsers();

		System.out.println();
		System.out.println("Student Logins");
		System.out.println("Trying to login as Will. Should succeed:");
		if (masterDB.isValidStudentLogin("will", "pw")) {
			System.out.println("Successful login");
		} else {
			System.err.println("Failed login");
		}

		System.out.println("Trying to login as David. Should fail:");
		if (masterDB.isValidStudentLogin("dave", "asdf")) {
			System.out.println("Successful login");
		} else {
			System.err.println("Failed login");
		}

		System.out.println();
		System.out.println("Professor Logins");
		System.out.println("Trying to login as Will. Should fail:");
		if (masterDB.isValidProfLogin("will", "pw")) {
			System.out.println("Successful login");
		} else {
			System.err.println("Failed login");
		}

		System.out.println("Trying to login as Norm. Should Succeed:");
		if (masterDB.isValidProfLogin("norm", "42")) {
			System.out.println("Successful login");
		} else {
			System.err.println("Failed login");
		}

		try {
			masterDB.statement.close();
			masterDB.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nThe program is finished running through the users");
		}

	}
	

	/**
	 * prints all items in database to console
	 */
	public void preparedprintCourses() {
		try {
			String sql = "SELECT * FROM " + coursesTable;
			statement = connection.prepareStatement(sql);
			ResultSet course = statement.executeQuery(sql);
			System.out.println("Courses:");
			while (course.next()) {
				System.out.println(course.getInt("COURSENUMBER") + " " + course.getString("PROFESSORNAME") + " "
						+ course.getString("COURSENAME") + " " + course.getBoolean("ACTIVE"));
			}
			course.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * prints all items in database to console
	 */
	public void preparedprintUsers() {
		try {
			String sql = "SELECT * FROM " + usersTable;
			statement = connection.prepareStatement(sql);
			ResultSet course = statement.executeQuery(sql);
			System.out.println("Users:");
			while (course.next()) {
				System.out.println(course.getString("USERNAME") + " " + course.getString("PASSWORD") + " "
						+ course.getString("TYPE"));
			}
			course.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
