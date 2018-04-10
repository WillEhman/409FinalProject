package backend;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

import shared.Assignment;
import shared.Chat;
import shared.Course;
import shared.Email;
import shared.LoginInfo;
import shared.Message;
import shared.Professor;
import shared.Student;
import shared.Submission;
import shared.User;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 *
 */
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

				// Syntax is ADDUSER.SPLITTER.USERNAME.SPLITTER.PASSWORD.SPLITTER.ADMINPW
				if (inMessage.getQuery().contains("ADDUSER")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");
					User u = (User) inMessage.getObject();
					boolean success = false;
					try {
						if (split[split.length - 1].equals(database.getAdminPW())) {
							success = database.preparedAdd(u, split[split.length - 3], split[split.length - 2]);

						}
						if (success) {
							Message<?> outMessage = new Message<String>("Success", "Success");
							System.out.println("Success");
							out.writeObject(outMessage);
						} else {
							throw new Exception();
						}

					} catch (Exception e) {
						Message<?> outMessage = new Message<String>("Failed", "Failed");
						System.out.println("Failure");
						out.writeObject(outMessage);
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

				if (inMessage.getQuery().equals("COURSELIST")
						&& inMessage.getObject().getClass().toString().contains("Student")) {
					Vector<Course> cVector = new Vector<Course>();
					cVector = database.listCourses((Student) inMessage.getObject());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Course>>(cVector, "COURSELIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("STUDENTLIST")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					// String [] split = inMessage.getQuery().split(".SPLITTER.");
					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse((Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "STUDENTLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("ENROLLSTUDENT")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");

					// Course courseToUpdate = (Course) inMessage.getObject();
					// System.out.println(split);
					boolean result = database.preparedEnrol(Integer.parseInt(split[split.length - 1]),
							(Course) inMessage.getObject());

					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse((Course) inMessage.getObject());
					System.out.println(uVector);
					Message<?> outMessage;
					if (result) {
						outMessage = new Message<Vector<User>>(uVector, "Success");
						System.out.println("Success");
					} else {
						outMessage = new Message<Vector<User>>(uVector, "Failure");
						System.out.println("Failure");
					}
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("REMOVESTUDENT")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					String[] split = inMessage.getQuery().split(".SPLITTER.");

					// Course courseToUpdate = (Course) inMessage.getObject();
					database.preparedUnenrol(Integer.parseInt(split[split.length - 1]), (Course) inMessage.getObject());

					Vector<User> uVector = new Vector<User>();
					uVector = database.preparedSearchUsersinCourse((Course) inMessage.getObject());
					System.out.println("to be returned:" + uVector);
					Message<?> outMessage = new Message<Vector<User>>(uVector, "REMOVESTUDENT");
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
					boolean result = database.preparedAdd(newcourse);

					Vector<Course> cVector = new Vector<Course>();
					cVector = database.listCourses(newcourse.getProfId());
					System.out.println(cVector);
					Message<?> outMessage;
					if (result) {
						outMessage = new Message<Vector<Course>>(cVector, "Success");
					} else {
						outMessage = new Message<Vector<Course>>(cVector, "Failure");
					}
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

				if (inMessage.getQuery().equals("TOGGLECOURSE")
						&& inMessage.getObject().getClass().toString().contains("Course")) {

					Course courseToUpdate = (Course) inMessage.getObject();
					database.preparedToggle(courseToUpdate);

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

				// TODO implement different versions for prof and student that only show when
				// flagged as active
				if (inMessage.getQuery().equals("ASSIGNMENTLISTPROF")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					Vector<Assignment> aVector = new Vector<Assignment>();
					aVector = database.listAssignmentsProf((Course) inMessage.getObject());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Assignment>>(aVector, "ASSIGNMENTLISTPROF");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("ASSIGNMENTLISTSTUDENT")
						&& inMessage.getObject().getClass().toString().contains("Course")) {

					Vector<Assignment> aVector = new Vector<Assignment>();
					aVector = database.listAssignmentsStudent((Course) inMessage.getObject());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Assignment>>(aVector, "ASSIGNMENTLISTSTUDENT");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("ASSIGNMENTDUE")
						&& inMessage.getObject().getClass().toString().contains("Assignment")) {
					Assignment a = (Assignment) inMessage.getObject();
					database.preparedSetDue(a, a.getDueDate());

					Vector<Assignment> aVector = new Vector<Assignment>();
					aVector = database.listAssignmentsProf(a.getCourseId());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Assignment>>(aVector, "ASSIGNMENTDUE");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("ACTIVATEASSIGNMENT")
						&& inMessage.getObject().getClass().toString().contains("Assignment")) {

					Assignment a = (Assignment) inMessage.getObject();
					database.preparedSetActive(a, !a.isActive());

					Vector<Assignment> aVector = new Vector<Assignment>();
					aVector = database.listAssignmentsProf(a.getCourseId());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Assignment>>(aVector, "ACTIVATEASSIGNMENT");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("SUBMISSIONPROFLIST")
						&& inMessage.getObject().getClass().toString().contains("Assignment")) {
					Vector<Submission> aVector = new Vector<Submission>();
					aVector = database.listSubmissions((Assignment) inMessage.getObject());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Submission>>(aVector, "SUBMISSIONPROFLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("SUBMISSIONLIST")
						&& inMessage.getObject().getClass().toString().contains("Assignment")) {

					String[] split = inMessage.getQuery().split(".SPLITTER.");

					Vector<Submission> aVector = new Vector<Submission>();
					aVector = database.listSubmissions((Assignment) inMessage.getObject(),
							Integer.parseInt(split[split.length - 1]));
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Submission>>(aVector, "SUBMISSIONLIST");
					out.writeObject(outMessage);
				}

				// TODO convert file sending to send back a submission filled with byte data
				// rather than just sending the data
				if (inMessage.getQuery().equals("VIEWSUBMISSION")
						&& inMessage.getObject().getClass().toString().contains("Submission")) {
					Submission s = (Submission) inMessage.getObject();// Should contain path i.e (test.txt)
					String path = s.getPath();
					byte[] data = null;
					try {
						data = fileManager.readFileContent(path);
						Message<?> outMessage = new Message<byte[]>(data, "Success");
						out.writeObject(outMessage);
					} catch (Exception e) {
						e.printStackTrace();
						Message<?> outMessage = new Message<byte[]>(data, "Failure");
						out.writeObject(outMessage);
					}

					System.out.println("Message sent back");
				}

				if (inMessage.getQuery().contains("UPLOADSUBMISSION")
						&& inMessage.getObject().getClass().toString().contains("Submission")) {
					Submission s = (Submission) inMessage.getObject();
					byte[] input = s.getData();
					database.preparedAdd(s);
					boolean result = fileManager.writeFileContent(input, inMessage.getQuery());

					Vector<Submission> aVector = database.listSubmissions(s, s.getStudentId());
					System.out.println(aVector);

					Message<?> outMessage;
					if (result) {
						outMessage = new Message<Vector<Submission>>(aVector, "Success");
					} else {
						outMessage = new Message<Vector<Submission>>(aVector, "Failure");
					}
					out.writeObject(outMessage);

				}

				if (inMessage.getQuery().equals("SETGRADE")
						&& inMessage.getObject().getClass().toString().contains("Submission")) {
					Submission s = (Submission) inMessage.getObject();

					database.preparedRemove(s);
					database.preparedAdd(s);

					Vector<Submission> aVector = new Vector<Submission>();
					aVector = database.listSubmissions((Submission) inMessage.getObject());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Submission>>(aVector, "SUBMISSIONLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("SETCOMMENT")
						&& inMessage.getObject().getClass().toString().contains("Submission")) {
					Submission s = (Submission) inMessage.getObject();

					database.preparedRemove(s);
					database.preparedAdd(s);

					Vector<Submission> aVector = new Vector<Submission>();
					aVector = database.listSubmissions((Submission) inMessage.getObject());
					System.out.println(aVector);
					Message<?> outMessage = new Message<Vector<Submission>>(aVector, "SUBMISSIONLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("GETPROF")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					Course c = (Course) inMessage.getObject();

					Professor p = database.findProf(c);
					Message<?> outMessage = new Message<Professor>(p, "GETPROF");
					out.writeObject(outMessage);
				}

				// Should contain path in query in form CREATEFILE.SPLITTER.TEST.SPLITTER.txt
				// Should contain data in object in form byte[]
				if (inMessage.getQuery().contains("CREATEFILE")) {
					Assignment a = (Assignment) inMessage.getObject();
					byte[] input = a.getBytes();
					database.preparedAdd(a);
					boolean result = fileManager.writeFileContent(input, inMessage.getQuery());

					Vector<Assignment> aVector = new Vector<Assignment>();
					aVector = database.listAssignmentsProf(a.getCourseId());
					System.out.println(aVector);
					Message<?> outMessage;
					if (result) {
						outMessage = new Message<Vector<Assignment>>(aVector, "Success");
					} else {
						outMessage = new Message<Vector<Assignment>>(aVector, "Failure");
					}
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().contains("READFILE")
						&& inMessage.getObject().getClass().toString().contains("Assignment")) {
					Assignment a = (Assignment) inMessage.getObject();// Should contain path i.e (test.txt)
					String path = a.getPath();
					byte[] data = null;
					Message<?> outMessage;
					try {
						data = fileManager.readFileContent(path);
						outMessage = new Message<byte[]>(data, "Success");
					} catch (Exception e) {
						e.printStackTrace();
						outMessage = new Message<byte[]>(data, "Failure");
					}
					out.writeObject(outMessage);
					System.out.println("Message sent back");
				}

				if (inMessage.getQuery().contains("SENDEMAIL")
						&& inMessage.getObject().getClass().toString().contains("Email")) {
					String response = "";
					Email e = (Email) inMessage.getObject();
					for (int i = 0; i < e.getTo().size(); i++) {
						System.out.println("Sent email");
						response = emailService.SendEmail(e.getFrom(), e.getPw(), e.getTo().get(i), e.getSubject(),
								e.getContent());
					}

					Message<?> outMessage = new Message<String>(response, response);
					System.out.println(outMessage);
					out.writeObject(outMessage);

				}

				if (inMessage.getQuery().equals("CHATLIST")
						&& inMessage.getObject().getClass().toString().contains("Course")) {
					Vector<Chat> cVector = new Vector<Chat>();
					cVector = database.listChat((Course) inMessage.getObject());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Chat>>(cVector, "CHATLIST");
					out.writeObject(outMessage);
				}

				if (inMessage.getQuery().equals("SENDCHAT")
						&& inMessage.getObject().getClass().toString().contains("Chat")) {
					Chat chat = (Chat) inMessage.getObject();
					database.preparedAdd(chat);

					Vector<Chat> cVector = new Vector<Chat>();
					cVector = database.listChat((Course) inMessage.getObject());
					System.out.println(cVector);
					Message<?> outMessage = new Message<Vector<Chat>>(cVector, "CHATLIST");
					out.writeObject(outMessage);
				}

			} catch (IOException e) {
				System.out.println("Client Disconnected");
				break;
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
				break;
			}
		}

		try {
			in.close();
			out.close();
		} catch (Exception e) {
			System.out.println("Ending Exception: " + e.getMessage());
		}
	}

	// TODO see if necessary
	void closeConnection() {

	}

}
