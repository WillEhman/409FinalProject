package backend;

import shared.Course;
import shared.LoginInfo;
import shared.Student;
import shared.User;

import java.sql.*;

public class DatabaseHelper {

	private PreparedStatement statement;
	private Connection connection;
	public String databaseName = "school_master", usersTable = "users", coursesTable = "courses", assignmentsTable = "assignments";
	public String connectionInfo = "jdbc:mysql://localhost:3306/school_master?verifyServerCertificate=false&useSSL=true",
			login = "student", password = "student";

	public DatabaseHelper() {
		try {
			// If this throws an error, make sure you have added the mySQL connector JAR to
			// the project
			Class.forName("com.mysql.jdbc.Driver");

			// If this fails make sure your connectionInfo and login/password are correct
			connection = DriverManager.getConnection(connectionInfo, login, password);
			System.out.println("|---Successfully connected to Database: " + databaseName + "----|");
			System.out.println("|--------------------------------------------------------|");

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
				return new Course(course.getInt("COURSENUMBER"), course.getInt("PROFESSORID"),
						course.getString("COURSENAME"), course.getBoolean("ACTIVE"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public User Login(LoginInfo login) {
		String sql = "SELECT * FROM users WHERE USERNAME= ?";
		ResultSet user;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, login.getUsername());
			user = statement.executeQuery();

			if (user.next()) {
				return new User(user.getInt("ID"), user.getString("FIRSTNAME"), user.getString("LASTNAME"),user.getString("TYPE"),user.getString("EMAIL"));
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
	
	/**
	 * Adds a user to the database table
	 * @param user to be added
	 */

		public void preparedAdd(User user, String username, String password) {
			String sql = "INSERT INTO users VALUES ( ?, ?, ?, ?, ?, ?, ?)";
			try {
				statement =  connection.prepareStatement(sql);
				statement.setInt(1,user.getId());
				statement.setString(2,username);
				statement.setString(3,password);
				statement.setString(4,user.getType());
				statement.setString(5,user.getFirstName());
				statement.setString(6,user.getLastName());
				statement.setString(7,user.getEmailAddress());

				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Adds a user to the database table
		 * @param user to be added
		 */

			public void preparedRemove(User user) {
				String sql = "DELETE FROM users WHERE ID=?";
				try {
					statement =  connection.prepareStatement(sql);
					statement.setInt(1,user.getId());

					statement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

	// ~~~~~~~~~~~~~FOR_TESTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static void main(String args[]) {
		DatabaseHelper masterDB = new DatabaseHelper();

		System.out.println();
		System.out.println("Reading all courses from the table:");
		masterDB.preparedprintCourses();

		System.out.println("\nSearching table for course 409: should return 'SoftwareDesign'");
		int courseID = 409;
		Course searchResult = masterDB.preparedsearchCourses(courseID);
		if (searchResult == null)
			System.out.println("Search Failed to find a course matching ID: " + courseID);
		else
			System.out.println("Search Result: " + searchResult.getCourseName());

		System.out.println("Searching table for course 441: should fail to find a course");
		courseID = 441;
		searchResult = masterDB.preparedsearchCourses(courseID);
		if (searchResult == null)
			System.out.println("Search Failed to find a course matching ID: " + courseID);
		else
			System.out.println("Search Result: " + searchResult.getCourseName());
		System.out.println();

		System.out.println("\nThe program is finished running through the courses");
		System.out.println();
		System.out.println();

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
			System.out.println("Failed login");
		}

		System.out.println();
		System.out.println("Professor Logins");
		System.out.println("Trying to login as Will. Should fail:");
		if (masterDB.isValidProfLogin("will", "pw")) {
			System.out.println("Successful login");
		} else {
			System.out.println("Failed login");
		}

		System.out.println("Trying to login as Norm. Should Succeed:");
		if (masterDB.isValidProfLogin("norm", "42")) {
			System.out.println("Successful login");
		} else {
			System.out.println("Failed login");
		}
		System.out.println();
		System.out.println("Trying to add user David.");
		User dave = new User(10,"David", "Parkin","S","dparkin@test.com");
		masterDB.preparedAdd(dave, "dparkin", "pass");
		masterDB.preparedprintUsers();
		System.out.println();
		System.out.println("Trying to remove user David.");
		masterDB.preparedRemove(dave);
		masterDB.preparedprintUsers();

		System.out.println("\nThe program is finished running through the users");
		
		System.out.println();

		System.out.println("Reading all assignments from the table:");
		masterDB.preparedprintAssignments();
		
		
		
		try {
			masterDB.statement.close();
			masterDB.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nThe program is finished running through the Database");
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
				System.out.println(course.getInt("COURSENUMBER") + " " + course.getInt("PROFESSORID") + " "
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
				System.out.println(course.getString("ID") + " " + course.getString("USERNAME") + " " + course.getString("PASSWORD") + " "
						+ course.getString("TYPE")  + " " + course.getString("FIRSTNAME") + " "  + course.getString("LASTNAME") + " " + course.getString("EMAIL"));
			}
			course.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * prints all items in database to console
	 */
	public void preparedprintAssignments() {
		try {
			String sql = "SELECT * FROM " + assignmentsTable;
			statement = connection.prepareStatement(sql);
			ResultSet course = statement.executeQuery(sql);
			System.out.println("Assignments:");
			while (course.next()) {
				System.out.println(course.getInt("COURSENUMBER") + " " + course.getInt("ASSIGNMENTID") + " " + course.getString("FILEPATH"));
			}
			course.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
