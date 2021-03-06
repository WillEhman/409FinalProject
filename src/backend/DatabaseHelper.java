package backend;

import shared.*;

import java.sql.*;
import java.util.Vector;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          DatabaseHelper for communication with the database
 *
 */

public class DatabaseHelper {

	// Database data members
	private PreparedStatement statement;
	private Connection connection;
	public String databaseName = "school_master", usersTable = "users", coursesTable = "courses",
			assignmentsTable = "assignments";
	public String connectionInfo = "jdbc:mysql://localhost:3306/school_master?verifyServerCertificate=false&useSSL=true",
			login = "student", password = "student";

	// Establish connection
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

	/**
	 * Logs in user
	 * 
	 * @param login
	 * @return
	 */
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

	/**
	 * Returns true if student login is valid
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isValidStudentLogin(String username, String password) {
		String sql = "SELECT * FROM users WHERE USERNAME= ?";
		ResultSet user;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			user = statement.executeQuery();

			if (user.next()) {
				if (password.equals(user.getString("PASSWORD")) && user.getString("TYPE").equalsIgnoreCase("s")) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Returns true if prof login is valid
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isValidProfLogin(String username, String password) {
		String sql = "SELECT * FROM users WHERE USERNAME= ?";
		ResultSet user;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, username);
			user = statement.executeQuery();

			if (user.next()) {
				if (password.equals(user.getString("PASSWORD")) && user.getString("TYPE").equalsIgnoreCase("p")) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Makes prepared statement for Add
	 * 
	 * @param user
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean preparedAdd(User user, String username, String password) {
		String sql = "INSERT INTO users VALUES ( Default, ?, ?, ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			// statement.setInt(1, user.getId());
			statement.setString(1, username);
			statement.setString(2, password);
			statement.setString(3, user.getType());
			statement.setString(4, user.getFirstName());
			statement.setString(5, user.getLastName());
			statement.setString(6, user.getEmailAddress());

			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gets administrator password
	 * 
	 * @return
	 */
	public String getAdminPW() {
		String sql = "SELECT * FROM users WHERE ID= ?";
		ResultSet user;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, 0);
			user = statement.executeQuery();

