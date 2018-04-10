package frontend;

import java.io.*;
import java.net.*;
import shared.Message;

/**
 * 
 * @author William Ehman
 * @author David Parkin
 * @author Luke Kushneryk
 * @since April 5 2018
 * @version 1.0
 * 
 *          Client for client-server communication
 *
 */
public class Client implements Serializable {
	/**
	 * Standard sockets and input/output streams
	 */
	public Socket socket;
	public ObjectInputStream in;
	public ObjectOutputStream out;

	/**
	 * Constructor for client
	 * 
	 * @param serverIp
	 * @param port
	 * @throws IOException
	 */
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

	/**
	 * Communicate facilitates client server communication
	 * 
	 * @param message
	 *            is message received
	 * @return message received
	 */
	public Message<?> communicate(Message<?> message) {
		System.out.println("Starting Communication");
		try {
			System.out.println(message.getQuery());
			out.writeObject(message);
			System.out.println("Message Sent");
			if ((message = (Message<?>) in.readObject()) != null) {
				System.out.println("Got the Message");
				return message;
			}
			System.out.println("got null Message");
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
