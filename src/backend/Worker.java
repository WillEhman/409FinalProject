package backend;

import java.io.*;
import java.net.Socket;

import shared.LoginInfo;

public class Worker implements Runnable {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;

	public Worker(Socket commSocket, Server server) throws IOException {

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

			try {
				
				
				
				
				
				try {
					login = (LoginInfo) in.readObject();
					if(server.getDatabase().isValidStudentLogin(login.getUsername(),login.getPassword())){
						out.writeObject("LOGIN SUCCESSFUL");
						System.out.println("HOLY SHIT");
					}
					System.out.println("HOLY SHIT AS WELL");
				}catch(Exception e) {}
				
				
				
			

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
