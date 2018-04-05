package backend;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

import shared.Course;
import shared.LoginInfo;
import shared.Message;
import shared.Professor;
import shared.User;

public class Worker implements Runnable {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	// private Server server;
	private FileHelper fileManager;
	private DatabaseHelper database;
	private EmailHelper emailService;

	public Worker(Socket commSocket, Server server) throws IOException {
		System.out.println("\n" + "|-----Created Worker-----|");
		in = new ObjectInputStream(commSocket.getInputStream());
		out = new ObjectOutputStream(commSocket.getOutputStream());
	}

	// public Worker(Socket commSocket, DatabaseHelper db, FileHelper fm,
	// EmailHelper eh) throws IOException {
	//
	// in = new ObjectInputStream(commSocket.getInputStream());
	// out = new ObjectOutputStream(commSocket.getOutputStream());
	// // fileManager = fm;
	// // database = db;
	// // emailService = eh;
	// }

	public Worker(Socket commSocket, FileHelper fileManager2, DatabaseHelper database2, EmailHelper emailService2) throws IOException {
		// TODO Auto-generated constructor stub
		System.out.println("\n" + "|-----Created Worker-----|");
		in = new ObjectInputStream(commSocket.getInputStream());
		out = new ObjectOutputStream(commSocket.getOutputStream());
		fileManager = fileManager2;
		database = database2;
		emailService = emailService2;
	}

	@Override
	public void run() {
		LoginInfo login = null;
		System.out.println("|---Running...");
		try {
			Message<?> inMessage = (Message<?>) in.readObject();
			
			if (inMessage.getQuery().equals("LOGIN") && inMessage.getObject().getClass().toString().contains("LoginInfo")) {
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
					}
					else if (database.isValidProfLogin(login.getUsername(), login.getPassword())) {
						Message<?> outMessage = new Message<User>(user, "PROFLOGIN");
						out.writeObject(outMessage);
						System.out.println("|---Successful login of Professor");
					}else {
						user = null;
						Message<?> outMessage = new Message<User>(user, null);
						out.writeObject(outMessage);
						System.err.println("|---Unsuccessful login");
					}
				} catch (IOException e) {
					System.err.println("|---Client Disconnected---|" + "\n");
				}
			}
			
			if (inMessage.getQuery().equals("COURSE") && inMessage.getObject().getClass().toString().contains("Professor")) {
				Vector<Course> cVector = new Vector<Course>();
				cVector = database.listCourses((Professor)inMessage.getObject());
				Message<?> outMessage = new Message<Vector>(cVector, "PROFLOGIN");
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

	void closeConnection() {

	}
	
	

}
