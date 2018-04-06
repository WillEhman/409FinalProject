package backend;

import shared.Assignment;
import shared.*;

import java.sql.*;
import java.util.Vector;


public class DatabaseHelper {

	private PreparedStatement statement;
	private Connection connection;
	// TODO format this
	public String databaseName = "school_master", usersTable = "users", coursesTable = "courses",
			assignmentsTable = "assignments";
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

	// TODO FOR ALL fix try catch statements for proper handling and output
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
				return new User(user.getInt("ID"), user.getString("FIRSTNAME"), user.getString("LASTNAME"),
						user.getString("TYPE"), user.getString("EMAIL"));
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

	public void preparedAdd(User user, String username, String password) {
		String sql = "INSERT INTO users VALUES ( ?, ?, ?, ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, user.getId());
			statement.setString(2, username);
			statement.setString(3, password);
			statement.setString(4, user.getType());
			statement.setString(5, user.getFirstName());
			statement.setString(6, user.getLastName());
			statement.setString(7, user.getEmailAddress());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedAdd(Course course) {
//		System.out.println("Adding Course");
		String sql = "INSERT INTO courses VALUES ( ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());
			statement.setInt(2, course.getProfId());
			statement.setString(3, course.getCourseName());
			statement.setBoolean(4, course.isActive());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void preparedAdd(Assignment assignment) {
		String sql = "INSERT INTO assignments VALUES (Default, ?, ?, ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, assignment.getCourseId());
			statement.setInt(2, assignment.getAssignId());
			statement.setString(3, assignment.getTitle());
			statement.setString(4, assignment.getPath());
			statement.setString(5, assignment.getDueDate());
			statement.setBoolean(6, assignment.isActive());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedSetActive(int id, boolean active) {
		String sql = "UPDATE courses SET ACTIVE = ? WHERE COURSENUMBER = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setBoolean(1, active);
			statement.setInt(2, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedSetActive(Assignment a, boolean active) {
		String sql = "UPDATE assignments SET ACTIVE = ? WHERE COURSENUMBER = ? AND ASSIGNMENTID = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setBoolean(1, active);
			statement.setInt(2, a.getCourseId());
			statement.setInt(3, a.getAssignId());

			statement.executeUpdate();
			System.out.println("Activated Assignment: "+ active);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void preparedSetDue(Assignment a, String date) {
		String sql = "UPDATE courses SET DUEDATE = ? WHERE COURSENUMBER = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, date);
			statement.setInt(2, a.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedRemove(User user) {
		String sql = "DELETE FROM users WHERE ID=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, user.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedRemove(Course course) {
		String sql = "DELETE FROM courses WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vector<User> preparedSearchUsers(int id) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND ID = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			statement.setInt(2, id);
			ResultSet uset = statement.executeQuery();

			while (uset.next()) {
				results.add(new User(uset.getInt("ID"), uset.getString("firstname"), uset.getString("lastname"),
						uset.getString("type"), uset.getString("email")));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Vector<User> preparedSearchUsersinCourse(int id, int coursenumber) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND ID = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			statement.setInt(2, id);
			ResultSet uset = statement.executeQuery();

			while (uset.next()) {
				sql = "SELECT * FROM enrolment WHERE USERID = ? AND COURSENUMBER= ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uset.getInt("ID"));
				statement.setInt(2, coursenumber);
				ResultSet eset = statement.executeQuery();
				if (eset.next()) {
					results.add(new User(uset.getInt("ID"), uset.getString("firstname"), uset.getString("lastname"),
							uset.getString("type"), uset.getString("email")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Vector<User> preparedSearchUsers(String lastname) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND LASTNAME = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			statement.setString(2, lastname);

			ResultSet rset = statement.executeQuery();
			while (rset.next()) {
				results.add(new User(rset.getInt("ID"), rset.getString("firstname"), rset.getString("lastname"),
						rset.getString("type"), rset.getString("email")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Vector<User> preparedSearchUsersinCourse(String lastname, int coursenumber) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND LASTNAME = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			statement.setString(2, lastname);
			ResultSet uset = statement.executeQuery();

			while (uset.next()) {
				sql = "SELECT * FROM enrolment WHERE USERID = ? AND COURSENUMBER= ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uset.getInt("ID"));
				statement.setInt(2, coursenumber);
				ResultSet eset = statement.executeQuery();
				if (eset.next()) {
					results.add(new User(uset.getInt("ID"), uset.getString("firstname"), uset.getString("lastname"),
							uset.getString("type"), uset.getString("email")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Vector<User> preparedSearchUsersinCourse(String lastname, Course object) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND LASTNAME = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			statement.setString(2, lastname);
			ResultSet uset = statement.executeQuery();

			while (uset.next()) {
				sql = "SELECT * FROM enrolment WHERE USERID = ? AND COURSENUMBER= ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uset.getInt("ID"));
				statement.setInt(2, object.getCourseId());
				ResultSet eset = statement.executeQuery();
				if (eset.next()) {
					results.add(new User(uset.getInt("ID"), uset.getString("firstname"), uset.getString("lastname"),
							uset.getString("type"), uset.getString("email")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Vector<User> preparedSearchUsersinCourse(Course object) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			ResultSet uset = statement.executeQuery();

			while (uset.next()) {
				sql = "SELECT * FROM enrolment WHERE USERID = ? AND COURSENUMBER= ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uset.getInt("ID"));
				statement.setInt(2, object.getCourseId());
				ResultSet eset = statement.executeQuery();
				// System.out.println(eset);
				if (eset.next()) {
					results.add(new User(uset.getInt("ID"), uset.getString("firstname"), uset.getString("lastname"),
							uset.getString("type"), uset.getString("email")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Vector<User> preparedSearchUsersinCourse(int id, Course object) {
		Vector<User> results = new Vector<User>();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND ID = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "s");
			statement.setInt(2, id);
			ResultSet uset = statement.executeQuery();

			while (uset.next()) {
				sql = "SELECT * FROM enrolment WHERE USERID = ? AND COURSENUMBER= ?";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, uset.getInt("ID"));
				statement.setInt(2, object.getCourseId());
				ResultSet eset = statement.executeQuery();
				if (eset.next()) {
					results.add(new User(uset.getInt("ID"), uset.getString("firstname"), uset.getString("lastname"),
							uset.getString("type"), uset.getString("email")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	public void preparedEnrol(int userid, int courseid) {
		String sql = "INSERT INTO enrolment VALUES (Default, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);

			statement.setInt(1, userid);
			statement.setInt(2, courseid);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedEnrol(int userid, Course course) {
		String sql = "INSERT INTO enrolment VALUES (Default, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);

			statement.setInt(1, userid);
			statement.setInt(2, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedUnenrol(int userid, int courseid) {
		String sql = "DELETE FROM enrolment WHERE COURSENUMBER=? AND USERID = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseid);
			statement.setInt(2, userid);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void preparedUnenrol(int userid, Course course) {
		String sql = "DELETE FROM enrolment WHERE COURSENUMBER=? AND USERID = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());
			statement.setInt(2, userid);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	Vector<Course> listCourses(Professor prof) {
		String sql = "SELECT * FROM courses WHERE PROFESSORID = " + prof.getId();
		try {
			Vector<Course> listofCourses = new Vector<Course>();

			statement = connection.prepareStatement(sql);
			// statement.setInt(1, prof.getId());
			ResultSet course = statement.executeQuery(sql);

			while (course.next()) {
				Course temp = new Course();
				temp.setActive(course.getBoolean("ACTIVE"));
				temp.setCourseId(course.getInt("COURSENUMBER"));
				temp.setCourseName(course.getString("COURSENAME"));
				temp.setProfId(course.getInt("PROFESSORID"));
				listofCourses.add(temp);
			}
			course.close();
			return listofCourses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	Vector<Assignment> listAssignments(Course course) {
		String sql = "SELECT * FROM assignments WHERE COURSENUMBER = " + course.getCourseId();
		try {
			Vector<Assignment> listofAssignments = new Vector<Assignment>();

			statement = connection.prepareStatement(sql);
			// statement.setInt(1, prof.getId());
			ResultSet assigns = statement.executeQuery(sql);

			while (assigns.next()) {
				Assignment temp = new Assignment();
				temp.setCourseId(assigns.getInt("COURSENUMBER"));
				temp.setAssignId(assigns.getInt("ASSIGNMENTID"));
				temp.setTitle(assigns.getString("ASSIGNMENTNAME"));
				temp.setPath(assigns.getString("FILEPATH"));
				temp.setDueDate(assigns.getString("DUEDATE"));
				temp.setActive(assigns.getBoolean("ACTIVE"));
				listofAssignments.add(temp);
			}
			assigns.close();
			return listofAssignments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	Vector<Assignment> listAssignments(int courseId) {
		String sql = "SELECT * FROM assignments WHERE COURSENUMBER = " + courseId;
		try {
			Vector<Assignment> listofAssignments = new Vector<Assignment>();

			statement = connection.prepareStatement(sql);
			// statement.setInt(1, prof.getId());
			ResultSet assigns = statement.executeQuery(sql);

			while (assigns.next()) {
				Assignment temp = new Assignment();
				temp.setCourseId(assigns.getInt("COURSENUMBER"));
				temp.setAssignId(assigns.getInt("ASSIGNMENTID"));
				temp.setTitle(assigns.getString("ASSIGNMENTNAME"));
				temp.setPath(assigns.getString("FILEPATH"));
				temp.setDueDate(assigns.getString("DUEDATE"));
				temp.setActive(assigns.getBoolean("ACTIVE"));
				listofAssignments.add(temp);
			}
			assigns.close();
			return listofAssignments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	// TODO make prepared statement
	Vector<Course> listCourses(int profid) {
		String sql = "SELECT * FROM courses WHERE PROFESSORID = " + profid;
		try {
			Vector<Course> listofCourses = new Vector<Course>();

			statement = connection.prepareStatement(sql);
			// statement.setInt(1, prof.getId());
			ResultSet course = statement.executeQuery(sql);

			while (course.next()) {
				Course temp = new Course();
				temp.setActive(course.getBoolean("ACTIVE"));
				temp.setCourseId(course.getInt("COURSENUMBER"));
				temp.setCourseName(course.getString("COURSENAME"));
				temp.setProfId(course.getInt("PROFESSORID"));
				listofCourses.add(temp);
			}
			course.close();
			return listofCourses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

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

		System.out.println();
		System.out.println("Trying to add course 200.");
		Course design = new Course(200, 3, "EnggDesign", false);
		masterDB.preparedAdd(design);
		masterDB.preparedprintCourses();
		System.out.println();
		System.out.println("Trying to remove course 200.");
		masterDB.preparedRemove(design);
		masterDB.preparedprintCourses();

		System.out.println();
		System.out.println("Trying to set 225 active");
		masterDB.preparedSetActive(225, true);
		masterDB.preparedprintCourses();
		System.out.println();
		System.out.println("Trying to set 225 inactive");
		masterDB.preparedSetActive(225, false);
		masterDB.preparedprintCourses();

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
		User dave = new User(10, "David", "Parkin", "S", "dparkin@test.com");
		masterDB.preparedAdd(dave, "dparkin", "pass");
		masterDB.preparedprintUsers();
		System.out.println();
		System.out.println("Trying to remove user David.");
		masterDB.preparedRemove(dave);
		masterDB.preparedprintUsers();

		System.out.println();
		System.out.println("Searching for Student 2. Should Return Will Ehman");
		Vector<User> result = masterDB.preparedSearchUsers(2);
		System.out.println(result.get(0).toString());
		System.out.println();
		System.out.println("Searching for Students with last name Ehman. Should return 2 results");
		result = masterDB.preparedSearchUsers("Ehman");
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).toString());
		}
		System.out.println("Searching for Students with last name Ehman in 409. Should return 2 results");
		result = masterDB.preparedSearchUsersinCourse("Ehman", 409);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).toString());
		}
		System.out.println("Searching for Students with last name Ehman in 453. Should return 1 result");
		result = masterDB.preparedSearchUsersinCourse("Ehman", 453);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).toString());
		}

		System.out.println("\nThe program is finished running through the users");

		System.out.println();

		System.out.println("Reading all enrolments from the table:");
		masterDB.preparedprintEnrolments();

		System.out.println("Enrolling Will in 453");
		masterDB.preparedEnrol(2, 453);
		masterDB.preparedprintEnrolments();

		System.out.println("Unenrolling Will in 453");
		masterDB.preparedUnenrol(2, 453);
		masterDB.preparedprintEnrolments();

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
				System.out.println(
						course.getString("ID") + " " + course.getString("USERNAME") + " " + course.getString("PASSWORD")
								+ " " + course.getString("TYPE") + " " + course.getString("FIRSTNAME") + " "
								+ course.getString("LASTNAME") + " " + course.getString("EMAIL"));
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
				System.out.println(course.getInt("COURSENUMBER") + " " + course.getInt("ASSIGNMENTID") + " "
						+ course.getString("FILEPATH"));
			}
			course.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * prints all items in database to console
	 */
	public void preparedprintEnrolments() {
		try {
			String sql = "SELECT * FROM enrolment";
			statement = connection.prepareStatement(sql);
			ResultSet rset = statement.executeQuery(sql);
			System.out.println("Enrolments:");
			while (rset.next()) {
				System.out.println(rset.getInt("ENROLMENTID") + " " + rset.getInt("USERID") + " "
						+ rset.getString("COURSENUMBER"));
			}
			rset.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
