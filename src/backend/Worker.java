package backend;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

import shared.Assignment;
import shared.Course;
import shared.LoginInfo;
import shared.Message;
import shared.Professor;
import shared.User;

public class Worker implements Runnable {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private FileHelper fileManager;
	private DatabaseHelper database;
	private EmailHelper emailService;

	public Worker(Socket commSocket, Server server) throws IOException {
		System.out.println("\n" + "|-----Created Worker-----|");
		in = new ObjectInputStream(commSocket.getInputStream());
		out = new ObjectOutputStream(commSocket.getOutputStream());
	}

	public Worker(Socket commSocket, FileHelper fileManager2, DatabaseHelper database2, EmailHelper emailService2)
			throws IOException {
		// TODO Auto-generated constructor stub
		System.out.println("\n" + "|-----Created Worker-----|");
		in = new ObjectInputStream(commSocket.getInputStream());
		out = new ObjectOutputStream(commSocket.getOutputStream());
		fileManager = fileManager2;
		database = database2;
		emailService = emailService2;
	}

	@Override
	// TODO implement proper outputs for catch statements. Possibly convert to
	// switch case
	public void run() {
		LoginInfo login = null;
		System.out.println("|---Running...");
		while (true) {

			try {
				Message<?> inMessage = (Message<?>) in.readObject();
				System.out.println("\nInput received: ");
				System.out.println(inMessage.getObject());
				System.out.println(inMessage.getQuery());

				if (inMessage.getQuery().equals("LOGIN")
						&& inMessage.getObject().getClass().toString().contains("LoginInfo")) {
					System.out.println("|---Logging in...");
					try {
						login = (LoginInfo) inMessage.getObject();
						User user = database.Login(login);
						// System.out.print(login.getPassword() + " " + login.getUsername() + "\n");
						// System.out.println(server.getDatabase());

						if (database.isValidStudentLogin(login.getUsername(), login.getPassword())) {
							Message<?> outMessage = new Message<User>(user, "STUDENTLOGIN");
							out.writeObject(outMessage);
							System.out.println("|---Successful login of Student");
						} else if (database.isValidProfLogin(login.getUsername(), login.getPassword())) {
							Message<?> outMessage = new Message<User>(user, "PROFLOGIN");
							out.writeObject(outMessage);
							System.out.println("|---Successful login of Professor");
						} else {
							user = null;
							Message<?> outMessage = new Message<User>(user, null);
							out.writeObject(outMessage);
							System.err.println("|---Unsuccessful login");
						}
					} catch (IOException e) {
						System.err.println("|---Client Disconnected---|" + "\n");
					}
				}

				if (inMessage.getQuery().equals("COURSELIST")
						&& inMessage.getObject().getClass().toString().contains("Professor")) {
					Vector<Course> cVector = new Vector<Course>();
					cVector = database.listCourses((Professor) inMessage.getObject());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Course>>(cVector, "COURSELIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("STUDENTLIST")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					// String [] split = inMessage.getQuery().split(".SPLITTER.");
					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse((Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "STUDENTLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("ENROLSTUDENT")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");

					// Course courseToUpdate = (Course) inMessage.getObject();
					database.preparedEnrol(Integer.parseInt(split[split.length - 1]), (Course) inMessage.getObject());

					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse((Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "ENROLSTUDENT");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("UNENROLSTUDENT")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");

					// Course courseToUpdate = (Course) inMessage.getObject();
					database.preparedUnenrol(Integer.parseInt(split[split.length - 1]), (Course) inMessage.getObject());

					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse((Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "ENROLSTUDENT");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("SEARCHSTUDENTSID")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");
					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse(Integer.parseInt(split[split.length - 1]),
							(Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "STUDENTLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("SEARCHSTUDENTSLN")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");
					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse(split[split.length - 1],
							(Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "STUDENTLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("ADDCOURSE")
						&& inMessage.getObject().getClass().toString().contains("Course")) {

					Course newcourse = (Course) inMessage.getObject();
					database.preparedAdd(newcourse);

					Vector<Course> cVector = new Vector<Course>();
					cVector = database.listCourses(newcourse.getProfId());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Course>>(cVector, "ADDCOURSE");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("UPDATECOURSE")
						&& inMessage.getObject().getClass().toString().contains("Course")) {

					Course courseToUpdate = (Course) inMessage.getObject();
					database.preparedRemove(courseToUpdate);
					database.preparedAdd(courseToUpdate);

					Vector<Course> cVector = new Vector<Course>();
					cVector = database.listCourses(courseToUpdate.getProfId());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Course>>(cVector, "UPDATECOURSE");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("REMOVECOURSE")
						&& inMessage.getObject().getClass().toString().contains("Course")) {

					Course removedcourse = (Course) inMessage.getObject();
					database.preparedRemove(removedcourse);

					Vector<Course> cVector = new Vector<Course>();
					cVector = database.listCourses(removedcourse.getProfId());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Course>>(cVector, "REMOVECOURSE");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("ASSIGNMENTLIST")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					Vector<Assignment> aVector = new Vector<Assignment>();
					aVector = database.listAssignments((Course) inMessage.getObject());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Assignment>>(aVector, "ASSIGNMENTLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("ACTIVATEASSIGNMENT")
						&& inMessage.getObject().getClass().toString().contains("Assignment")) {

					Assignment a = (Assignment) inMessage.getObject();
					database.preparedSetActive(a, true);

					Message<?> outMessage = null;
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("CREATEFILE")) {
					byte[] input = (byte[]) inMessage.getObject();
					fileManager.writeFileContent(input, inMessage.getQuery());
					out.writeObject(null);
				}

				if (inMessage.getQuery().contains("READFILE")) {
					String input = (String) inMessage.getObject();// Should contain path i.e (test.txt)
					fileManager.readFileContent(input);
					Assignment a = null;
					Message<?> outMessage = new Message<Assignment>(a, null);
					out.writeObject(outMessage);
				}

				// } catch (IOException e) {
				// System.out.println("Client Disconnected");
				// break;
				// } catch (ClassNotFoundException e) {
				// System.out.println(e.getMessage());
				// break;
				// }

			} catch (Exception e) {

			}

			try {
				// in.close();
				// out.close();
			} catch (Exception e) {
				System.out.println("Ending Exception: " + e.getMessage());
			}
		}

	}

	// TODO see if necessary
	void closeConnection() {

	}

}
