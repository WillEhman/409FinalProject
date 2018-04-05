package frontend;

import java.io.*;
import java.net.*;

import com.mysql.jdbc.log.Log;

import shared.LoginInfo;
import shared.Message;

public class Client implements Serializable {
	public Socket socket;
	public ObjectInputStream in;
	public ObjectOutputStream out;
	// private LoginWindow logingui;
	// private boolean login;

	public Client(String serverIp, int port) throws IOException {

		try {
			System.out.println("Setting up client");
			socket = new Socket(serverIp, port);
			System.out.println("Setting up out socket");
			out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("Setting up in socket");
			in = new ObjectInputStream(socket.getInputStream());

			System.out.println("Client now Running");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Message<?> communicate(Message<?> message) {
		try {
			System.out.println(message);
			out.writeObject(message);
			if ((message = (Message<?>) in.readObject()) != null) {
				return message;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// public void communicate(LoginWindow gui) {
	// // System.out.println("in communicate:");
	// // System.out.println(l.getUsername());
	// // System.out.println(l.getPassword());
	// if (l.getUsername() != null && l.getPassword() != null) {
	// System.out.println("Its not NULL");
	// try {
	//
	// out.writeObject(l);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// try {
	// System.out.println(in.readObject());
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	public File getFile(String path) {
		// TODO
		return null;
	}

	// public static void main(String[] args) throws IOException {
	// Client client = new Client("localhost", 9090);
	// // Client client = new Client("10.13.166.195", 9090);
	//
	// LoginWindow logingui = new LoginWindow(client.l);
	//
	// while (true) {
	// // System.out.println("In while");
	// System.out.println(client.l.getUsername());
	// System.out.println(client.l.getPassword());
	// client.communicate(logingui);
	// }
	//
	// // System.out.println("WE ESCAPED");
	//
	// }
}