			if (user.next()) {
				return user.getString("PASSWORD");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates prepared statement for add
	 * 
	 * @param course
	 * @return
	 */
	public boolean preparedAdd(Course course) {
		// System.out.println("Adding Course");
		String sql = "INSERT INTO courses VALUES ( ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());
			statement.setInt(2, course.getProfId());
			statement.setString(3, course.getCourseName());
			statement.setBoolean(4, course.isActive());

			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates prepared statement for toggle
	 * 
	 * @param course
	 * @return
	 */
	public boolean preparedToggle(Course course) {
		// System.out.println("Adding Course");
		String sql = "UPDATE courses SET ACTIVE =? WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setBoolean(1, course.isActive());
			statement.setInt(2, course.getCourseId());

			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates prepared statement for Add
	 * 
	 * @param assignment
	 */
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

	/**
	 * Creates prepared statement for add
	 * 
	 * @param submission
	 */
	public void preparedAdd(Submission submission) {
		String sql = "INSERT INTO submissions VALUES (Default, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, submission.getCourseId());
			statement.setInt(2, submission.getAssignId());
			statement.setInt(3, submission.getStudentId());
			statement.setString(4, submission.getPath());
			statement.setInt(5, submission.getGrade());
			statement.setString(6, submission.getComment());
			statement.setString(7, submission.getTitle());
			statement.setString(8, submission.getTimeStamp());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates prepared statement for setting course as active
	 * 
	 * @param id
	 * @param active
	 */
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

	/**
	 * Creates prepared statement for setting assignments as active
	 * 
	 * @param a
	 * @param active
	 */
	public void preparedSetActive(Assignment a, boolean active) {
		String sql = "UPDATE assignments SET ACTIVE = ? WHERE COURSENUMBER = ? AND ASSIGNMENTID = ?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setBoolean(1, active);
			statement.setInt(2, a.getCourseId());
			statement.setInt(3, a.getAssignId());

			statement.executeUpdate();
			System.out.println("Activated Assignment: " + active);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates prepared statement for setting assignments as due
	 * 
	 * @param a
	 * @param date
	 */
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

	/**
	 * Creates prepared statement for removing a user
	 * 
	 * @param user
	 */

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

	/**
	 * Creates prepared statement for removing a course
	 * 
	 * @param course
	 */
	public void preparedRemove(Course course) {
		String sql = "DELETE FROM courses WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sql = "DELETE FROM chats WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sql = "DELETE FROM assignments WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sql = "DELETE FROM enrolment WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sql = "DELETE FROM submissions WHERE COURSENUMBER=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, course.getCourseId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates prepared statement for removing a submission
	 * 
	 * @param submission
	 */
	public void preparedRemove(Submission submission) {
		String sql = "DELETE FROM submissions WHERE SID=?";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, submission.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates prepared statement for searching users
	 * 
	 * @param id
	 * @return
	 */
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

	/**
	 * Creates prepared statement for searching for users within a course
	 * 
	 * @param id
	 * @param coursenumber
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for searching for users by last name
	 * 
	 * @param lastname
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for searching for students
	 * 
	 * @param lastname
	 * @param coursenumber
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for searching for users
	 * 
	 * @param lastname
	 * @param object
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for searching for users
	 * 
	 * @param object
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for searching for users
	 * 
	 * @param id
	 * @param object
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for enrolling students
	 * 
	 * @param userid
	 * @param courseid
	 */
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

	/**
	 * Creates a prepared statement for enrolling students
	 * 
	 * @param userid
	 * @param course
	 * @return
	 */
	public boolean preparedEnrol(int userid, Course course) {
		Vector<User> vu1 = preparedSearchUsers(userid); // confirm user exists
		if (vu1.size() != 1) {
			return false;
		}
		Vector<User> vu2 = preparedSearchUsersinCourse(userid, course); // confirm they aren't already enrolled
		if (vu2.size() != 0) {
			return false;
		}

		String sql = "INSERT INTO enrolment VALUES (Default, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);

			statement.setInt(1, userid);
			statement.setInt(2, course.getCourseId());

			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates a prepared statement for unenrolling students
	 * 
	 * @param userid
	 * @param courseid
	 */
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

	/**
	 * Creates a prepared statement for unenrolling students
	 * 
	 * @param userid
	 * @param course
	 */
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

	/**
	 * Creates a prepared statement for listing courses
	 * 
	 * @param prof
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for listing courses
	 * 
	 * @param student
	 * @return
	 */
	Vector<Course> listCourses(Student student) {
		Vector<Course> results = new Vector<Course>();
		try {
			String sql = "SELECT * FROM enrolment WHERE USERID = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, student.getId());
			ResultSet eset = statement.executeQuery();

			while (eset.next()) {
				sql = "SELECT * FROM courses WHERE COURSENUMBER= ? AND ACTIVE =? ORDER BY COURSENUMBER";
				statement = connection.prepareStatement(sql);
				statement.setInt(1, eset.getInt("COURSENUMBER"));
				statement.setBoolean(2, true);
				ResultSet cset = statement.executeQuery();
				if (cset.next()) {
					results.add(new Course(cset.getInt("COURSENUMBER"), cset.getInt("PROFESSORID"),
							cset.getString("COURSENAME"), cset.getBoolean("ACTIVE")));

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Creates a prepared statement for listing assignments in a course
	 * 
	 * @param course
	 * @return
	 */
	Vector<Assignment> listAssignmentsProf(Course course) {
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

	/**
	 * Creates a prepared statement for listing assignments from a course
	 * 
	 * @param courseId
	 * @return
	 */
	Vector<Assignment> listAssignmentsProf(int courseId) {
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

	/**
	 * Creates a prepared statement for listing assignments for a course
	 * 
	 * @param course
	 * @return
	 */
	Vector<Assignment> listAssignmentsStudent(Course course) {
		String sql = "SELECT * FROM assignments WHERE ACTIVE=TRUE AND COURSENUMBER=" + course.getCourseId();
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

	/**
	 * Creates a prepared statement for listing courses
	 * 
	 * @param profid
	 * @return
	 */
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

	/**
	 * Creates a prepared statement for listing submissions for an assignment
	 * 
	 * @param assignment
	 * @return
	 */
	Vector<Submission> listSubmissions(Assignment assignment) {
		// System.out.println(assignment.getCourseId());
		// System.out.println(assignment.getAssignId());

		try {
			// String sql1 = "SELECT * FROM submissions WHERE ASSIGNMENTID=1 AND
			// COURSENUMBER=453";
			String sql2 = "SELECT * FROM submissions WHERE ASSIGNMENTID=" + assignment.getAssignId()
					+ " AND COURSENUMBER=" + assignment.getCourseId();

			Vector<Submission> listofSubmissions = new Vector<Submission>();

			statement = connection.prepareStatement(sql2);
			// statement.setInt(1, assignment.getCourseId());
			// statement.setInt(2, assignment.getAssignId());
			// System.out.println(sql1);
			// System.out.println(sql2);
			ResultSet subs = statement.executeQuery(sql2);

			while (subs.next()) {
				Submission temp = new Submission(subs.getInt("SID"), subs.getInt("COURSENUMBER"),
						subs.getInt("ASSIGNMENTID"), subs.getInt("STUDENTID"), subs.getString("FILEPATH"),
						subs.getInt("Grade"), subs.getString("COMMENT"), subs.getString("TITLE"),
						subs.getString("TIMESTAMP"), null);
				listofSubmissions.add(temp);
			}
			subs.close();
			return listofSubmissions;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Creates a prepared statement for listing submissions for an assignment
	 * 
	 * @param assignment
	 * @param studentId
	 * @return
	 */
	public Vector<Submission> listSubmissions(Assignment assignment, int studentId) {

		try {
			String sql = "SELECT * FROM submissions WHERE STUDENTID=" + studentId + " AND COURSENUMBER="
					+ assignment.getCourseId() + " AND ASSIGNMENTID=" + assignment.getAssignId();

			Vector<Submission> listofSubmissions = new Vector<Submission>();

			statement = connection.prepareStatement(sql);
			ResultSet subs = statement.executeQuery(sql);

			while (subs.next()) {
				Submission temp = new Submission(subs.getInt("SID"), subs.getInt("COURSENUMBER"),
						subs.getInt("ASSIGNMENTID"), subs.getInt("STUDENTID"), subs.getString("FILEPATH"),
						subs.getInt("Grade"), subs.getString("COMMENT"), subs.getString("TITLE"),
						subs.getString("TIMESTAMP"), null);
				listofSubmissions.add(temp);
			}
			subs.close();
			return listofSubmissions;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a prepared statement for listing submissions
	 * 
	 * @param submission
	 * @return
	 */
	Vector<Submission> listSubmissions(Submission submission) {

		try {
			// String sql1 = "SELECT * FROM submissions WHERE ASSIGNMENTID=1 AND
			// COURSENUMBER=453";
			String sql2 = "SELECT * FROM submissions WHERE ASSIGNMENTID=" + submission.getAssignId()
					+ " AND COURSENUMBER=" + submission.getCourseId();

			Vector<Submission> listofSubmissions = new Vector<Submission>();

			statement = connection.prepareStatement(sql2);
			// statement.setInt(1, assignment.getCourseId());
			// statement.setInt(2, assignment.getAssignId());
			// System.out.println(sql1);
			// System.out.println(sql2);
			ResultSet subs = statement.executeQuery(sql2);

			while (subs.next()) {
				Submission temp = new Submission(subs.getInt("SID"), subs.getInt("COURSENUMBER"),
						subs.getInt("ASSIGNMENTID"), subs.getInt("STUDENTID"), subs.getString("FILEPATH"),
						subs.getInt("Grade"), subs.getString("COMMENT"), subs.getString("TITLE"),
						subs.getString("TIMESTAMP"), null);
				listofSubmissions.add(temp);
			}
			subs.close();
			return listofSubmissions;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Creates a prepared statement for listing a single student's submissions
	 * 
	 * @param submission
	 * @param studentid
	 * @return
	 */
	Vector<Submission> listSubmissions(Submission submission, int studentid) {

		try {
			String sql2 = "SELECT * FROM submissions WHERE ASSIGNMENTID=" + submission.getAssignId()
					+ " AND COURSENUMBER=" + submission.getCourseId() + " AND STUDENTID=" + studentid;

			Vector<Submission> listofSubmissions = new Vector<Submission>();

			statement = connection.prepareStatement(sql2);
			// statement.setInt(1, assignment.getCourseId());
			// statement.setInt(2, assignment.getAssignId());
			// System.out.println(sql1);
			// System.out.println(sql2);
			ResultSet subs = statement.executeQuery(sql2);

			while (subs.next()) {
				Submission temp = new Submission(subs.getInt("SID"), subs.getInt("COURSENUMBER"),
						subs.getInt("ASSIGNMENTID"), subs.getInt("STUDENTID"), subs.getString("FILEPATH"),
						subs.getInt("Grade"), subs.getString("COMMENT"), subs.getString("TITLE"),
						subs.getString("TIMESTAMP"), null);
				listofSubmissions.add(temp);
			}
			subs.close();
			return listofSubmissions;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Finds professor of a course
	 * 
	 * @param c
	 * @return
	 */
	public Professor findProf(Course c) {
		Professor results = new Professor();
		try {
			String sql = "SELECT * FROM users WHERE TYPE = ? AND ID = ?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, "p");
			statement.setInt(2, c.getProfId());

			ResultSet rset = statement.executeQuery();
			while (rset.next()) {
				results = new Professor(rset.getInt("ID"), rset.getString("firstname"), rset.getString("lastname"),
						rset.getString("EMAIL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Creates a prepared add statement for a message for the chat
	 * 
	 * @param chat
	 */
	public void preparedAdd(Chat chat) {
		String sql = "INSERT INTO chats VALUES (Default, ?, ?, ?)";
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, chat.getCoursenum());
			statement.setString(2, chat.getSender());
			statement.setString(3, chat.getMessage());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates a prepared statement for retrieving chat messages
	 * 
	 * @param course
	 * @return
	 */
	Vector<Chat> listChat(Course course) {

		try {
			String sql = "SELECT * FROM chats WHERE COURSENUMBER=" + course.getCourseId();

			Vector<Chat> listofChat = new Vector<Chat>();

			statement = connection.prepareStatement(sql);
			ResultSet chats = statement.executeQuery(sql);

			while (chats.next()) {
				Chat temp = new Chat(chats.getInt("COURSENUMBER"), chats.getString("SENDER"),
						chats.getString("MESSAGE"));
				listofChat.add(temp);
			}
			chats.close();
			return listofChat;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Creates a prepared statement for listing chat messages
	 * 
	 * @param chat
	 * @return
	 */
	Vector<Chat> listChat(Chat chat) {

		try {
			String sql = "SELECT * FROM chats WHERE COURSENUMBER=" + chat.getCoursenum();

			Vector<Chat> listofChat = new Vector<Chat>();

			statement = connection.prepareStatement(sql);
			ResultSet chats = statement.executeQuery(sql);

			while (chats.next()) {
				Chat temp = new Chat(chats.getInt("COURSENUMBER"), chats.getString("SENDER"),
						chats.getString("MESSAGE"));
				listofChat.add(temp);
			}
			chats.close();
			return listofChat;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

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

	// ~~~~~~~~~~~~~FOR_TESTING~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static void main(String args[]) {
		DatabaseHelper masterDB = new DatabaseHelper();
		Assignment a = new Assignment(1, 453, "Boolean Logic", "A1.txt", true, "Today", null);
		Vector<Submission> v = masterDB.listSubmissions(a);
		System.out.println(v);
		Course c = new Course(453, 1, "", true);
		Vector<Assignment> av = masterDB.listAssignmentsStudent(c);

		String s = masterDB.getAdminPW();
		try {
			System.out.println(s);
			System.out.println(s.equals("adminPW"));
		} catch (NullPointerException n) {
			n.printStackTrace();
		}
		/*
		 * System.out.println();
		 * System.out.println("Reading all courses from the table:");
		 * masterDB.preparedprintCourses();
		 * 
		 * System.out.
		 * println("\nSearching table for course 409: should return 'SoftwareDesign'");
		 * int courseID = 409; Course searchResult =
		 * masterDB.preparedsearchCourses(courseID); if (searchResult == null)
		 * System.out.println("Search Failed to find a course matching ID: " +
		 * courseID); else System.out.println("Search Result: " +
		 * searchResult.getCourseName());
		 * 
		 * System.out.
		 * println("Searching table for course 441: should fail to find a course");
		 * courseID = 441; searchResult = masterDB.preparedsearchCourses(courseID); if
		 * (searchResult == null)
		 * System.out.println("Search Failed to find a course matching ID: " +
		 * courseID); else System.out.println("Search Result: " +
		 * searchResult.getCourseName()); System.out.println();
		 * 
		 * System.out.println(); System.out.println("Trying to add course 200."); Course
		 * design = new Course(200, 3, "EnggDesign", false);
		 * masterDB.preparedAdd(design); masterDB.preparedprintCourses();
		 * System.out.println(); System.out.println("Trying to remove course 200.");
		 * masterDB.preparedRemove(design); masterDB.preparedprintCourses();
		 * 
		 * System.out.println(); System.out.println("Trying to set 225 active");
		 * masterDB.preparedSetActive(225, true); masterDB.preparedprintCourses();
		 * System.out.println(); System.out.println("Trying to set 225 inactive");
		 * masterDB.preparedSetActive(225, false); masterDB.preparedprintCourses();
		 * 
		 * System.out.println("\nThe program is finished running through the courses");
		 * System.out.println(); System.out.println();
		 * 
		 * System.out.println("Reading all users from the table:");
		 * masterDB.preparedprintUsers();
		 * 
		 * System.out.println(); System.out.println("Student Logins");
		 * System.out.println("Trying to login as Will. Should succeed:"); if
		 * (masterDB.isValidStudentLogin("will", "pw")) {
		 * System.out.println("Successful login"); } else {
		 * System.err.println("Failed login"); }
		 * 
		 * System.out.println("Trying to login as David. Should fail:"); if
		 * (masterDB.isValidStudentLogin("dave", "asdf")) {
		 * System.out.println("Successful login"); } else {
		 * System.out.println("Failed login"); }
		 * 
		 * System.out.println(); System.out.println("Professor Logins");
		 * System.out.println("Trying to login as Will. Should fail:"); if
		 * (masterDB.isValidProfLogin("will", "pw")) {
		 * System.out.println("Successful login"); } else {
		 * System.out.println("Failed login"); }
		 * 
		 * System.out.println("Trying to login as Norm. Should Succeed:"); if
		 * (masterDB.isValidProfLogin("norm", "42")) {
		 * System.out.println("Successful login"); } else {
		 * System.out.println("Failed login"); } System.out.println();
		 * System.out.println("Trying to add user David."); User dave = new User(10,
		 * "David", "Parkin", "S", "dparkin@test.com"); masterDB.preparedAdd(dave,
		 * "dparkin", "pass"); masterDB.preparedprintUsers(); System.out.println();
		 * System.out.println("Trying to remove user David.");
		 * masterDB.preparedRemove(dave); masterDB.preparedprintUsers();
		 * 
		 * System.out.println();
		 * System.out.println("Searching for Student 2. Should Return Will Ehman");
		 * Vector<User> result = masterDB.preparedSearchUsers(2);
		 * System.out.println(result.get(0).toString()); System.out.println();
		 * System.out.
		 * println("Searching for Students with last name Ehman. Should return 2 results"
		 * ); result = masterDB.preparedSearchUsers("Ehman"); for (int i = 0; i <
		 * result.size(); i++) { System.out.println(result.get(i).toString()); }
		 * System.out.
		 * println("Searching for Students with last name Ehman in 409. Should return 2 results"
		 * ); result = masterDB.preparedSearchUsersinCourse("Ehman", 409); for (int i =
		 * 0; i < result.size(); i++) { System.out.println(result.get(i).toString()); }
		 * System.out.
		 * println("Searching for Students with last name Ehman in 453. Should return 1 result"
		 * ); result = masterDB.preparedSearchUsersinCourse("Ehman", 453); for (int i =
		 * 0; i < result.size(); i++) { System.out.println(result.get(i).toString()); }
		 * 
		 * System.out.println("\nThe program is finished running through the users");
		 * 
		 * System.out.println();
		 * 
		 * System.out.println("Reading all enrolments from the table:");
		 * masterDB.preparedprintEnrolments();
		 * 
		 * System.out.println("Enrolling Will in 453"); masterDB.preparedEnrol(2, 453);
		 * masterDB.preparedprintEnrolments();
		 * 
		 * System.out.println("Unenrolling Will in 453"); masterDB.preparedUnenrol(2,
		 * 453); masterDB.preparedprintEnrolments();
		 * 
		 * System.out.println();
		 * 
		 * System.out.println("Reading all assignments from the table:");
		 * masterDB.preparedprintAssignments();
		 */

		try {
			masterDB.statement.close();
			masterDB.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nThe program is finished running through the Database");
		}

	}

}
