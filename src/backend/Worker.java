package backend;

import java.io.*;
import java.net.Socket;
import java.time.*;

import shared.LoginInfo;

public class Worker implements Runnable {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;
	private Clock clock;

	public Worker(Socket commSocket, Server server) throws IOException {
		System.out.println("\n" + "|-----Created Worker-----|");
		in = new ObjectInputStream(commSocket.getInputStream());
		out = new ObjectOutputStream(commSocket.getOutputStream());
		this.server = server;
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

	@Override
	public void run() {
			LoginInfo login = null;
			System.out.println("|---Running...");
			try {
				System.out.println("|---Logging in...");
				try {
					login = (LoginInfo) in.readObject();
//					System.out.print(login.getPassword() + "  " + login.getUsername() + "\n");
//					System.out.println(server.getDatabase());
					if(server.getDatabase().isValidStudentLogin(login.getUsername(),login.getPassword())){
						out.writeObject("Login Successful");
						System.out.println("|---Successful login");
					}
					else {
						out.writeObject("UH OH! Login Failed");
						System.err.println("|---Unsuccessful login");
					}
				}catch(IOException e) {
					System.err.println("|---Client Disconnected---|" + "\n");
				}
				
				
				
			

//			} catch (IOException e) {
//				System.out.println("Client Disconnected");
//				break;
//			} catch (ClassNotFoundException e) {
//				System.out.println(e.getMessage());
//				break;
//			}
				
				
				
				
				
			}catch(Exception e) {
			
			}

		try {
//			in.close();
//			out.close();
		} catch (Exception e) {
			System.out.println("Ending Exception: " + e.getMessage());
		}

	}

	void closeConnection() {

	}

}
